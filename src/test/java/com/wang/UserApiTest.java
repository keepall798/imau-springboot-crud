package com.wang;

import com.wang.controller.UserController;
import com.wang.exception.BizException;
import com.wang.service.UserService;
import com.wang.vo.UserVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Controller 层单元测试。
 * <p>
 * @WebMvcTest 只启动 Web 层，不启动 Tomcat、不连数据库；
 * @MockitoBean 给 UserService 造一个假实现，我们只验证"接口契约"：
 * 路径对不对、HTTP 状态码对不对、返回的 JSON 结构对不对。
 * <p>
 * 业务逻辑由 Service 层单独测，两层分开，失败时一眼看出是谁的问题。
 * <p>
 * 小知识：@MockitoBean 是 Spring Boot 3.4+ 的新注解，老教程里的 @MockBean 已标记过时。
 */
@WebMvcTest(UserController.class)
class UserApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void 查询用户_成功时返回统一结构和HTTP200() throws Exception {
        given(userService.getByUsername("丁真")).willReturn(new UserVO("丁真"));

        mockMvc.perform(get("/users/丁真"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("ok"))
                .andExpect(jsonPath("$.data.username").value("丁真"));
    }

    @Test
    void 查询用户_不存在时返回HTTP404和业务码404() throws Exception {
        given(userService.getByUsername("不存在的人"))
                .willThrow(new BizException(404, "用户不存在：不存在的人"));

        mockMvc.perform(get("/users/不存在的人"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.msg").value("用户不存在：不存在的人"));
    }

    @Test
    void 新增用户_参数不合法时返回HTTP400和字段提示() throws Exception {
        String badJson = """
                {"username":"","password":"1"}
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.msg").value(org.hamcrest.Matchers.containsString("用户名不能为空")));
    }
}

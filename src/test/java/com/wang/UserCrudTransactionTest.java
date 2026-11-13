package com.wang;

import com.wang.common.PageResult;
import com.wang.dto.UserCreateDTO;
import com.wang.dto.UserUpdateDTO;
import com.wang.exception.BizException;
import com.wang.service.UserService;
import com.wang.vo.UserVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 集成测试：真的连数据库，把"增 → 查 → 改 → 分页搜索 → 删"整条链路跑一遍。
 * <p>
 * 关键点是类上的 <b>@Transactional</b>：
 * Spring Test 会在每个测试方法结束后自动回滚事务，
 * 所以测试往数据库里插的数据不会留下痕迹，也不会污染真实数据。
 * 这就是"测试可以反复执行"的正确做法（你原来那份代码每跑一次就插三条，第二次直接主键冲突）。
 * <p>
 * 注意：@Transactional 在测试里是"测试结束后回滚"，在业务代码里是"保证一个业务原子性"，两者作用不同。
 */
@SpringBootTest
@Transactional
class UserCrudTransactionTest {

    /** 测试专用用户名，带前缀方便一眼认出 */
    private static final String TEST_USERNAME = "__verify_tmp_user__";

    @Autowired
    private UserService userService;

    @Test
    void 增删改查全流程() {
        // ---------- 1. 增 ----------
        UserCreateDTO createDTO = new UserCreateDTO();
        createDTO.setUsername(TEST_USERNAME);
        createDTO.setPassword("123456");
        userService.create(createDTO);

        // ---------- 2. 查 ----------
        UserVO found = userService.getByUsername(TEST_USERNAME);
        assertThat(found.username()).isEqualTo(TEST_USERNAME);
        // 返回的是 VO，只有 username 字段，password 不会出现在结果里
        assertThat(found).isNotNull();

        // ---------- 3. 改 ----------
        UserUpdateDTO updateDTO = new UserUpdateDTO();
        updateDTO.setNewPassword("654321");
        userService.updatePassword(TEST_USERNAME, updateDTO);

        // ---------- 4. 分页 + 模糊搜索 ----------
        PageResult<UserVO> page = userService.page("__verify_tmp", 1, 10);
        assertThat(page.total()).isEqualTo(1L);
        assertThat(page.records()).hasSize(1);
        assertThat(page.records().get(0).username()).isEqualTo(TEST_USERNAME);
        // pageNum/pageSize 会被兜底修正后回传，前端据此渲染分页条
        assertThat(page.pageNum()).isEqualTo(1L);
        assertThat(page.pageSize()).isEqualTo(10L);

        // ---------- 5. 删 ----------
        userService.delete(TEST_USERNAME);

        // ---------- 6. 删完再查，必须是 404 业务异常 ----------
        assertThatThrownBy(() -> userService.getByUsername(TEST_USERNAME))
                .isInstanceOf(BizException.class)
                .hasMessageContaining("用户不存在");
    }

    @Test
    void 修改不存在的用户返回404而不是空指针() {
        UserUpdateDTO updateDTO = new UserUpdateDTO();
        updateDTO.setNewPassword("654321");

        // 老写法 selectById 拿到 null 后直接 setPassword，会抛 NullPointerException（500）
        // 新写法 updateById 返回影响行数 0，明确抛业务异常 404
        assertThatThrownBy(() -> userService.updatePassword("这个人真的不存在", updateDTO))
                .isInstanceOf(BizException.class)
                .extracting(e -> ((BizException) e).getCode())
                .isEqualTo(404);
    }
}

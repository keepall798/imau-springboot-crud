package com.wang.controller;

import com.wang.common.PageResult;
import com.wang.common.Result;
import com.wang.dto.UserCreateDTO;
import com.wang.dto.UserUpdateDTO;
import com.wang.service.UserService;
import com.wang.vo.UserVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户接口层（Controller）。
 * <p>
 * 职责边界：只负责"收请求 → 调用 Service → 返回 Result"，不做业务判断、不碰数据库。
 * 就像餐厅服务员：接待、传话、上菜，不做菜、不去仓库搬货。
 * <p>
 * 【REST 路径规范】
 * <ul>
 *   <li>资源用复数小写：/users 而不是 /getUser</li>
 *   <li>动词交给 HTTP 方法表达：GET 查、POST 增、PUT 改、DELETE 删</li>
 *   <li>某个资源的标识放路径里：/users/{username}</li>
 *   <li>筛选/分页条件放查询参数：/users?keyword=丁&pageNum=1</li>
 * </ul>
 * <p>
 * 【返回什么】返回 Result 对象，让 Jackson 自动序列化成 application/json。
 * 老写法返回 String 再手动拼 JSON 的问题：Content-Type 是 text/plain，前端还要自己 JSON.parse。
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    /** 只依赖接口，不依赖实现类 */
    private final UserService userService;

    // ==================== 增 ====================

    /**
     * 新增用户。
     * <pre>
     * POST /users
     * Content-Type: application/json
     *
     * { "username": "李四", "password": "123456" }
     * </pre>
     * 成功：HTTP 200 + {"code":200,"msg":"ok","data":null}
     * 失败：HTTP 400（参数校验不通过）/ 409（用户名已存在）
     */
    @PostMapping
    public Result<Void> create(@Valid @RequestBody UserCreateDTO dto) {
        userService.create(dto);
        return Result.ok();
    }

    // ==================== 删 ====================

    /**
     * 删除用户。
     * <pre>DELETE /users/{username}</pre>
     * 成功：HTTP 200；用户不存在：HTTP 404
     */
    @DeleteMapping("/{username}")
    public Result<Void> delete(@PathVariable String username) {
        userService.delete(username);
        return Result.ok();
    }

    // ==================== 改 ====================

    /**
     * 修改密码。
     * <pre>
     * PUT /users/{username}
     * { "newPassword": "654321" }
     * </pre>
     * 成功：HTTP 200；用户不存在：HTTP 404
     */
    @PutMapping("/{username}")
    public Result<Void> updatePassword(@PathVariable String username,
                                       @Valid @RequestBody UserUpdateDTO dto) {
        userService.updatePassword(username, dto);
        return Result.ok();
    }

    // ==================== 查（单个） ====================

    /**
     * 按用户名查询。
     * <pre>GET /users/{username}</pre>
     * 成功：HTTP 200 + {"code":200,"data":{"username":"丁真"}}
     */
    @GetMapping("/{username}")
    public Result<UserVO> get(@PathVariable String username) {
        return Result.ok(userService.getByUsername(username));
    }

    // ==================== 查（列表 + 分页） ====================

    /**
     * 分页 + 模糊查询。
     * <pre>
     * GET /users                     查全部（第 1 页，每页 10 条）
     * GET /users?keyword=丁           模糊搜索
     * GET /users?pageNum=2&pageSize=5 分页
     * </pre>
     * 注意：@RequestParam 用在"查询条件"上；路径里的标识才用 @PathVariable；
     * 请求体才用 @RequestBody。三者别混。
     */
    @GetMapping
    public Result<PageResult<UserVO>> page(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "10") long pageSize) {
        return Result.ok(userService.page(keyword, pageNum, pageSize));
    }

    @GetMapping("/count")
    public Result<Long> count() {
        return Result.ok(userService.count());
    }
}

package com.wang.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 新增用户请求体（DTO = Data Transfer Object，专门承载"前端传进来的数据"）。
 * <p>
 * 为什么不直接用实体 User 接收？三个理由：
 * 1. 校验规则属于"接口契约"，不该混进数据库映射类；
 * 2. 前端能传什么由 DTO 决定，将来实体加字段（比如 create_time）不会影响接口；
 * 3. 实体字段和接口字段以后可能分道扬镳（比如前端传 password 明文，实体存哈希）。
 * <p>
 * 命名约定：XXXCreateDTO / XXXUpdateDTO，后缀表明用途。
 * <p>
 * 注意：Spring Boot 3 的校验注解在 jakarta.validation 包下，不是 javax.validation（抄老教程会报错）。
 */
@Data
public class UserCreateDTO {

    /** 用户名不能为空白，最长 32 个字符（和数据库 varchar(32) 保持一致） */
    @NotBlank(message = "用户名不能为空")
    @Size(max = 32, message = "用户名最长 32 个字符")
    private String username;

    /** 密码 6~32 位 */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 32, message = "密码长度必须在 6 到 32 之间")
    private String password;
}

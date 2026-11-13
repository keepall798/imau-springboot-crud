package com.wang.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 修改密码请求体。
 * <p>
 * 只允许传 newPassword —— 这就是 DTO 的作用：接口能改什么，由它说了算。
 * 想改用户名？没有这个字段，前端传了也会被忽略。
 */
@Data
public class UserUpdateDTO {

    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 32, message = "密码长度必须在 6 到 32 之间")
    private String newPassword;
}

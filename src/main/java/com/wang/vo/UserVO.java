package com.wang.vo;

import com.wang.entity.User;

/**
 * 用户视图对象（VO = View Object，专门承载"返回给前端的数据"）。
 * <p>
 * 只用它暴露 username，password 永远不出现在响应里。
 * <p>
 * 命名约定：XXXVO，后缀表明这是出参。
 * 经验法则：<b>Entity 里有的敏感字段，必须在 VO 里被"过滤"掉。</b>
 */
public record UserVO(String username) {

    /** Entity → VO 的转换方法，放在 VO 里，谁要转谁调用 */
    public static UserVO from(User user) {
        return new UserVO(user.getUsername());
    }
}

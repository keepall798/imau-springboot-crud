package com.wang.common;

/**
 * 统一返回体：所有接口都返回这个结构。
 * <pre>
 * {
 *   "code": 200,
 *   "msg":  "ok",
 *   "data": { ... }
 * }
 * </pre>
 * 用 record 而不是普通类：record 自动生成「字段 + 全参构造 + getter + equals/hashCode/toString」，
 * 正好适合这种"只装数据、创建后不再修改"的对象，能少写几十行。
 * <p>
 * 注意：实体类（User）不能用 record，因为 MyBatis-Plus 需要 setter 往对象里塞值。
 *
 * @param code 业务状态码：200 成功；400 参数错；404 没找到；409 冲突；500 服务端错误
 * @param msg  给前端看的提示语
 * @param data 真正的业务数据，没有就返回 null
 * @param <T>  数据类型
 */
public record Result<T>(int code, String msg, T data) {

    /** 成功，且没有数据要返回（新增/修改/删除用这个） */
    public static Result<Void> ok() {
        return new Result<>(200, "ok", null);
    }

    /** 成功，带数据（查询用这个） */
    public static <T> Result<T> ok(T data) {
        return new Result<>(200, "ok", data);
    }

    /** 成功，带自定义提示语 */
    public static <T> Result<T> ok(String msg, T data) {
        return new Result<>(200, msg, data);
    }

    /** 失败 */
    public static Result<Void> fail(int code, String msg) {
        return new Result<>(code, msg, null);
    }
}

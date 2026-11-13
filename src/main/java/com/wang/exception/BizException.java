package com.wang.exception;

/**
 * 业务异常：用来表达"这条请求不合规矩"（用户不存在、密码太简单……）。
 * <p>
 * 为什么继承 RuntimeException 而不是 Exception？
 * 因为 Spring 的 @Transactional 默认<b>只在运行时异常时回滚</b>事务。
 * 抛出后由 GlobalExceptionHandler 统一翻译成 JSON，业务代码里不用 try-catch。
 * <p>
 * 命名约定：XXXException，放在 exception 包下。
 */
public class BizException extends RuntimeException {

    /** 业务状态码，常用 400 / 404 / 409 */
    private final int code;

    public BizException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

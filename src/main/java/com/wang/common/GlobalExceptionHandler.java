package com.wang.common;

import com.wang.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理：项目里任何地方抛出的异常，最后都在这里被翻译成统一 JSON + 合适的 HTTP 状态码。
 * <p>
 * 好处：
 * 1. Controller / Service 里不需要写 try-catch，业务代码保持干净；
 * 2. 前端拿到的永远是 {code,msg,data} 结构，不会再突然收到一大页 HTML 报错；
 * 3. HTTP 状态码也正确（400/404/409/500），而不是全都 200。
 * <p>
 * 原理：@RestControllerAdvice 会拦截所有 Controller 抛出的异常，
 * 再用 @ExceptionHandler 按异常类型匹配处理。
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 业务异常：用户不存在、参数不合理等，状态码由抛出方指定（如 404） */
    @ExceptionHandler(BizException.class)
    public ResponseEntity<Result<Void>> handleBizException(BizException e) {
        HttpStatus status = HttpStatus.resolve(e.getCode());
        if (status == null) {
            status = HttpStatus.BAD_REQUEST;
        }
        return ResponseEntity.status(status).body(Result.fail(e.getCode(), e.getMessage()));
    }

    /**
     * 参数校验失败：Controller 上写了 @Valid，而 DTO 上的 @NotBlank / @Size 没通过时抛出。
     * 把每个字段的错误拼成一句话返回，前端能直接提示用户。
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<Void>> handleValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ResponseEntity.badRequest().body(Result.fail(400, message));
    }

    /** 主键冲突：插入一个已经存在的用户名 */
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Result<Void>> handleDuplicateKey(DuplicateKeyException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Result.fail(409, "用户名已存在"));
    }

    /**
     * 路径不存在：请求了一个没有任何接口的地址（打错字、少了字母 s、结尾多了斜杠）。
     * 如果不在这里单独处理，它会被下面的"兜底"分支接住，错误地返回 500，
     * 让人以为"服务器挂了"，其实是自己地址写错了。
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Result<Void>> handleNoResourceFound(NoResourceFoundException e) {
        log.warn("请求了不存在的接口：{}", e.getResourcePath());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Result.fail(404, "接口不存在，请检查路径和请求方法是否正确"));
    }

    /** 兜底：其他没预料到的异常。日志里留完整堆栈，但只给前端一句人话 */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> handleOtherException(Exception e) {
        log.error("未处理的异常", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.fail(500, "服务器内部错误，请查看服务端日志"));
    }
}

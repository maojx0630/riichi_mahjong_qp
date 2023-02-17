package com.github.maojx0630.mahjong.common.advice;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.github.maojx0630.mahjong.common.exception.StateEnum;
import com.github.maojx0630.mahjong.common.exception.StateException;
import com.github.maojx0630.mahjong.common.result.ResponseResult;
import com.github.maojx0630.mahjong.common.result.ResponseResultState;
import com.github.maojx0630.mahjong.common.util.RequestUtils;
import com.github.maojx0630.mahjong.common.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * 控制增强,自定义异常处理
 *
 * @author MaoJiaXing
 * @since 2019-07-21 18:56
 */
@RestControllerAdvice
@Slf4j
public class ResponseResultAdvice implements ResponseBodyAdvice<Object> {

  @Override
  public boolean supports(
          MethodParameter methodParameter, @NotNull Class<? extends HttpMessageConverter<?>> aClass) {
    if (!Objects.requireNonNull(methodParameter.getMethod())
        .getDeclaringClass()
        .getPackage()
        .getName()
        .startsWith(SpringUtils.getBasePackage())) {
      return false;
    }
    return !(methodParameter
            .getDeclaringClass()
            .isAnnotationPresent(IgnoreResponseResultAdvice.class)
        || Objects.requireNonNull(methodParameter.getMethod())
            .isAnnotationPresent(IgnoreResponseResultAdvice.class));
  }
  // 处理结果
  @Override
  public Object beforeBodyWrite(
          Object o,
          @NotNull MethodParameter methodParameter,
          @NotNull MediaType mediaType,
          @NotNull Class<? extends HttpMessageConverter<?>> aClass,
          @NotNull ServerHttpRequest serverHttpRequest,
          @NotNull ServerHttpResponse serverHttpResponse) {
    ResponseResultState responseResult;
    if (o instanceof ResponseResultState) {
      responseResult = (ResponseResultState) o;
    } else {
      responseResult = ResponseResult.of(StateEnum.success, o);
    }
    String s = JSON.toJSONString(responseResult);
    if (s.length() > 500) {
      log.debug(s.substring(0, 400) + "......");
    } else {
      log.debug(s);
    }
    return responseResult;
  }

  /** 自定义异常处理 */
  @ExceptionHandler(StateException.class)
  public ResponseResultState stateException(StateException exception) {
    return ResponseResult.of(exception);
  }

  private ResponseResultState test(Throwable throwable, int number) {
    if (number > 3 || throwable == null) {
      return null;
    } else if (throwable instanceof StateException) {
      return ResponseResult.of((StateException) throwable);
    } else {
      number++;
      return test(throwable.getCause(), number);
    }
  }
  // 自定义异常处理结束
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseResultState httpMessageNotReadableException(
      HttpMessageNotReadableException exception) {
    String msg = exception.getMessage();
    String paramName = StrUtil.subBefore(StrUtil.subAfter(msg, "[\"", true), "\"]", true);
    String message = StrUtil.subAfter(msg, ":", false);
    message = StrUtil.subBefore(message, ":", false).trim();
    return StateEnum.invalid_format_exception
        .build()
        .setMsg("[\"" + paramName + "\"] " + message)
        .setData(exception.getMessage());
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseResultState methodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException exception) {
    return StateEnum.invalid_format_exception
        .build()
        .setMsg(String.format("参数[%s]不能被转换,传入值为[%s]", exception.getName(), exception.getValue()))
        .setData(exception.getMessage());
  }
  // 参数校验异常区域
  @ExceptionHandler(BindException.class)
  public ResponseResultState constraintViolationException(BindException e) {
    return bindingResult(e.getBindingResult());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseResultState methodArgumentNotValidException(MethodArgumentNotValidException e) {
    return bindingResult(e.getBindingResult());
  }

  private ResponseResultState bindingResult(BindingResult e) {
    List<BindingResultMsg> list = new LinkedList<>();
    e.getFieldErrors()
        .forEach(
            err ->
                list.add(
                    new BindingResultMsg(
                        err.getField(), err.getDefaultMessage(), err.getRejectedValue())));
    return ResponseResult.of(StateEnum.valid_error)
        .setData(list)
        .appendMsg(" >>> 错误数量 : " + e.getFieldErrorCount());
  }
  // 参数校验区域结束

  /** 请求类型不匹配异常处理 */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseResultState httpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException e) {
    String str =
        "{ " + RequestUtils.getRequest().getRequestURI() + " } { " + e.getMessage() + " } ";
    return StateEnum.no_found.build().appendMsg(str);
  }

  /** 其他异常处理 */
  @ExceptionHandler(Exception.class)
  public ResponseResultState exception(Exception e) {
    ResponseResultState responseResult = test(e, 0);
    if (responseResult == null) {
      log.error("发生了异常，异常堆栈如下", e);
      return ResponseResult.of(StateEnum.error, e.getMessage());
    } else {
      if (Objects.equals(responseResult.getState(), StateEnum.error.getState())) {
        log.error("发生了异常，异常堆栈如下", e);
      }
      return responseResult;
    }
  }
}

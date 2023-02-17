package com.github.maojx0630.mahjong.spring.interceptor;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.github.maojx0630.mahjong.common.exception.StateEnum;
import com.github.maojx0630.mahjong.utils.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 日志拦截器
 *
 * @author MaoJiaXing
 * @since 2019-07-21 18:54
 */
public class HomeLogInterceptor implements HandlerInterceptor {

  private static final Logger LOGGER = LoggerFactory.getLogger(HomeLogInterceptor.class);

  private static final ThreadLocal<TimeInterval> LONG_THREAD_LOCAL = new ThreadLocal<>();

  @Override
  public boolean preHandle(
      @Nonnull HttpServletRequest request,
      @Nonnull HttpServletResponse response,
      @Nonnull Object handler) {
    if (!(handler instanceof HandlerMethod)) {
      ResponseUtils.response(
          response, StateEnum.no_found.build().append("{" + request.getRequestURI() + "}"));
      return false;
    }
    LONG_THREAD_LOCAL.set(DateUtil.timer());
    LOGGER.info("▁▂▃▄▅▆▇█本次请求路径[{}]█▇▆▅▄▃▂▁ ", request.getRequestURI());
    return true;
  }

  @Override
  public void afterCompletion(
      @Nonnull HttpServletRequest request,
      @Nonnull HttpServletResponse response,
      @Nonnull Object handler,
      Exception ex) {
    HandlerMethod handlerMethod = (HandlerMethod) handler;
    LOGGER.info(
        "▁▂▃▄▅▆▇█执行完成[{}]方法█▇▆▅▄▃▂▁ 耗时：{} ms\n",
        handlerMethod.getMethod().getName(),
        getAndRemove());
  }

  private Long getAndRemove() {
    Long aLong = LONG_THREAD_LOCAL.get().interval();
    LONG_THREAD_LOCAL.remove();
    return aLong;
  }
}

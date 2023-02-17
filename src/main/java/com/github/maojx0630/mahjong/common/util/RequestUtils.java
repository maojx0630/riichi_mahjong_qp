package com.github.maojx0630.mahjong.common.util;

import lombok.experimental.UtilityClass;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * request工具类
 *
 * @author MaoJiaXing
 * @since 2020-12-14 11:25
 */
@UtilityClass
public class RequestUtils {

  /**
   * 获取当前请求的request
   *
   * @return javax.servlet.http.HttpServletRequest
   * @author MaoJiaXing
   * @since 2020-12-14 11:26
   */
  public HttpServletRequest getRequest() {
    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    if (Objects.isNull(requestAttributes)) {
      return null;
    } else {
      return ((ServletRequestAttributes) requestAttributes).getRequest();
    }
  }

  /**
   * 获取指定请求参数
   *
   * @param string name
   * @return java.lang.String
   * @author MaoJiaXing
   * @since 2020-04-13 14:55
   */
  public String getParam(String string) {
    return getRequest().getParameter(string);
  }

  /**
   * 获取指定Header
   *
   * @param string name
   * @return java.lang.String
   * @author MaoJiaXing
   * @since 2020-04-13 14:55
   */
  public String getHeader(String string) {
    return getRequest().getHeader(string);
  }
}

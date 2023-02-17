package com.github.maojx0630.mahjong.common.mybatis;

import cn.hutool.core.util.StrUtil;
import com.github.maojx0630.mahjong.common.GlobalStatic;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Objects;

/**
 * 从request参数中获取page对象
 *
 * @see Page 用来快速构造次对象 方便分页
 * @author MaoJiaXing
 * @since 2019-07-25 15:45
 */
@SuppressWarnings("all")
public class PageUtils {

  public static <T> Page<T> empty() {
    return empty(null);
  }

  public static <T> Page<T> empty(T t) {
    Page<T> page = new Page<T>(1L, -1L, t);
    page.setRecords(Collections.emptyList());
    page.setTotal(0);
    return page;
  }

  /**
   * 不分页构造
   *
   * @return com.zfei.rehabilitation.common.mybatis.page.Page<T>
   * @author 毛家兴
   * @since 2021/10/15 15:22
   */
  public static <T> Page<T> noPaging() {
    return noPaging(null);
  }

  /**
   * 不分页构造
   *
   * @param t 参数
   * @return com.zfei.rehabilitation.common.mybatis.page.Page<T>
   * @author 毛家兴
   * @since 2021/10/15 15:23
   */
  public static <T> Page<T> noPaging(T t) {
    return get(-1L, t);
  }

  /**
   * 构造分页对象
   *
   * @return com.zfei.rehabilitation.common.mybatis.page.Page<T>
   * @author 毛家兴
   * @since 2021/10/15 15:23
   */
  public static <T> Page<T> get() {
    return get(null);
  }

  /**
   * 快速构造分页对象
   *
   * @param t 请求参数
   * @return com.zfei.rehabilitation.common.mybatis.page.Page<T>
   * @author 毛家兴
   * @since 2021/10/15 15:23
   */
  public static <T> Page<T> get(T t) {
    return get(null, t);
  }

  /**
   * 快速构造分页对象
   *
   * @param sizeParam 指定分页长度
   * @param t 请求参数
   * @return com.zfei.rehabilitation.common.mybatis.page.Page<T>
   * @author 毛家兴
   * @since 2021/10/15 15:23
   */
  public static <T> Page<T> get(Long sizeParam, T t) {
    return get(GlobalStatic.DEFAULT_CURRENT_NAME, GlobalStatic.DEFAULT_SIZE_NAME, sizeParam, t);
  }

  /**
   * 快速构造分页对象
   *
   * @param currentName 分页参数名称
   * @param sizeName 步长分页名称
   * @param t 请求参数
   * @return com.zfei.rehabilitation.common.mybatis.page.Page<T>
   * @author 毛家兴
   * @since 2021/10/15 15:23
   */
  public static <T> Page<T> get(String currentName, String sizeName, T t) {
    return get(currentName, sizeName, null, t);
  }

  /**
   * 快速构造分页对象
   *
   * @param currentName 分页参数名称
   * @param sizeName 步长分页名称
   * @param sizeParam 指定分页长度
   * @param t 请求参数
   * @return com.zfei.rehabilitation.common.mybatis.page.Page<T>
   * @author 毛家兴
   * @since 2021/10/15 15:23
   */
  public static <T> Page<T> get(String currentName, String sizeName, Long sizeParam, T t) {

    ServletRequestAttributes servletRequestAttributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    HttpServletRequest request = servletRequestAttributes.getRequest();
    long current = getPageParam(request, currentName, GlobalStatic.DEFAULT_CURRENT);
    if (Objects.nonNull(sizeParam)) {
      return create(current, sizeParam, t);
    }
    long size = getPageParam(request, sizeName, GlobalStatic.DEFAULT_SIZE);
    return create(current, size, t);
  }

  /**
   * 快速构造分页对象
   *
   * @param current 页码
   * @return com.zfei.rehabilitation.common.mybatis.page.Page<T>
   * @author 毛家兴
   * @since 2021/10/15 15:24
   */
  public static <T> Page<T> create(Long current) {
    return create(current, GlobalStatic.DEFAULT_SIZE, null);
  }

  /**
   * 快速构造分页对象
   *
   * @param current 页码
   * @param size 页长
   * @return com.zfei.rehabilitation.common.mybatis.page.Page<T>
   * @author 毛家兴
   * @since 2021/10/15 15:24
   */
  public static <T> Page<T> create(Long current, Long size) {
    return create(current, size, null);
  }

  /**
   * 快速构造分页对象
   *
   * @param current 页码
   * @param t 参数
   * @return com.zfei.rehabilitation.common.mybatis.page.Page<T>
   * @author 毛家兴
   * @since 2021/10/15 15:24
   */
  public static <T> Page<T> create(Long current, T t) {
    return create(current, GlobalStatic.DEFAULT_SIZE, t);
  }

  /**
   * 快速构造分页对象
   *
   * @param current 页码
   * @param size 页长
   * @param t 参数
   * @return com.zfei.rehabilitation.common.mybatis.page.Page<T>
   * @author 毛家兴
   * @since 2021/10/15 15:24
   */
  public static <T> Page<T> create(Long current, Long size, T t) {
    if (current <= 0) {
      current = 1L;
    }
    if (size < 0) {
      size = -1L;
    } else {
      if (size < GlobalStatic.MIN_PAGE_SIZE) {
        size = GlobalStatic.MIN_PAGE_SIZE;
      }
      if (size > GlobalStatic.MAX_PAGE_SIZE) {
        size = GlobalStatic.MAX_PAGE_SIZE;
      }
    }
    return new Page<T>(current, size, t);
  }

  private static long getPageParam(
      HttpServletRequest request, String paramName, long defaultValue) {
    String value = request.getParameter(paramName);
    if (StrUtil.isNotBlank(value)) {
      try {
        return Long.valueOf(value);
      } catch (Exception e) {
        return defaultValue;
      }
    } else {
      return defaultValue;
    }
  }
}

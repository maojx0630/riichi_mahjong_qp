package com.github.maojx0630.mahjong.common.util;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.github.maojx0630.mahjong.common.mybatis.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * spring工具类
 *
 * @author MaoJiaXing
 * @since 2020-04-13 14:39
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SpringUtils implements ApplicationContextAware {

  private static ApplicationContext CONTEXT;

  private static Environment ENVIRONMENT;

  private static String BASE_PACKAGE;

  /**
   * 复制bean
   *
   * @param k 原始数据
   * @return V 复制后对象
   * @author MaoJiaXing
   * @since 2019-07-22 19:39
   */
  public static <K, V> V copy(K k, Class<V> aClass) {
    if (Objects.isNull(k)) {
      return ReflectUtil.newInstanceIfPossible(aClass);
    }
    return CglibUtil.copy(k, aClass);
  }

  /**
   * copy集合
   *
   * @param kList 被复制的集合
   * @param aClass 要转换的类
   * @return java.util.List<V>
   * @author MaoJiaXing
   * @since 2020-12-08 14:36
   */
  public static <K, V> List<V> copy(List<K> kList, Class<V> aClass) {
    List<V> list = new ArrayList<>();
    if (kList == null || kList.isEmpty()) {
      return list;
    }
    for (K k : kList) {
      list.add(copy(k, aClass));
    }
    return list;
  }

  /**
   * copy page
   *
   * @param page 被转换类的page对象
   * @param aClass 转换成的类
   * @return java.util.List<V>
   * @author MaoJiaXing
   * @since 2020-12-08 14:37
   */
  public static <K, V> Page<V> copy(Page<K> page, Class<V> aClass) {
    List<V> copy = copy(page.getRecords(), aClass);
    Page<V> vPage = new Page<>();
    vPage.setRecords(copy);
    vPage.setTotal(page.getTotal());
    vPage.setSize(page.getSize());
    vPage.setCurrent(page.getCurrent());
    return vPage;
  }

  /**
   * 获取bean
   *
   * @param clazz 类型
   * @return T
   * @author MaoJiaXing
   * @since 2020-04-13 14:55
   */
  public static <T> T getBean(Class<T> clazz) {
    return CONTEXT.getBean(clazz);
  }

  /**
   * 获取bean
   *
   * @param clazz 类型
   * @return T
   * @author MaoJiaXing
   * @since 2020-04-13 14:55
   */
  public static <T> Optional<T> getOptBean(Class<T> clazz) {
    try {
      return Optional.of(CONTEXT.getBean(clazz));
    } catch (NoSuchBeanDefinitionException e) {
      return Optional.empty();
    }
  }

  /**
   * 获取指定类型的集合
   *
   * @param clazz 类型
   * @return java.util.List<T>
   * @author MaoJiaXing
   * @since 2020-12-25 15:52
   */
  public static <T> List<T> getBeanList(Class<T> clazz) {
    Map<String, T> map = CONTEXT.getBeansOfType(clazz);
    if (map.isEmpty()) {
      return Collections.emptyList();
    } else {
      List<T> list = new LinkedList<>();
      map.forEach((str, t) -> list.add(t));
      return list;
    }
  }

  public static Collection<Object> getBeansWithAnnotation(
      Class<? extends Annotation> annotationType) {
    return CONTEXT.getBeansWithAnnotation(annotationType).values();
  }

  public static String getProperty(String key) {
    return ENVIRONMENT.getProperty(key);
  }

  public static String getProperty(String key, String defaultValue) {
    return ENVIRONMENT.getProperty(key, defaultValue);
  }

  // 获取启动类包名
  public static String getBasePackage() {
    return BASE_PACKAGE;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    CONTEXT = applicationContext;
    ENVIRONMENT = applicationContext.getEnvironment();
    log.info("ApplicationContext 已经成功注入！");

    String[] beanNamesForAnnotation =
        CONTEXT.getBeanNamesForAnnotation(SpringBootApplication.class);
    if (beanNamesForAnnotation.length != 1) {
      throw new RuntimeException("启动类不能为多个");
    }
    Object bean = CONTEXT.getBean(beanNamesForAnnotation[0]);
    BASE_PACKAGE = bean.getClass().getPackage().getName();
  }
}

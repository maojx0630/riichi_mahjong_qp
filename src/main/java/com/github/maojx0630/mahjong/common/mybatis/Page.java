package com.github.maojx0630.mahjong.common.mybatis;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.maojx0630.mahjong.common.GlobalStatic;

import java.util.List;

/**
 * @see IPage 对象返回时的二次封装
 * @author MaoJiaXing
 * @since 2019-07-26 12:09
 */
public class Page<T> implements IPage<T> {

  private Long current;

  private Long size;

  private Long total;

  private Long pages;

  private boolean last;

  private boolean first;

  private List<T> records;
  // 仅用于忽略返回值无实际作用
  @JsonIgnore private transient T param;

  // 仅用于忽略返回值无实际作用
  @JsonIgnore private transient boolean hitCount;

  // 仅用于忽略返回值无实际作用
  @JsonIgnore private transient boolean searchCount;

  // 是否优化countSql
  @JsonIgnore private transient boolean optimizeCountSql = true;

  public Page() {}

  public Page(Long current, Long size, T t) {
    if (size == -1L) {
      this.total = -1L;
      this.current = 1L;
    } else {
      this.current = current;
    }
    this.size = size;
    this.param = t;
  }

  @Override
  public List<OrderItem> orders() {
    return null;
  }

  @Override
  public List<T> getRecords() {
    return records;
  }

  @Override
  public IPage<T> setRecords(List<T> records) {
    this.records = records;
    return this;
  }

  @Override
  public long getTotal() {
    return total;
  }

  @Override
  public IPage<T> setTotal(long total) {
    this.total = total;
    return this;
  }

  @Override
  public long getSize() {
    return size;
  }

  @Override
  public IPage<T> setSize(long size) {
    this.size = size;
    return this;
  }

  @Override
  public long getCurrent() {
    return current;
  }

  @Override
  public IPage<T> setCurrent(long current) {
    this.current = current;
    return this;
  }

  @Override
  public long getPages() {
    if (getSize() == 0) {
      return 0L;
    }
    long pages = getTotal() / getSize();
    if (getTotal() % getSize() != 0) {
      pages++;
    }
    return pages;
  }

  public boolean isLast() {
    return current == getPages();
  }

  public boolean isFirst() {
    return current == GlobalStatic.DEFAULT_CURRENT;
  }

  public T getParam() {
    return param;
  }

  public void setParam(T param) {
    this.param = param;
  }

  /**
   * 禁用分页优化
   *
   * @return com.zfei.rehabilitation.common.mybatis.page.Page<?>
   * @author 毛家兴
   * @since 2021/11/18 14:23
   */
  public Page<T> disableOptimizeCountSql() {
    this.optimizeCountSql = false;
    return this;
  }

  @Override
  public boolean optimizeCountSql() {
    return this.optimizeCountSql;
  }
}

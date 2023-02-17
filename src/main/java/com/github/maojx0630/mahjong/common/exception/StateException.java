package com.github.maojx0630.mahjong.common.exception;

import com.github.maojx0630.mahjong.common.result.ResponseResultState;

/**
 * 自定义异常
 *
 * @author MaoJiaXing
 * @since 2019-07-21 18:56
 */
public class StateException extends RuntimeException implements ResponseResultState {

  private final Integer state;

  private String msg;

  private Object data;

  public StateException(ResponseResultState bodyState) {
    this.state = bodyState.getState();
    this.msg = bodyState.getMsg();
    this.data = bodyState.getData();
  }

  @Override
  public Integer getState() {
    return state;
  }

  @Override
  public String getMsg() {
    return msg;
  }

  @Override
  public Object getData() {
    return data;
  }

  public StateException setData(Object data) {
    this.data = data;
    return this;
  }

  public StateException append(String msg) {
    this.msg = this.msg + msg;
    return this;
  }

  public StateException insert(String msg) {
    this.msg = msg + this.msg;
    return this;
  }

  @Override
  public String getMessage() {
    return this.msg;
  }
}

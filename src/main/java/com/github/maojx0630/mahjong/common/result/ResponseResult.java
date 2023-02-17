package com.github.maojx0630.mahjong.common.result;

import com.alibaba.fastjson.JSON;
import com.github.maojx0630.mahjong.common.exception.StateEnum;

import java.util.Objects;

/**
 * 统一返回值对象
 *
 * @author MaoJiaXing
 */
public class ResponseResult implements ResponseResultState {

  private Integer state;

  private String msg;

  private Object data;

  public static ResponseResult of(Object data) {
    return of(StateEnum.success, data);
  }

  public static ResponseResult of(ResponseResultState bodyState) {
    return of(bodyState, bodyState.getData());
  }

  public static ResponseResult of(ResponseResultState bodyState, Object data) {
    return new ResponseResult(bodyState, data);
  }

  public ResponseResult appendMsg(String msg) {
    this.msg = this.msg + msg;
    return this;
  }

  /**
   * 向后追加信息
   *
   * @param msg 信息
   * @return com.zfei.rehabilitation.common.config.response.result.ResponseResult
   * @author MaoJiaXing
   * @since 2020-12-07 14:06
   */
  public ResponseResult append(String msg) {
    this.msg = this.msg + msg;
    return this;
  }

  /**
   * 向前插入信息
   *
   * @param msg 信息
   * @return com.zfei.rehabilitation.common.config.response.result.ResponseResult
   * @author MaoJiaXing
   * @since 2020-12-07 14:07
   */
  public ResponseResult insert(String msg) {
    this.msg = msg + this.msg;
    return this;
  }

  public boolean success() {
    return Objects.equals(StateEnum.success.getState(), this.state);
  }

  public boolean failure() {
    return !success();
  }

  public boolean hasValid() {
    return Objects.equals(StateEnum.valid_error.getState(), this.state);
  }

  private ResponseResult(ResponseResultState bodyState, Object data) {
    this.state = bodyState.getState();
    this.msg = bodyState.getMsg();
    this.data = data;
  }

  @Override
  public Integer getState() {
    return state;
  }

  public ResponseResult setState(Integer state) {
    this.state = state;
    return this;
  }

  @Override
  public String getMsg() {
    return msg;
  }

  public ResponseResult setMsg(String msg) {
    this.msg = msg;
    return this;
  }

  @Override
  public Object getData() {
    return data;
  }

  public ResponseResult setData(Object data) {
    this.data = data;
    return this;
  }

  public String toJson() {
    return JSON.toJSONString(this);
  }
}

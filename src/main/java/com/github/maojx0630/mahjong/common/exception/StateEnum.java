package com.github.maojx0630.mahjong.common.exception;

import com.github.maojx0630.mahjong.common.result.ResponseResult;
import com.github.maojx0630.mahjong.common.result.ResponseResultState;

import java.util.function.Supplier;

/**
 * 错误码
 *
 * @author MaoJiaXing
 * @since 2019-07-21 18:55
 */
public enum StateEnum implements ResponseResultState, Supplier<StateException> {

  /** 成功 */
  success(200, "OK"),
  /** 无数据返回 */
  no_data(300, "no data"),
  /** 参数不正确 */
  invalid_format_exception(400, "参数无法解析"),
  /** 访问的接口不存在 */
  no_found(404, "访问的接口不存在 "),
  /** 发生了意料之外的异常 */
  error(500, "发生了未知异常，请您稍后再试！"),
  file_download_error(600, "文件下载失败"),
  user_not_login(700, "用户未登录"),
  // 1000业务相关
  not_user(1000, "未绑定用户"),
  ranking_not_correct(1001, "传入的排名不正确"),
  user_number_error(1002, "选手数量不正确"),
  // 2000操作记录相关
  /** 操作失败 */
  operation_failure(2000, ""),
  /** 修改失败 */
  update_error(2001, "修改失败"),
  /** 添加失败 */
  add_error(2002, "添加失败"),
  /** 删除失败 */
  delete_error(2003, "删除失败"),
  // 3000参数及类型相关
  /** 必须拥有默认构造方法 */
  default_structure(3001, "必须拥有默认构造方法"),
  /** 参数校验异常 */
  valid_error(3002, "参数校验失败"),
  /** 参数已存在 */
  param_exist(3003, "已存在"),
  /** 参数不存在 */
  param_nonentity(3004, "不存在"),
  /** 无对应数据 */
  no_relevant_data(3005, "无相关数据"),
  please_use_correct_timestamp(3006, "请使用正确时间戳"),
  ;

  private Integer state;

  private String msg;

  StateEnum(Integer state, String msg) {
    this.state = state;
    this.msg = msg;
  }

  @Override
  public Integer getState() {
    return state;
  }

  @Override
  public String getMsg() {
    return msg;
  }

  public StateException create() {
    return create(null);
  }

  public StateException create(Object msg) {
    return new StateException(this).setData(msg);
  }

  @Override
  public StateException get() {
    return create();
  }

  public ResponseResult build() {
    return ResponseResult.of(this);
  }
}

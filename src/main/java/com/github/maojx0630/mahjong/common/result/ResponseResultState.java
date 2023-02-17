package com.github.maojx0630.mahjong.common.result;

/**
 * @author MaoJiaXing
 */
public interface ResponseResultState {

  /**
   * 获取状态值
   *
   * @return java.lang.String
   * @author MaoJiaXing
   */
  Integer getState();

  /**
   * 获取返回信息
   *
   * @return java.lang.String
   * @author MaoJiaXing
   */
  String getMsg();

  default Object getData() {
    return null;
  }
}

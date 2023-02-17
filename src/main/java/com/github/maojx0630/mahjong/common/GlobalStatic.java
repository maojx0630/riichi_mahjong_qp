package com.github.maojx0630.mahjong.common;

import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 全局静态配置
 *
 * @author MaoJiaXing
 * @since 2019-07-21 18:02
 */
@UtilityClass
public class GlobalStatic {

  public static final Charset UTF8 = StandardCharsets.UTF_8;

  public static final String UTF_8 = "utf-8";

  public static final String PHONE_REGEX = "^1[\\d]{10}$";

  // 密码的组成至少要包括大小写字母、数字及标点符号的其中两项,并且8-16位
  public static final String PASSWORD_REGEX = "^(?![A-Za-z]+$)(?!\\d+$)(?![\\W_]+$)\\S{8,16}$";

  // 用户名的组成至少要包括大小写字母、数字的其中两项,并且6-16位
  public static final String USERNAME_REGEX = "^(?![A-Za-z]+$)(?![\\W_]+$)\\S{6,20}$";

  // ipv4 正则校验
  public static final String IP_REGEX =
      "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
          + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
          + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";

  // 正整数校验
  public static final String POSITIVE_INTEGER_REGEX = "^[1-9]\\d*$";

  // 树顶级id
  public static final Long TREE_ROOT = 0L;

  // 拼接符号
  public static final String JOIN_STR = ",";

  // 连字符(减号)-拼接符
  public static final String HYPHEN_JOIN_STR = "-";

  // 特殊拼接符号,用来防止,可能会出现的状况
  public static final String SPECIAL_JOIN_STR = "@@";

  public static final String USER_AUTH = "auth";

  // 默认页码
  public static final long DEFAULT_CURRENT = 1;

  // 默认页码参数名
  public static final String DEFAULT_CURRENT_NAME = "current";

  // 默认页长
  public static final long DEFAULT_SIZE = 20;
  // 最小页长
  public static final long MIN_PAGE_SIZE = 10;
  // 最大页长
  public static final long MAX_PAGE_SIZE = 100;

  // 默认页长参数名
  public static final String DEFAULT_SIZE_NAME = "size";

  // 绑定参数异常信息最大长度
  public static final int BINDING_MSG_MAX_LENGTH = 30;

  // 绑定参数异常信息最大长度超过后显示的信息
  public static final String BINDING_MSG_MAX_LENGTH_MSG = "参数不正确,请确认";

  // 单个ip 或登录用户 每秒最多可以请求接口次数 小于0为不限制
  // (登录用户与ip分离计算如果登录了则以登录用户频次计算否则按照ip计算)
  public static final Double PERMITS_PER_SECONDS = 0D;

  // 单个ip 或登录用户 每分钟最多可以请求接口次数 小于0为不限制
  public static final Double PERMITS_PER_MINUTES = 0D;

  public static final String FILE_NAME="日麻数据.xlsx";

  public static final List<String> LETTER_LIST =
      StrUtil.split("A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z", ",");
}

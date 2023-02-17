package com.github.maojx0630.mahjong.utils;

import cn.hutool.extra.pinyin.PinyinUtil;
import com.github.maojx0630.mahjong.common.GlobalStatic;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LetterUtil {

  public String getFirst(String str) {
    String firstLetter = PinyinUtil.getFirstLetter(str, "");
    String first = String.valueOf(firstLetter.toCharArray()[0]).toUpperCase();
    if (GlobalStatic.LETTER_LIST.contains(first)) {
      return first;
    } else {
      return "#";
    }
  }

  public String getAll(String str) {
    StringBuilder sb = new StringBuilder();
    String firstLetter = PinyinUtil.getFirstLetter(str, "");
    for (char c : firstLetter.toCharArray()) {
      String strChar = String.valueOf(c).toUpperCase();
      if (GlobalStatic.LETTER_LIST.contains(strChar)) {
        sb.append(strChar);
      }
    }
    return sb.toString();
  }
}

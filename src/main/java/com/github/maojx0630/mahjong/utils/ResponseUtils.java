package com.github.maojx0630.mahjong.utils;

import com.alibaba.fastjson.JSON;
import com.github.maojx0630.mahjong.common.GlobalStatic;
import com.github.maojx0630.mahjong.common.exception.StateEnum;
import com.github.maojx0630.mahjong.common.result.ResponseResultState;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 请求返回工具类
 *
 * @author 毛家兴
 * @since 2021-07-26 10:44
 */
@Slf4j
@UtilityClass
public class ResponseUtils {

  private final String DOWNLOAD_TYPE = "application/x-download";

  /**
   * 输出json字符串
   *
   * @param state 要输出对象
   */
  public void response(HttpServletResponse response, ResponseResultState state) {
    response(response, JSON.toJSONString(state));
  }

  public void response(HttpServletResponse response, String data) {
    response.setHeader("Content-type", "application/Json;charset=UTF-8");
    try {
      response.getOutputStream().write(data.getBytes(StandardCharsets.UTF_8));
    } catch (Exception ignored) {
    }
  }

  /**
   * 下载文件
   *
   * @param file 文件
   * @param fileName 文件名
   */
  public void write(HttpServletResponse response, File file, String fileName) {
    write(response, file, fileName, DOWNLOAD_TYPE);
  }

  /**
   * 指定 contentType导出文件
   *
   * @param file 文件
   * @param fileName 文件名
   * @param contentType 网络类型
   */
  public void write(HttpServletResponse response, File file, String fileName, String contentType) {

    try (FileInputStream in = new FileInputStream(file);
        OutputStream out = response.getOutputStream()) {
      // 创建缓冲区
      byte[] buffer = new byte[1024];
      int len;
      // 循环将输入流中的内容读取到缓冲区当中
      while ((len = in.read(buffer)) > 0) {
        // 输出缓冲区的内容到浏览器，实现文件下载
        out.write(buffer, 0, len);
      }
      setFileName(response, fileName);
      response.setHeader("Content-Length", file.length() + "");
      response.setContentType(contentType);
    } catch (Exception e) {
      log.error("下载文件异常", e);
      throw StateEnum.file_download_error.create();
    }
  }

  public void writeByte(HttpServletResponse response, byte[] bytes) {
    try (ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        OutputStream out = response.getOutputStream()) {
      // 创建缓冲区
      byte[] buffer = new byte[1024];
      int len;
      // 循环将输入流中的内容读取到缓冲区当中
      while ((len = in.read(buffer)) > 0) {
        // 输出缓冲区的内容到浏览器，实现文件下载
        out.write(buffer, 0, len);
      }
      response.setHeader("Content-Length", bytes.length + "");
      response.setContentType(DOWNLOAD_TYPE);
    } catch (Exception e) {
      log.error("下载文件异常", e);
      throw StateEnum.file_download_error.create();
    }
  }

  private void setFileName(HttpServletResponse response, String fileName) {
    try {
      response.setCharacterEncoding(GlobalStatic.UTF_8);
      response.setHeader(
          "Content-Disposition",
          "attachment;fileName=" + URLEncoder.encode(fileName, GlobalStatic.UTF_8));
    } catch (UnsupportedEncodingException e) {
      throw StateEnum.file_download_error.create(e.getMessage());
    }
  }
}

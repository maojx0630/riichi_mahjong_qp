package com.github.maojx0630.mahjong.utils;

import lombok.experimental.UtilityClass;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 * okhttp请求封装
 *
 * @author MaoJiaXing
 * @since 2020-12-03 15:41
 */
@UtilityClass
public class OkHttpUtils {

  /**
   * 构造http请求 okhttp
   *
   * @return okhttp3.OkHttpClient
   * @author MaoJiaXing
   * @since 2020-12-14 14:28
   */
  public OkHttpClient getClient() {
    return getClient(new OkHttpClient().newBuilder());
  }

  /**
   * 构造http请求 okhttp
   *
   * @param builder 自定义部分参数
   * @return okhttp3.OkHttpClient
   * @author MaoJiaXing
   * @since 2020-12-14 14:29
   */
  public OkHttpClient getClient(OkHttpClient.Builder builder) {
    return builder.sslSocketFactory(getSSLSocketFactory(), getX509TrustManager()).build();
  }

  /**
   * 直接获得call 发送普通无自定义参数https请求
   *
   * @param request 请求参数
   * @return okhttp3.Call
   * @author MaoJiaXing
   * @since 2020-12-14 14:29
   */
  public Call getCall(Request request) {
    return getClient().newCall(request);
  }

  private SSLSocketFactory getSSLSocketFactory() {
    try {
      SSLContext sslContext = SSLContext.getInstance("SSL");
      sslContext.init(null, getTrustManager(), new SecureRandom());
      return sslContext.getSocketFactory();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // 获取TrustManager
  private TrustManager[] getTrustManager() {
    return new TrustManager[] {getX509TrustManager()};
  }

  private X509TrustManager getX509TrustManager() {
    return new X509TrustManager() {
      @Override
      public void checkClientTrusted(X509Certificate[] chain, String authType) {}

      @Override
      public void checkServerTrusted(X509Certificate[] chain, String authType) {}

      @Override
      public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[] {};
      }
    };
  }
}

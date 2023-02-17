package com.github.maojx0630.mahjong.common.advice;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.maojx0630.mahjong.common.exception.StateEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 统一返回配置
 *
 * @author MaoJiaXing
 * @since 2019-07-21 18:57
 */
@Configuration
public class ResponseConfiguration implements WebMvcConfigurer {

  @Bean
  public Converter<String, Date> dateConverter() {
    return new DateConverter();
  }

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    // 获取converter
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    // 设置long转string
    ObjectMapper objectMapper = converter.getObjectMapper();
    SimpleModule simpleModule = new SimpleModule();
    simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
    simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
    simpleModule.addDeserializer(
        Date.class,
        new JsonDeserializer<Date>() {
          @Override
          public Date deserialize(JsonParser p, DeserializationContext deserializationContext) {
            try {
              String value = p.getValueAsString();
              if (StrUtil.isNotBlank(value)) {
                return new Date(Long.parseLong(value));
              } else {
                return null;
              }
            } catch (Exception e) {
              throw StateEnum.please_use_correct_timestamp.create();
            }
          }
        });
    objectMapper.registerModule(simpleModule);
    converter.setObjectMapper(objectMapper);
    // 设置long转string 结束
    // 转换类型设置test
    List<MediaType> supportedMediaTypes = new ArrayList<>();
    supportedMediaTypes.add(MediaType.APPLICATION_JSON);
    supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
    supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
    supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
    supportedMediaTypes.add(MediaType.APPLICATION_PDF);
    supportedMediaTypes.add(MediaType.APPLICATION_RSS_XML);
    supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
    supportedMediaTypes.add(MediaType.APPLICATION_XML);
    supportedMediaTypes.add(MediaType.IMAGE_GIF);
    supportedMediaTypes.add(MediaType.IMAGE_JPEG);
    supportedMediaTypes.add(MediaType.IMAGE_PNG);
    supportedMediaTypes.add(MediaType.TEXT_EVENT_STREAM);
    supportedMediaTypes.add(MediaType.TEXT_HTML);
    supportedMediaTypes.add(MediaType.TEXT_MARKDOWN);
    supportedMediaTypes.add(MediaType.TEXT_PLAIN);
    supportedMediaTypes.add(MediaType.TEXT_XML);
    converter.setSupportedMediaTypes(supportedMediaTypes);
    converters.clear();
    converters.add(converter);
  }
}

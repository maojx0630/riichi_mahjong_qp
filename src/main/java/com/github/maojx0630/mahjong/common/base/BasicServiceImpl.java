package com.github.maojx0630.mahjong.common.base;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

public class BasicServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

  public LambdaQueryWrapper<T> wrapper() {
    return Wrappers.lambdaQuery();
  }

  public LambdaUpdateWrapper<T> updateWrapper() {
    return Wrappers.lambdaUpdate();
  }
}

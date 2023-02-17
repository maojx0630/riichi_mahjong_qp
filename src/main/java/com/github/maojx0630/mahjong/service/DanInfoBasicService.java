package com.github.maojx0630.mahjong.service;

import com.github.maojx0630.mahjong.common.base.BasicServiceImpl;
import com.github.maojx0630.mahjong.mapper.DanInfoMapper;
import com.github.maojx0630.mahjong.model.DanInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 毛家兴
 * @since 2022/2/21 16:41
 */
@Service
@Transactional(readOnly = true)
public class DanInfoBasicService extends BasicServiceImpl<DanInfoMapper, DanInfo> {}

package com.github.maojx0630.mahjong.service;

import com.github.maojx0630.mahjong.common.base.BasicServiceImpl;
import com.github.maojx0630.mahjong.mapper.DanTitleMapper;
import com.github.maojx0630.mahjong.model.DanTitle;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 毛家兴
 * @since 2022/2/24 15:57
 */
@Service
@Transactional(readOnly = true)
public class DanTitleBasicService extends BasicServiceImpl<DanTitleMapper, DanTitle> {}

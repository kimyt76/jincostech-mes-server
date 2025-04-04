package com.daehanins.mes.biz.tag.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.tag.entity.TagInfo;
import com.daehanins.mes.biz.tag.mapper.TagInfoMapper;
import com.daehanins.mes.biz.tag.service.ITagInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * TagInfo 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2021-04-08
 */
@Service
public class TagInfoServiceImpl extends ServiceImpl<TagInfoMapper, TagInfo> implements ITagInfoService {

}

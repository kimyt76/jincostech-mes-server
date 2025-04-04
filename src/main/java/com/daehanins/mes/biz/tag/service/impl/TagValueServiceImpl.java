package com.daehanins.mes.biz.tag.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.tag.entity.TagValue;
import com.daehanins.mes.biz.tag.mapper.TagValueMapper;
import com.daehanins.mes.biz.tag.service.ITagValueService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * TagValue 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2021-04-08
 */
@Service
public class TagValueServiceImpl extends ServiceImpl<TagValueMapper, TagValue> implements ITagValueService {

}

package com.daehanins.mes.biz.adm.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import com.daehanins.mes.common.utils.SnowFlakeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 공통코드CommonCode 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("common_code")
public class CommonCode extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId
    private String codeId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    private String codeType;

    private String code;

    private String codeName;

    private String description;

    private Integer displayOrder;

    private String opt1;
    private String opt2;
    private String opt3;
    private String opt4;
    private String opt5;

    private String delYn;

}

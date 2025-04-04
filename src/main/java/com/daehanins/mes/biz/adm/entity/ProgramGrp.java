package com.daehanins.mes.biz.adm.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * ProgramGrp 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2022-10-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("program_grp")
public class ProgramGrp extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId
    private String programGrpCd;

    private String programGrpName;

    private Integer displayOrder;

    private String icon;

    private String path;


}

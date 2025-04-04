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
 * 공통코드유형CommonCodeType 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-10-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("common_code_type")
public class CommonCodeType extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId
    private String codeType;

    private String codeTypeName;

    private String description;

    private String userEditYn;

    private String opt1Name;

    private String opt1Type;

    private String opt2Name;

    private String opt2Type;

    private String opt3Name;

    private String opt3Type;

    private String opt4Name;

    private String opt4Type;

    private String opt5Name;

    private String opt5Type;
}

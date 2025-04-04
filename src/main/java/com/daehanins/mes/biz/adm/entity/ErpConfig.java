package com.daehanins.mes.biz.adm.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * ErpConfig 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-06-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("erp_config")
public class ErpConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * 회사코드
     */
    @TableId
    @TableField("com_code")
    private String comCode;

    /**
     * 연계사용자id
     */
    @TableField("user_id")
    private String userId;

    /**
     * API연계키
     */
    @TableField("api_cert_key")
    private String apiCertKey;

    /**
     * 언어타입
     */
    @TableField("lan_type")
    private String lanType;

    /**
     * ZONE코드
     */
    @TableField("zone")
    private String zone;

}

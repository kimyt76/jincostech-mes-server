package com.daehanins.mes.biz.adm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.daehanins.mes.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
* <p>
* SysLogData 엔티티
* </p>
*
* @author jeonsj
* @since 2022-10-01
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_log_data")
public class SysLogData extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "log_data_id", type = IdType.AUTO)
    private Long logDataId;

    private String logDt;

    private String useGb;

    private String sysUser;

    private String conectIp;

    private Integer dataUsgQty;

    public String getXmlData(String certKey) {

        StringBuffer sb = new StringBuffer("<logData>");
        sb.append("<crtfcKey>").append(certKey).append("</crtfcKey>");
        sb.append("<logDt>").append(logDt).append("</logDt>");
        sb.append("<useSe>").append(useGb).append("</useSe>");
        sb.append("<sysUser>").append(sysUser).append("</sysUser>");
        sb.append("<conectIp>").append(conectIp).append("</conectIp>");
        sb.append("<dataUsgqty>").append(dataUsgQty).append("</dataUsgqty>");
        sb.append("</logData>");

        return sb.toString();
    }

}

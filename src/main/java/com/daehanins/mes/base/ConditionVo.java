package com.daehanins.mes.base;

import lombok.Data;

import java.io.Serializable;

@Data
public  class  ConditionVo  implements Serializable {
    private static final long serialVersionUID = -5421678511367889748L;
    // 컬럼명
    private String column;
    //값
    private String value;
    // 조건형식 : eq, ne, like, gt, lt, ge, le ....
    private String type;
}

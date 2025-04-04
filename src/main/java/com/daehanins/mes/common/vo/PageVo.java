package com.daehanins.mes.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jeonsj
 */
@Data
public class PageVo implements Serializable{

    private static final long serialVersionUID = 1L;

    private int currentPage;

    private int pageSize;

    private String sortColumn;

    private boolean orderAsc;

}

package com.daehanins.mes.biz.qt.vo;

import com.daehanins.mes.biz.qt.entity.TestTypeMethod;
import com.daehanins.mes.biz.qt.entity.TestTypeView;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TestTypeReadWithItems {
    private TestTypeView testType;

    private List<TestTypeMethod> testTypeMethods = new ArrayList<>();

}

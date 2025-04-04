package com.daehanins.mes.biz.qt.vo;

import com.daehanins.mes.biz.qt.entity.TestType;
import com.daehanins.mes.biz.qt.entity.TestTypeMethod;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TestTypeSaveWithItems {

    private TestType testType;

    private List<TestTypeMethod> testTypeMethods = new ArrayList<>();

    private List<TestTypeMethod> deleteTestTypeMethods = new ArrayList<>();

}

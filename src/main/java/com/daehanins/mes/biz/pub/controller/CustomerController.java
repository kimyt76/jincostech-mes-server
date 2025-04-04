package com.daehanins.mes.biz.pub.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daehanins.mes.common.utils.ExcelPoiUtil;
import com.daehanins.mes.common.utils.JasperUtil;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.utils.SearchUtil;
import com.daehanins.mes.common.vo.RestResponse;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.pub.service.ICustomerService;
import com.daehanins.mes.biz.pub.service.ICustomerViewService;
import com.daehanins.mes.biz.pub.entity.Customer;
import com.daehanins.mes.biz.pub.entity.CustomerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * Customer Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-01
 */
@RestController
@RequestMapping("/pub/customer")
public class CustomerController extends BaseController<Customer, CustomerView, String> {

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ICustomerViewService customerViewService;

    @Override
    public ICustomerService getTableService() {
        return this.customerService;
    }

    @Override
    public ICustomerViewService getViewService() {
        return this.customerViewService;
    }

    @RequestMapping(value = "/getPdf",method = RequestMethod.GET)
    public ResponseEntity<Resource> getPdf(@RequestParam (name="condition", required=false ) String conditionJson) throws Exception {

        QueryWrapper<CustomerView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        List<CustomerView> customerList = getViewService().list(queryWrapper);

        try {
            InputStream reportStream = getClass().getResourceAsStream("/report/customer_list.jrxml");
            byte[] pdfContent = JasperUtil.getPdfBinary(reportStream, customerList);
            ByteArrayResource resource = new ByteArrayResource(pdfContent);
            HttpHeaders header = JasperUtil.getHeader("customer_list", pdfContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("리포트 생성중 에러발생!");
        }
    }

    @RequestMapping(value = "/getExcel",method = RequestMethod.GET)
    public ResponseEntity<Resource> getExcel(@RequestParam (name="condition", required=false ) String conditionJson) throws Exception {

        QueryWrapper<CustomerView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        List<CustomerView> customerList = getViewService().list(queryWrapper.orderByAsc("customer_name"));
        try {
            // 엑셀양식 로드
            InputStream excelStream = getClass().getResourceAsStream("/excel/customer_erp_list.xls");
            Workbook workbook = ExcelPoiUtil.createHssfWorkbook(excelStream);
//            File file = ResourceUtils.getFile("classpath:excel/customer_erp_list.xls");
//            Workbook workbook = ExcelPoiUtil.createHssfWorkbook(new FileInputStream(file));
            // 양식의 시트 로드
            Sheet sheet = workbook.getSheet("거래처리스트");

            // 기본 변수 선언
            Row row = null;
            Cell cell = null;
            int rowNo = 2;     // 출력시작 Row,  POI row는 0부터 시작
            int lineNo = 1;    // 줄번호 표시용

            // 양식파일의 첫줄에 셀스타일 적용한 경우,  셀 스타일을 구함
            List<CellStyle> cellStyleList = ExcelPoiUtil.getRowCellStyle(sheet, rowNo, 16);
            for (CustomerView customer : customerList){
                // 셀스타일 적용시 getStyleCell , 미적용시 getCell 사용
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(customer.getCustomerCd());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 1, cellStyleList.get(1)).setCellValue(customer.getCustomerName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 2, cellStyleList.get(2)).setCellValue(customer.getPresident());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 3, cellStyleList.get(3)).setCellValue(customer.getTel());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 4, cellStyleList.get(4)).setCellValue(customer.getFax());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 5, cellStyleList.get(5)).setCellValue(customer.getEmail());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 6, cellStyleList.get(6)).setCellValue(customer.getPresidentTel());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 7, cellStyleList.get(7)).setCellValue(customer.getCustomerManager());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 8, cellStyleList.get(8)).setCellValue(customer.getCustomerManagerTel());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 9, cellStyleList.get(9)).setCellValue(customer.getAddress());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 10, cellStyleList.get(10)).setCellValue(customer.getMemberName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 11, cellStyleList.get(11)).setCellValue(customer.getSearchText());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 12, cellStyleList.get(12)).setCellValue(customer.getCustomerGrp1Name());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 13, cellStyleList.get(13)).setCellValue(customer.getCustomerGrp2Name());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 14, cellStyleList.get(14)).setCellValue("등록");
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 15, cellStyleList.get(15)).setCellValue(customer.getMemo());
                rowNo++;
            }
            // 끝라인에 다운로드 시간 표시
            SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy/MM/dd a hh:mm:ss");
            String saveTime = sdf.format (System.currentTimeMillis());
            ExcelPoiUtil.getCell(sheet, rowNo, 0).setCellValue(saveTime);

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("customer_erp_list", excelContent.length, "xls");

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }

    @RequestMapping(value = "/saveItems", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<List<Customer>> saveItems(@RequestBody List<Customer> customerList){

        List<Customer> data = getTableService().saveItems(customerList);

        return new RestUtil<List<Customer>>().setData(data);
    }

    @RequestMapping(value = "/getGroup",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<Customer>> getGroup(@RequestParam (name="condition", required=false ) String conditionJson){

       QueryWrapper<Customer> wrapper = new QueryWrapper<Customer>();
       wrapper.select("customer_manager")
               .groupBy("customer_manager");

        List<Map<String,Object>> mapList = getTableService().listMaps(wrapper);

        for (Map<String, Object> mp : mapList) {
            System.out.println(mp);
        }

        /**
         * lambdaQueryWrapper groupBy orderBy
         */
        LambdaQueryWrapper<Customer> lambdaQueryWrapper = new QueryWrapper<Customer>().lambda()
                .select(Customer::getCustomerManager)
                .groupBy(Customer::getCustomerManager)
                .orderByAsc(Customer::getCustomerManager);
        List<Customer> customerList = getTableService().list(lambdaQueryWrapper);
        for (Customer customer : customerList) {
            System.out.println(customer);
        }

        return new RestUtil<List<Customer>>().setData(customerList);
    }

}

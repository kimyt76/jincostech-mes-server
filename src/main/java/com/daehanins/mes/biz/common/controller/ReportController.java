package com.daehanins.mes.biz.common.controller;

import com.daehanins.mes.biz.common.vo.Barcode;
import com.daehanins.mes.biz.common.vo.LabelPrint;
import com.daehanins.mes.biz.common.vo.MailTestVo;
import com.daehanins.mes.biz.mat.vo.PurchaseOrderItem;
import com.daehanins.mes.biz.mat.vo.PurchaseOrderSheet;
import com.daehanins.mes.common.mail.MailManager;
import com.daehanins.mes.common.mail.MessageVariable;
import com.daehanins.mes.common.utils.JasperUtil;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.vo.RestResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * MatStock Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-13
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    MailManager mailManager;

    // PDF파일을 REST컨트롤러에서 출력하는 예제
    @RequestMapping(value = "/pdf/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> download(@PathVariable("fileName") String fileName) throws IOException {
        System.out.println("Calling Download:- " + fileName);
        ClassPathResource pdfFile = new ClassPathResource("report/word_label_01.pdf");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Content-Disposition", "filename=" + fileName);
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        headers.setContentLength(pdfFile.contentLength());
        ResponseEntity<InputStreamResource> response = new ResponseEntity<InputStreamResource>(
                new InputStreamResource(pdfFile.getInputStream()), headers, HttpStatus.OK);
        return response;
    }

    // jrxml로 PDF생성하는 처리
    @RequestMapping(value = "/jasper1", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> jasperTest1() throws IOException {
        String fileName = "test1";
        ClassPathResource pdfFile = new ClassPathResource("report/word_label_01.pdf");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Content-Disposition", "filename=" + fileName);
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        headers.setContentLength(pdfFile.contentLength());
        ResponseEntity<InputStreamResource> response = new ResponseEntity<InputStreamResource>(
                new InputStreamResource(pdfFile.getInputStream()), headers, HttpStatus.OK);
        return response;
    }

    @GetMapping(value = "/jasper2", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] exportReport(String reportFormat) throws FileNotFoundException, JRException {

        Barcode[] barcodeArray = {
                new Barcode("12345", "CAMPO SIDDHA NEER SIKKAPPU (Liquid water soluble)", "시험번호1", "사용기한", "테스트"),
                new Barcode("23456", "품목2", "시험번호2", "사용기한", "테스트"),
                new Barcode("34567", "품목3", "시험번호3", "사용기한", "테스트"),
                new Barcode("45678", "품목4", "시험번호4", "사용기한", "테스트"),
        };

        List<Barcode> barcodes = Arrays.asList(barcodeArray);
        //load file and compile it
        File file = ResourceUtils.getFile("classpath:report/barcode_01.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(barcodes);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("DataFile", "Java Techie");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    @RequestMapping(value = "/jasper3",method = RequestMethod.GET)
    public ResponseEntity<Resource> exportReport3(String reportFormat) throws Exception {

        LabelPrint[] labelArray = {
                new LabelPrint("JRFP000001", "CAMPO SIDDHA NEER SIKKAPPU (Liquid water soluble)", "실온",
                        "2006301001", "21D9721up2131-L21357", "2021-01-21", "400kg", "시험대기"),
                new LabelPrint("( JRFP000002 )", "CAMPO SIDDHA NEER TEST JDAHMVHJP34JFMCKFA LFDAJF", "실온",
                        "2006301002", "21D9721up2131-L21357", "2021-01-21", "3,000kg", "시험대기")
        };

        try {
            List<LabelPrint> labelPrintList = Arrays.asList(labelArray);

            byte[] pdfContent = JasperUtil.getPdfBinary("classpath:report/mat_label.jrxml", labelPrintList);
            ByteArrayResource resource = new ByteArrayResource(pdfContent);
            HttpHeaders header = JasperUtil.getHeader("poSheet_list", pdfContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("리포트에서 에러발생");
        }
    }

    @RequestMapping(value = "/getReportSheet",method = RequestMethod.GET)
    public ResponseEntity<Resource> getReportSheet() throws Exception {

        List<PurchaseOrderSheet> poSheetList = new ArrayList<>();
        for (int j = 0; j < 3; j++) {
            PurchaseOrderSheet poSheet = new PurchaseOrderSheet();
            poSheet.setMemberName("홍길동");
            poSheet.setOrderDate("2020/06/05");
            poSheet.setDeliveryDate("2020/06/07");
            poSheet.setEmail("test@jincostech.com");
            poSheet.setCustomerName("한국화장품");
            poSheet.setCustomerManager("김담당");
            poSheet.setTel("02-2345-6780");
            poSheet.setFax("02-2345-6781");
            poSheet.setAddress("경기도 시흥시 정왕동 111");
            poSheet.setHanAmount("금 삼천오백이십만원 정");
            poSheet.setNumAmount("(\\ 35,200,000 )");
            poSheet.setTotOrderQty(new BigDecimal(8));
            poSheet.setTotSupplyAmt(new BigDecimal(4000000));
            poSheet.setTotVat(new BigDecimal(400000));
            poSheet.setTotAmt(new BigDecimal(35200000));

            // subItems 만들기
            List<PurchaseOrderItem> poItemList = new ArrayList<>();
            // sub items
            for (int i = 0; i < 10; i++) {
                PurchaseOrderItem poItem = new PurchaseOrderItem();
                poItem.setItemCd("JFP0001");
                poItem.setItemName("테스트품목01테스트품목01테스트품목01테스트품목01");
                poItem.setSpec("테스트품목01");
                poItem.setOrderQty(new BigDecimal(8));
                poItem.setPrice(new BigDecimal(500000));
                poItem.setSupplyAmt(new BigDecimal(4000000));
                poItem.setVat(new BigDecimal(400000));
                poItem.setMemo(" ");
                poItemList.add(poItem);
            }

            poSheet.setOrderItems(poItemList);

            poSheetList.add(poSheet);
        }


        try {

            Map<String, Object> parameters = new HashMap<>();
            // 로고이미지
            File logoImage = ResourceUtils.getFile("classpath:static/images/logo1.png");
            parameters.put("logo", new FileInputStream(logoImage));

            // subReport 셋팅
            File file = ResourceUtils.getFile("classpath:report/purchase_order_sub.jrxml");
            JasperReport subReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            parameters.put("subReport", subReport);

            byte[] pdfContent = JasperUtil.getPdfBinary("classpath:report/purchase_order_sheet.jrxml", parameters, poSheetList);
            ByteArrayResource resource = new ByteArrayResource(pdfContent);
            HttpHeaders header = JasperUtil.getHeader("poSheet_list", pdfContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("리포트 생성중 에러발생!");
        }

    }

    @RequestMapping(value = "/getReportMail",method = RequestMethod.GET)
    public ResponseEntity<Resource> getReportMail() throws Exception {

        List<PurchaseOrderSheet> poSheetList = new ArrayList<>();
        for (int j = 0; j < 3; j++) {
            PurchaseOrderSheet poSheet = new PurchaseOrderSheet();
            poSheet.setMemberName("홍길동");
            poSheet.setOrderDate("2020/06/05");
            poSheet.setDeliveryDate("2020/06/07");
            poSheet.setEmail("test@jincostech.com");
            poSheet.setCustomerName("한국화장품");
            poSheet.setCustomerManager("김담당");
            poSheet.setTel("02-2345-6780");
            poSheet.setFax("02-2345-6781");
            poSheet.setAddress("경기도 시흥시 정왕동 111");
            poSheet.setHanAmount("금 삼천오백이십만원 정");
            poSheet.setNumAmount("(\\ 35,200,000 )");
            poSheet.setTotOrderQty(new BigDecimal(8));
            poSheet.setTotSupplyAmt(new BigDecimal(4000000));
            poSheet.setTotVat(new BigDecimal(400000));
            poSheet.setTotAmt(new BigDecimal(35200000));

            // subItems 만들기
            List<PurchaseOrderItem> poItemList = new ArrayList<>();
            // sub items
            for (int i = 0; i < 10; i++) {
                PurchaseOrderItem poItem = new PurchaseOrderItem();
                poItem.setItemCd("JFP0001");
                poItem.setItemName("테스트품목01테스트품목01테스트품목01테스트품목01");
                poItem.setSpec("테스트품목01");
                poItem.setOrderQty(new BigDecimal(8));
                poItem.setPrice(new BigDecimal(500000));
                poItem.setSupplyAmt(new BigDecimal(4000000));
                poItem.setVat(new BigDecimal(400000));
                poItem.setMemo(" ");
                poItemList.add(poItem);
            }

            poSheet.setOrderItems(poItemList);

            poSheetList.add(poSheet);
        }

        try {

            Map<String, Object> parameters = new HashMap<>();
            // 로고이미지
            File logoImage = ResourceUtils.getFile("classpath:static/images/logo1.png");
            parameters.put("logo", new FileInputStream(logoImage));

            // subReport 셋팅
            File file = ResourceUtils.getFile("classpath:report/purchase_order_sub.jrxml");
            JasperReport subReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            parameters.put("subReport", subReport);

            byte[] pdfContent = JasperUtil.getPdfBinary("classpath:report/purchase_order_sheet.jrxml", parameters, poSheetList);
            ByteArrayResource resource = new ByteArrayResource(pdfContent);
            HttpHeaders header = JasperUtil.getHeader("poSheet_list", pdfContent.length);

            // send(String emailAddress, String subject, String template, Map<String, InputStream > files, MessageVariable... variables) {
            ByteArrayDataSource bds = new ByteArrayDataSource(pdfContent, "application/pdf");
            Map<String, DataSource> files = new HashMap<>();
            files.put("발주서.pdf", bds);
            files.put("발주서2.pdf", bds);
            files.put("발주서3.pdf", bds);

            MailTestVo mailVo = new MailTestVo();
            mailVo.setUsername("전성종");
            mailVo.setMoney("100억원");
            mailVo.setHouse("한강이 조망되는 멋진 집");
            mailManager.send("jeonsj@daehanins.com", "tessan@naver.com", "발주서테스트입니다.", "test.ftl",  files, MessageVariable.from("vo", mailVo));


            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);


        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("리포트 생성중 에러발생!");
        }

    }

    @GetMapping(value = "/jasper4")
    public ResponseEntity<RestResponse> exportReport4(String reportFormat) throws Exception {

        Barcode[] barcodeArray = {
                new Barcode("12345", "CAMPO SIDDHA NEER SIKKAPPU (Liquid water soluble)", "시험번호1", "사용기한", "테스트"),
                new Barcode("23456", "품목2", "시험번호2", "사용기한", "테스트"),
                new Barcode("34567", "품목3", "시험번호3", "사용기한", "테스트"),
                new Barcode("45678", "품목4", "시험번호4", "사용기한", "테스트"),
        };

        try {
            List<Barcode> barcodes = Arrays.asList(barcodeArray);
            //load file and compile it
            File file = ResourceUtils.getFile("classpath:report/purchase_order_sheet.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(barcodes);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("DataFile", "Java Techie");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            byte[] pdfContent = JasperExportManager.exportReportToPdf(jasperPrint);
            ByteArrayResource resource = new ByteArrayResource(pdfContent);
//            String fileName = "sample2";
//            HttpHeaders header = new HttpHeaders();
//            header.setContentType(new MediaType("application", "pdf"));
//            header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + ".pdf");
//            header.setContentLength(pdfContent.length);

            byte[] encodedPdf = java.util.Base64.getEncoder().encode(pdfContent);


            // int i = 100 / 0;
            RestResponse<byte[]> response = new RestUtil<byte[]>().setData(encodedPdf, "잘 수행되었습니다.");
//            RestResponse<ByteArrayResource> response = new RestUtil<ByteArrayResource>().setData(resource, "잘 수행되었습니다.");
            return new ResponseEntity<RestResponse>(response, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("리포트에서 에러발생");
        }
    }
}


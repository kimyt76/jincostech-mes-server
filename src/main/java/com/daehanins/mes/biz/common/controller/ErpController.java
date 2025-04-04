package com.daehanins.mes.biz.common.controller;

import com.daehanins.mes.biz.adm.entity.ErpConfig;
import com.daehanins.mes.biz.adm.service.IErpConfigService;
import com.daehanins.mes.biz.common.vo.ErpPurchase;
import com.daehanins.mes.biz.common.vo.ErpPurchaseLine;
import com.daehanins.mes.biz.common.vo.ErpPurchaseListVo;
import com.daehanins.mes.biz.pub.entity.Area;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.vo.RestResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Iterator;

/**
 * <p>
 * ERP 연계 Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-13
 */
@RestController
@RequestMapping("/erp")
public class ErpController {

    @Autowired
    WebClient webClient;

    @Autowired
    IErpConfigService erpConfigService;


    @RequestMapping(value = "/webtest", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<Object> webtest(){

               RestResponse res = webClient.get().uri("/pub/area/get/A001")
                .retrieve()
                .bodyToMono(RestResponse.class)
                .block();

               // JsonNode data = res.get("result");


        return new RestUtil<Object>().setData(res.getResult());
    }

    @RequestMapping(value = "/webtest2", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<Area> webtest2() throws Exception{

        JsonNode res = webClient.get().uri("/pub/area/get/A001")
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        JsonNode data = res.get("result");
//        JsonNode data = res.findValue("areaCd");

        Area area = new ObjectMapper().treeToValue(data, Area.class);


        return new RestUtil<Area>().setData(area);
    }

    @RequestMapping(value = "/webtest3", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<String> webtest3() throws Exception{

        JsonNode res = webClient.get().uri("/pub/area/get/A001")
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

//        JsonNode data = res.get("result");
        JsonNode data = res.findValue("areaCd");

        String textVal = data.textValue();



        return new RestUtil<String>().setData(textVal);
    }


    @RequestMapping(value = "/savePurchase", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<String> savePurchase() throws Exception{

        // https://login.ecounterp.com/ECERP/OAPI/OAPIView?lan_type=ko-KR#  연계방법설명

        // TODO :: 서버에서 전표 내역 읽는다.  parameter는 구매입고id

        // 로그인하여 sesseionId 구함
        ErpPurchaseListVo purchaseListVo = new ErpPurchaseListVo();
        ErpPurchase purchase = new ErpPurchase();
        purchase.setIO_DATE("20200510");    // 입고일자
        purchase.setUPLOAD_SER_NO("1"); // 업로드 그룹번호
        purchase.setCUST("2148644483"); // 주피터인터네셔널
        purchase.setCUST_DES("(주)주피터인터내셔널"); // 주피터인터네셔널
        purchase.setWH_CD("S001");      // 원재료창고
        purchase.setEMP_CD("00686");    // 김주호
        purchase.setU_MEMO1("자급");    // 발주구분
        purchase.setPROD_CD("JRW00023");    // 품목코드
        purchase.setPROD_DES("GUM TYPE RL-200 test");   // 품목명
        purchase.setSIZE_DES("25kg");   // 규격
        purchase.setQTY("800");     // 수량
        purchase.setPRICE("65000"); // 단가
        purchase.setSUPPLY_AMT("52000000"); // 공급가
        purchase.setVAT_AMT("5200000"); // 부가세

        purchaseListVo.getPurchasesList().add(new ErpPurchaseLine("1", purchase));

        purchase = new ErpPurchase();

        purchase.setIO_DATE("20200510");    // 입고일자
        purchase.setUPLOAD_SER_NO("1"); // 업로드 그룹번호
        purchase.setCUST("2148644483"); // 주피터인터네셔널
        purchase.setCUST_DES("(주)주피터인터내셔널"); // 주피터인터네셔널
        purchase.setWH_CD("S001");      // 원재료창고
        purchase.setEMP_CD("00686");    // 김주호
        purchase.setU_MEMO1("자급");    // 발주구분
        purchase.setPROD_CD("JRW00025");    // 품목코드
        purchase.setPROD_DES("Carrageenan AKW");   // 품목명
        purchase.setSIZE_DES("20kg");   // 규격
        purchase.setQTY("800");     // 수량
        purchase.setPRICE("31000"); // 단가
        purchase.setSUPPLY_AMT("24800000"); // 공급가
        purchase.setVAT_AMT("2480000"); // 부가세

        purchaseListVo.getPurchasesList().add(new ErpPurchaseLine("2", purchase));

        String data = savePurchasesList(purchaseListVo);
//        JsonNode data = savePurchasesList2(sessionId, purchaseListVo);

        return new RestUtil<String>().setData(data);
    }

    private JsonNode savePurchasesList2(ErpPurchaseListVo purchaseListVo) throws Exception {

        JsonNode res = webClient.post().uri("/Purchases/SavePurchases?SESSION_ID=" + getErpSessionId())
                .body(Mono.just(purchaseListVo), ErpPurchaseListVo.class)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
        return res;
}

    private String savePurchasesList(ErpPurchaseListVo purchaseListVo) throws Exception {

        JsonNode res = webClient.post().uri("/Purchases/SavePurchases?SESSION_ID=" + getErpSessionId())
                .body(Mono.just(purchaseListVo), ErpPurchaseListVo.class)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        // 서버오류 검사
        String status = res.get("Status").textValue();
        if (!status.equals("200")) {
            throw new Exception("서버접속 오류");
        }

        // 라인별 발생한 오류메시지 구함
        int failCnt = res.get("Data").get("FailCnt").intValue();
        String message = "";
        if (failCnt > 0) {
            // 결과 detail array를 iterate하여 에러코드 수집
            Iterator<JsonNode> it = res.get("Data").get("ResultDetails").iterator();
            while (it.hasNext()) {
                JsonNode detailItem = it.next();
                if (!detailItem.get("IsSuccess").booleanValue()) {
                    message += ", line[" + detailItem.get("Line").intValue() + "] " + detailItem.get("TotalError").textValue();
                }
            }
        }
        // 전표번호 구함
        String slipNo = "";
        JsonNode slipNos = res.get("Data").get("SlipNos");
        for (final JsonNode slipNoNode : slipNos) {
            slipNo = slipNoNode.textValue();
            break;
        }
        // TODO:: 비고에 ERP에 생성된 전표번호 등록한다.
        if (slipNo != "") {
        }

        return "전표:[" + slipNo + "]" + message;
    }

    private String getErpSessionId() throws Exception {

        ErpConfig erpConfig = erpConfigService.getById("64301");

        // 기존 0a1ffe23ffaec44f3bd47e87f3e1e21906
        // 신규 3de2e032b8f1e4d7595d27bb1b5da15a30

//        ErpConfig config = new ErpConfig();
//        config.setCOM_CODE("64301");
//        config.setAPI_CERT_KEY("0a1ffe23ffaec44f3bd47e87f3e1e21906");
//        config.setLAN_TYPE("ko_KR");
//        config.setUSER_ID("황길수");
//        config.setZONE("CD");

        JsonNode res = webClient.post().uri("/OAPILogin")
                .body(Mono.just(erpConfig), ErpConfig.class)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        String code = res.get("Data").get("Code").textValue();
        String sessionId = "";
        if (code.equals("00")) {
            sessionId = res.findValue("SESSION_ID").textValue();
        } else {
            String message = res.findValue("Message").textValue();
            throw new Exception("ERP로그인 실패::" + message);
        }
        return sessionId;
    }

}

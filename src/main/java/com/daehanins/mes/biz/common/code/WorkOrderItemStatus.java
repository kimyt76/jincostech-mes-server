package com.daehanins.mes.biz.common.code;

public class WorkOrderItemStatus {

    public static final String 작업지시     = "00";
    public static final String 칭량중       = "11";
    public static final String 칭량완료     = "12";
    public static final String 제조중       = "21";
    public static final String 제조완료     = "22";
    public static final String 코팅중       = "31";
    public static final String 코팅완료     = "32";
    public static final String 충전중       = "41";
    public static final String 충전완료     = "42";
    public static final String 포장중       = "51";
    public static final String 포장완료     = "52";
    public static final String 작업취소     = "99";

    /** 공정코드별 각 공정에 해당하는 시작상태를 반환한다. **/
    public static String getStartStatus(String processCd) {
        String status = "";
        switch ( processCd ) {
            case ProcessCd.칭량 : status = WorkOrderItemStatus.칭량중; break;
            case ProcessCd.제조 : status = WorkOrderItemStatus.제조중; break;
            case ProcessCd.코팅 : status = WorkOrderItemStatus.코팅중; break;
            case ProcessCd.충전 : status = WorkOrderItemStatus.충전중; break;
            case ProcessCd.포장 : status = WorkOrderItemStatus.포장중; break;
            default: break;
        }
        return status;
    }

    /** 공정코드별 각 공정에 해당하는 완료상태를 반환한다. **/
    public static String getEndStatus(String processCd) {
        String status = "";
        switch ( processCd ) {
            case ProcessCd.칭량 : status = WorkOrderItemStatus.칭량완료; break;
            case ProcessCd.제조 : status = WorkOrderItemStatus.제조완료; break;
            case ProcessCd.코팅 : status = WorkOrderItemStatus.코팅완료; break;
            case ProcessCd.충전 : status = WorkOrderItemStatus.충전완료; break;
            case ProcessCd.포장 : status = WorkOrderItemStatus.포장완료; break;
            default: break;
        }
        return status;
    }

    public static String getStatusName(String workOrderItemStatus) {
        String statusName = "";
        switch ( workOrderItemStatus ) {
            case WorkOrderItemStatus.작업지시: statusName = "작업지시"; break;
            case WorkOrderItemStatus.칭량중:  statusName = "칭량중"; break;
            case WorkOrderItemStatus.칭량완료: statusName = "칭량완료"; break;
            case WorkOrderItemStatus.제조중: statusName = "제조중"; break;
            case WorkOrderItemStatus.제조완료: statusName = "제조완료"; break;
            case WorkOrderItemStatus.코팅중: statusName = "코팅중"; break;
            case WorkOrderItemStatus.코팅완료: statusName = "코팅완료"; break;
            case WorkOrderItemStatus.충전중: statusName = "충전중"; break;
            case WorkOrderItemStatus.충전완료: statusName = "충전완료"; break;
            case WorkOrderItemStatus.포장중: statusName = "포장중"; break;
            case WorkOrderItemStatus.포장완료: statusName = "포장완료"; break;
            case WorkOrderItemStatus.작업취소: statusName = "작업취소"; break;
            default: break;
        }
        return statusName;
    }

}

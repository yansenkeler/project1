package com.fruit.client.util;

/**
 * Created by qianyx on 16-5-5.
 */
public class Urls {
    public static final String ROOT = "http://www.51anys.com:91";

    public static final String ADD_PILE = "/AJAXReturnData/Pile.ashx";
    /**
     * type：类别（固定值
     * add/update/delete/query/ submit/createBillNo）
     * BillDate：事件日期
     * Grade：等级
     * CarNo：车牌号
     * RouteCode：路线编号
     * userName：用户编号
     * userPhone：用户手机号
     * Direction：方向
     * Lon：经度
     * Lat：纬度
     * Type1：类别
     * Type2：项目
     * Type3：项目类别
     * cltype：处理类别
     * Number：数量
     * Unit：计量单位
     * ProcessType：处理情况
     * ProcessDept：施工单位
     * RequEndTime：预计完成时间
     * IsProcess：是否已处理
     */
    public static final String ADD_EVENT = "/AJAXReturnData/EventPublish.ashx";
    /**
     * type：类别（固定值
     * add /delete）
     * FilePK：图片PK
     * BillNo：事件编号
     * Type：类别（提交/业务部门确认/施工确认/施工/验收）
     * UpUser：上传用户编号
     */
    public static final String EVENT_IMAGE = "/AJAXReturnData/EventImage.ashx";
    /**
     * type：类别（固定值
     * query/ submit/return）
     * BillPk：事件PK
     * BillNo：事件编号
     * BillDate：事件日期
     * Grade：等级
     * CarNo：车牌号
     * RouteCode：路线编号
     * userName：用户编号
     * userPhone：用户手机号
     * Direction：方向
     * Lon：经度
     * Lat：纬度
     * Type1：类别
     * Type2：项目
     * Type3：项目类别
     * Type：处理类别
     * Number：数量
     * Unit：计量单位
     * ProcessType：处理情况
     * ProcessDept：施工单位
     * RequEndTime：预计完成时间
     * IsProcess：是否已处理
     * Memo：处理意见
     */
    public static final String EVENT_CONFIRM = "/AJAXReturnData/Confirm.ashx";
    /**
     * type：类别（固定值
     * query/ submit/return）
     * BillPk：事件PK
     * userName：用户编号
     * Memo：处理意见
     */
    public static final String CONSTR_CONFIRM = "/AJAXReturnData/ConstrConfirm.ashx";
    /**
     * type：类别（固定值
     * query/ submit/ delay）
     * BillPk：事件PK
     * userName：用户编号
     * Memo：处理意见
     */
    public static final String CONSTR = "/AJAXReturnData/Constr.ashx";
    /**
     * type：类别（固定值
     * query/ submit/return）
     * BillPk：事件PK
     * userName：用户编号
     * Memo：处理意见
     */
    public static final String ACCEPTANCE = "/AJAXReturnData/Acceptance.ashx";
    /**
     * type：类别（固定值
     * add/query）
     * UserNo:用户号
     * Longitude:经度
     * Latitude:纬度
     */
    public static final String GPS_INFO = "/AJAXReturnData/GpsInfo.ashx";
    /**
     * BillNo：事件编号
     * Type1：类别
     * RouteCode：路线编号
     */
    public static final String EVENT_QUERY = "/AJAXReturnData/EventQuery.ashx";
    /**
     * type：类别（固定值
     * query/ end/ queryPoint/ queryEvent）
     * deptPk：用户部门PK
     * dateInfo：日期(格式：yyyyMMdd)
     * TaskNo：任务单号
     * PlanPk：规划pk
     * userNo：用户编号
     */
    public static final String INSPECT_TASK = "/AJAXReturnData/Task.ashx";

    public static final String PERSONAL_CENTER = "/AJAXReturnData/PersonalCenter.ashx";

    public static final String ADD_OTHER = "/AJAXReturnData/Other.ashx";

    public static final String USER_MAG = "/AJAXReturnData/UserMsg.ashx";

}

package com.sky.constant;

/**
 * 账户、报修、派修状态常量
 */
public class StatusConstant {

    //status账户状态
    //启用
    public static final Integer ENABLE = 0;
    //禁用
    public static final Integer DISABLE = 1;

    //repair_status报修状态
    //未提交
    public static final Integer NotSubmitted = 2;
    //待受理
    public static final Integer ToBeAccepted = 3;
    //已派修
    public static final Integer Dispatched = 4;
    //维修结束
    public static final Integer TheRepairIsOver = 5;

    //order_status派修状态
    //未处理
    public static final Integer NotProcessed = 6;
    //已处理
    public static final Integer Processed = 7;

}

package com.ordermanger.online.ordermanager.common;

public class SubaruConfig {

    public static String WY_UPDATE_APK_PATH = "/Xybus/Update/";

    public static String CONFIGNAME = "config_name";
    public static String TOKENKEY = "token";
    public static String WORKERNO = "workerno";
    public static String LocNo = "LocNo";
    public static String LocName = "LocName";
    public static String OwnerNo = "OwnerNo";
    public static String OwnerName = "OwnerName";
    public static String DeptNo = "DeptNo";
    public static String UserRole = "UserRole";
    public static String DeptName = "DeptName";
    public static String Pwd = "Pwd";

    public static String REMBERPWD = "rember_pwd";
    public static String USERNAME = "username";
    public static String PASSWORD = "password";


    public static interface Http {
        public static final String baseUrl = "http://111.40.84.172:7777/API/";

        public static final String login = baseUrl + "Account/Login";

        public static final String UpdateAppVersion = "UpdateAppVersion";

        //获取员工仓库数据权限 所有参数都非明文
        public static final String userLoc = baseUrl + "AccountRole/GetUserLoc?WorkerNo=%s";

        //获取员工业主数据权限
        public static final String getUserOwner = baseUrl + "AccountRole/GetUserOwner?WorkerNo=%s";

        //获取用户审核权限
        public static final String getUserRole = baseUrl + "AccountRole/GetUserRole?LocNo=%s&WorkerNo=%s";

        //获取员工部门数据权限 所有参数都非明文
        public static final String getUserDept = baseUrl + "AccountRole/GetUserDept?LocNo=%s&OwnerNo=%s&WorkerNo=%s";

        //获取商品信息
        public static final String getGoods = baseUrl + "Goods/GetGoods?LocNo=%s&OwnerNo=%s&Page_No=%s&Page_Size=%s&QueryCondition=%s";

        //获取订单头档信息
        public static final String getOrderMasters = baseUrl + "Orders/GetOrderMasters?LocNo=%s&OwnerNo=%s&Page_No=%s&Page_Size=%s&QueryCondition=%s";

        //退出系统
        public static final String getLogout = baseUrl + "Account/LoginOut";

        //非审核订单取消,提交订单明细数据,提交内容为OrderMasterEntiy实列JSON后的加密字符串
        public static final String cancelOrder = baseUrl + "Orders/CancelOrder";

        //审核订单,提交订单明细数据,提交内容为OrderMasterEntiy实列JSON后的加密字符串 OrderMasterEntiy 说明参考方法
        public static final String auditOrder = baseUrl + "Orders/AuditOrder";

        public static final String getOrderDetail = baseUrl + "Orders/GetOrderDetail?LocNo=%s&OwnerNo=%s&SheetID=%s&Page_No=1&Page_Size=100000";


        //新增订单时获取订单号
        public static final String getOrderId = baseUrl + "Orders/GetOrderID?LocNo=%s&OwnerNo=%s";

        //提交订单明细数据,
        public static final String addOrderDetails = baseUrl +  "Orders/AddOrderDetails";

        //获取业主客户信息
        public static final String getUserCustomer = baseUrl + "AccountRole/GetUserCustomer?OwnerNo=%s&Page_No=%s&Page_Size=%s&QueryCondition=%s";

        //未审核订单列表
        public static final String getOrderMastersUnAduit = baseUrl + "Orders/GetOrderMastersUnAduit?LocNo=%s&OwnerNo=%s&Page_No=%s&Page_Size=%s&QueryCondition=%s";
        //未审核订单详情
        public static final String getOrderDetailUnAduit = baseUrl + "Orders/GetOrderDetailUnAduit?LocNo=%s&OwnerNo=%s&SheetID=%s&Page_No=1&Page_Size=100000";

        public static final int HttpSucessCode = 0;
        public static final int HttpErrorCode = -1;
    }
}

package com.vians.admin.response;

import com.vians.admin.enums.BaseEnum;

/**
 * rest 返回CODE定义
 * Created by wangkun23 on 2018/12/19.
 */
public enum ResponseCode implements BaseEnum {

    /***********************************************************************
     * ERROR
     ***********************************************************************/
    ERROR("1000000", "未知错误"),
    //登录错误
    ERROR_USERNAME_NOT_EXIST("1000001", "用户名不存在"),
    ERROR_USERNAME_LENGTH_OUT("1000002", "用户名过长"),
    ERROR_USERNAME_EXIST("1000003", "用户名已存在"),
    ERROR_USERNAME_PWD_INCORRECT("1000004", "用户名或者密码错误"),
    ERROR_CAPTCHA_NOT_MATCH("1000005", "验证码错误"),
    ERROR_UN_AUTHORIZE_LOGIN("1000006", "非授信登录"),
    // 账户错误
    ERROR_ACCOUNT_ACTIVATION("1004000", "账户激活错误"),
    ERROR_ACCOUNT_ACTIVATION_EXPIRED("1004001", "激活链接过期"),
    ERROR_ACCOUNT_ACTIVATION_NOT_EXIST("1004002", "激活账户不存在"),
    ERROR_ACCOUNT_INACTIVE("1004003", "账户待激活"),
    ERROR_ACCOUNT_LOCK("1004004", "账户已锁定"),
    ERROR_ACCOUNT_CANCELLED("1004005", "账户已注销"),
    ERROR_ACCOUNT_EXIST("1004006", "账户已存在"),
    ERROR_ACCOUNT_NOT_LOGIN("1004007", "账户没有登录"),
    ERROR_ACCOUNT_NOT_EXIST("1004008", "账户不存在"),
    ERROR_ACCOUNT_AUTH("1004009", "不合法的凭证类型"),
    // API错误
    ERROR_API_INVALID_GATEWAY("1001000", "无效的网关"),
    ERROR_API_INVALID_UUID("1001001", "无效的 UUID"),
    ERROR_API_INVALID_USER("1001002", "无效的用户"),
    ERROR_API_INVALID_KEY("1001003", "无效的 Key"),
    ERROR_API_BASE64_ERROR("1001004", "Base64 错误"),
    ERROR_API_SEND_TOPIC_FAIL("1001005", "发送主题失败"),
    ERROR_API_CREATE_FILE_ERROR("1001006", "创建文件错误"),
    ERROR_API_JSON_FORMAT_ERROR("1001007", "Json 参数格式错误"),
    ERROR_API_ILLEGAL_DEVICE("1001008", "非法设备"),
    ERROR_API_FILE_TRANSFER_FAIL("1001009", "文件传送失败"),
    ERROR_API_NO_OPERATE_PERMISSION("1001010", "无操作权限"),
    ERROR_API_COMMUNICATING("1001011", "正在通讯中"),
    //设备相关
    ERROR_DEVICE_ADMIN("1006001", "设备不属于该管理员"),
    ERROR_DEVICE_NO_GW("1006002", "设备没有绑定网关盒子"),
    ERROR_DEVICE_NOT_EXIST("1006003", "设备不存在"),
    ERROR_DEVICE_MODEL_NOT_EXIST("1006004", "设备型号不存在"),
    ERROR_DEVICE_CRC_ERROR("1006005", "CRC检验错误"),
    ERROR_DEVICE_PHYSIC_NOT_GET_TOKEN("1006006", "物理账号未获取到Token"),
    ERROR_DEVICE_PHYSIC_NOT_LOGIN("1006007", "物理账号未登录"),
    ERROR_DEVICE_GET_DEVICE_FAILURE("1006008", "获取设备失败"),
    // 项目错误
    ERROR_PROJECT_NAME_EXIST("1007001", "项目名称已存在"),
    // 小区错误
    ERROR_COMMUNITY_NAME_EXIST("1008001", "小区名称已存在"),
    // 楼栋错误
    ERROR_BUILDING_NAME_EXIST("1009001", "楼栋名称已存在"),
    // 单元错误
    ERROR_UNIT_NAME_EXIST("1010001", "单元名称已存在"),
    // 楼层错误
    ERROR_FLOOR_NAME_EXIST("1011001", "楼层名称已存在"),
    // 房间错误
    ERROR_ROOM_NAME_EXIST("1012001", "房间名称已存在"),
    // 人员错误
    ERROR_USER_PHONE_EXIST("1013001", "手机号码已存在"),
    /***********************************************************************
     * SUCCESS
     ***********************************************************************/
    SUCCESS("200", "操作成功"),

    OTHER("0000000", "默认");

    private String key;
    /**
     * 备注
     */
    private String remark;

    ResponseCode(String key, String remark) {
        this.key = key;
        this.remark = remark;
    }

    /**
     * key along with enum
     *
     * @return
     */
    @Override
    public String getKey() {
        return this.key;
    }

    public String getRemark() {
        return this.remark;
    }
}

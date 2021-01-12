package com.vians.admin.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 用户ajax提交之后的相应结果
 * <p>
 * Created by wangkun23 on 2018/12/19.
 */
@ToString
public class ResponseData implements Serializable {

    /**
     * 消息code
     */
    @Setter
    @Getter
    private String code;

    /**
     * 错误提示
     */
    @Setter
    @Getter
    private String message;

    /**
     * 结果数据
     * 如果返回对象或数组直接用setData
     * 如果返回简单的键值对用addData
     */
    @Setter
    @Getter
    private Object data;

    private Map<String, Object> dataShadow;

    public ResponseData() {

    }

    public ResponseData(ResponseCode code) {
        this.code = code.getKey();
        this.message = code.getRemark();
    }

    public ResponseData(ResponseCode code, String message) {
        this.code = code.getKey();
        this.message = message;
    }

    /**
     * 返回成功
     *
     * @return
     */
    public static ResponseData success() {
        return new ResponseData(ResponseCode.SUCCESS);
    }

    /**
     * 返回成功消息
     *
     * @param code 消息code
     * @return 成功消息
     */
    public static ResponseData success(ResponseCode code) {
        return new ResponseData(code);
    }

    /**
     * 返回错误
     *
     * @return
     */
    public static ResponseData error() {
        return new ResponseData(ResponseCode.ERROR);
    }

    /**
     * 返回错误消息
     *
     * @param code 消息code
     * @return 错误消息
     */
    public static ResponseData error(ResponseCode code) {
        return new ResponseData(code);
    }

    /**
     * 返回错误消息
     *
     * @param code 消息code
     * @return 错误消息
     */
    public static ResponseData error(ResponseCode code, String error) {
        return new ResponseData(code, error);
    }

    /**
     * 增加数据绑定
     *
     * @param value
     */
    public ResponseData with(Object value) {
        this.setData(value);
        return this;
    }

    /**
     * 添加返回的数据
     *
     * @param key
     * @param value
     */
    public void addData(String key, Object value) {
        if (this.dataShadow == null) {
            this.dataShadow = new LinkedHashMap<String, Object>();

            this.data = this.dataShadow;
        }
        this.dataShadow.put(key, value);
    }

    /**
     * @param key
     * @return
     */
    public Object removeData(String key) {
        return this.dataShadow != null ? this.dataShadow.remove(key) : null;
    }
}

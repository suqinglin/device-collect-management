package com.vians.admin.emqx;

import com.vians.admin.enums.EmqxMsgType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * mqtt消息体
 *
 * @author plutobe@qq.com
 * @date 2019-12-05 14:51
 */
@ToString
public class EmqxMessageBody<T> {

    /**
     * 消息类型
     */
    @Getter
    @Setter
    private EmqxMsgType msgType;

    /**
     * 消息数据
     */
    @Getter
    @Setter
    private T msgData;

}

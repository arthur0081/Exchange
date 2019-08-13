package com.slabs.exchange.model.dto;

import com.slabs.exchange.common.exception.AbstractExchangeException;
import com.slabs.exchange.common.exception.RespBizMessageEnum;
import com.slabs.exchange.common.exception.RespMessage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by pengchangguo on 16/11/9.
 */
@ApiModel(value = "ResponseDto", description = "响应输出模型")
@Data
public class ResponseDto<T> implements Serializable {

    private static final long serialVersionUID = 8295069664642001595L;

    @ApiModelProperty(value = "请求轨迹Id", required = true)
    private String traceId;

    @ApiModelProperty(value = "响应状态模型", required = true)
    private Status status;

    @ApiModelProperty(value = "业务数据实体")
    private T body;

    /**
     * 重截，不传参，默认构造
     */
    public ResponseDto(RespMessage respMessage) {
        this(respMessage.getErrorCode(),respMessage.getErrorMsg());
    }

    public ResponseDto(AbstractExchangeException exception) {
        this.status = new Status(exception.getErrorCode(), exception.getErrorMsg());
    }

    /**
     * 传入返回内容数据, 默认状态为成功
     *
     * @param body
     *         内容数据
     */
    public ResponseDto(T body) {
        this(RespBizMessageEnum.SUCCESS);
        this.body = body;

    }

    /**
     * 传入状态枚举值和定制的提示信息
     *
     * @param retCode
     * @param customMessage
     *         定制的提示信息
     */
    public ResponseDto(String retCode, String customMessage) {
        this.status = new Status(retCode, customMessage);
    }

    /**
     * 传入状态枚举值, 定制的提示信息
     *
     * @param retCode
     * @param customMessage
     *         定制的提示信息
     * @param body
     *         内容数据
     */
    public ResponseDto(String retCode, String customMessage, T body) {
        this(retCode, customMessage);
        this.body = body;
    }

    @ApiModel(value = "Status", description = "响应状态模型")
    @Data
    public class Status implements Serializable {

        private static final long serialVersionUID = 3583940269234355173L;

        @ApiModelProperty(value = "状态码(成功:000000)", required = true)
        private String code;

        @ApiModelProperty(value = "状态描述", required = true)
        private String msg;

        Status() {
        }

        Status(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        @Override
        public String toString() {
            return String.format("Status [code=%s, msg=%s]", code, msg);
        }

    }

    @Override
    public String toString() {
        return String.format("ResponseDto [traceId=%s, status=%s, body=%s]", traceId, status, body);
    }

}

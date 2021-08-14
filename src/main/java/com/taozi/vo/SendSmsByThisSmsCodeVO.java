package com.taozi.vo;

import com.sun.istack.internal.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 给指定手机发送指定验证码api使用的vo
 *
 * @author taozi - 2021年8月14日, 014 - 23:12:53
 */
@Data
@ApiModel(value="SendSmsByThisSmsCodeVO对象", description="给指定手机发送指定验证码api使用的vo")
public class SendSmsByThisSmsCodeVO {

	@NotNull
	@ApiModelProperty(value = "验证码")
	private String smsCode;

	@NotNull
	@ApiModelProperty(value = "手机号")
	private String phone;
}

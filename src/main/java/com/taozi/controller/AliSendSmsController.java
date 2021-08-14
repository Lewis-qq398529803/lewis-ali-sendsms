package com.taozi.controller;

import com.sun.istack.internal.NotNull;
import com.taozi.config.AliSendSmsConfig;
import com.taozi.utils.AliSendSmsUtils;
import com.taozi.vo.SendSmsByThisSmsCodeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 阿里云短信api
 *
 * @author taozi - 2021年8月14日, 014 - 23:00:06
 */
@Api(tags = "阿里云短信api")
@RestController
@RequestMapping("/sms")
@Slf4j
public class AliSendSmsController {

	@GetMapping("/sendSms")
	@ApiOperation(value = "给指定手机发送随机6位数字验证码", notes = "手机号必填，发送成功时会返回已发送的验证码，失败会返回00000")
	public int sendSms(@NotNull String phone) {
		// 随机生成六位数验证码
		int smsCode = (int) (Math.random() * 1000000);
		// 实体封装
		AliSendSmsConfig aliSendSmsConfig = new AliSendSmsConfig();
		aliSendSmsConfig.setPhoneNumbers(phone);
		aliSendSmsConfig.setTemplateParam("{\"code\":" + smsCode + "}");
		aliSendSmsConfig.setTemplateCode("SMS_217850728");
		// 发送验证码
		log.info("即将给[" + phone + "]发送验证码[" + smsCode + "]......");
		return AliSendSmsUtils.sendSmsMessage(aliSendSmsConfig) ? smsCode : 00000;
	}

	@GetMapping("/sendSmsByThisSmsCode")
	@ApiOperation(value = "给指定手机发送指定验证码", notes = "手机号必填，返回发送结果")
	public boolean sendSmsByThisSmsCode(@Validated SendSmsByThisSmsCodeVO sendSmsByThisSmsCodeVO) {
		String smsCode = sendSmsByThisSmsCodeVO.getSmsCode();
		String phone = sendSmsByThisSmsCodeVO.getPhone();
		// 实体封装
		AliSendSmsConfig aliSendSmsConfig = new AliSendSmsConfig();
		aliSendSmsConfig.setPhoneNumbers(phone);
		aliSendSmsConfig.setTemplateParam("{\"code\":" + smsCode + "}");
		aliSendSmsConfig.setTemplateCode("SMS_217850728");
		// 发送验证码并返回结果
		log.info("即将给[" + phone + "]发送验证码[" + smsCode + "]......");
		return AliSendSmsUtils.sendSmsMessage(aliSendSmsConfig);
	}
}

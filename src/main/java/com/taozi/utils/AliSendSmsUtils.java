package com.taozi.utils;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.taozi.config.AliSendSmsConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * 阿里云短信验证码工具类
 *
 * @author taozi - 2021年6月15日, 015 - 17:00:07
 */
@Slf4j
public class AliSendSmsUtils {

	/**
	 * id
	 */
	private static final String ACCESS_KEY_ID = "ACCESS_KEY_ID";

	/**
	 * 密钥
	 */
	private static final String ACCESS_KEY_SECRET = "ACCESS_KEY_SECRET";

	/**
	 * 签名名称 - 服务器设定好的名称
	 */
	private static final String SIGN_NAME = "SIGN_NAME";

	/**
	 * 版本
	 */
	private static final String VERSION = "2017-05-25";

	/**
	 * 访问链接
	 */
	private static final String DOMAIN = "dysmsapi.aliyuncs.com";

	/**
	 * 不同的内容发送给不同的手机号，最多十个手机号码
	 */
	public static void sendBatchSmsMessage() {
		DefaultProfile profile = DefaultProfile.getProfile("default", ACCESS_KEY_ID, ACCESS_KEY_SECRET);
		IAcsClient client = new DefaultAcsClient(profile);
		CommonRequest request = new CommonRequest();
		request.setMethod(MethodType.POST);
		request.setDomain("dysmsapi.aliyuncs.com");
		request.setVersion("2017-05-25");
		request.setAction("SendBatchSms");
		//默认就行，他自己会分配
		request.putQueryParameter("RegionId", "default");
		//要发送给谁  最多十个手机号码  例子：["15900000000","13500000000"]   为数组和SignNameJson通过下标对应
		request.putQueryParameter("PhoneNumberJson", "[\"15900000000\",\"13500000000\"]");
		request.putQueryParameter("SignNameJson", "签名名称");
		request.putQueryParameter("TemplateCode", "模板code");
		//模板的参数值，key要和模板中一致，然后会将内容进行替换     为数组和SignNameJson通过下标对应
		request.putQueryParameter("TemplateParamJson", "[{\"code\":123},{\"code\":123}]");
		try {
			CommonResponse response = client.getCommonResponse(request);
			System.out.println(response.getData());
		} catch (ClientException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 同一内容,群发
	 * @param aliSendSmsConfig
	 * @return boolean
	 */
	public static boolean sendSmsMessage(AliSendSmsConfig aliSendSmsConfig) {

		DefaultProfile profile = DefaultProfile.getProfile("default", ACCESS_KEY_ID, ACCESS_KEY_SECRET);
		IAcsClient client = new DefaultAcsClient(profile);
		CommonRequest request = new CommonRequest();
		request.setMethod(MethodType.POST);
		request.setDomain(DOMAIN);
		request.setVersion(VERSION);
		request.setAction("SendSms");
		//默认就行，他自己会分配
		request.putQueryParameter("RegionId", "default");
		//要发送给谁  最多十个
		request.putQueryParameter("PhoneNumbers", aliSendSmsConfig.getPhoneNumbers());
		//签名名称
		request.putQueryParameter("SignName", SIGN_NAME);
		//模板code
		request.putQueryParameter("TemplateCode", aliSendSmsConfig.getTemplateCode());
		//key要和模板中一致  然后会将内容进行替换
		request.putQueryParameter("TemplateParam", aliSendSmsConfig.getTemplateParam());
		try {
			CommonResponse response = client.getCommonResponse(request);
			String data = response.getData();
			JSONObject jsonObject = JSONObject.parseObject(data);
			String code = jsonObject.getString("Code");
			log.info(data);
			return StringUtils.equals(code, "OK");
		} catch (ClientException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) {
		int smsCode = (int) (Math.random() * 1000000);
		System.out.println(smsCode);

		//实体封装
		AliSendSmsConfig aliSendSmsConfig = new AliSendSmsConfig();
		aliSendSmsConfig.setPhoneNumbers("13712345678");
		aliSendSmsConfig.setTemplateParam("{\"code\":" + smsCode + "}");
		aliSendSmsConfig.setTemplateCode("SMS_217850728");
		AliSendSmsUtils.sendSmsMessage(aliSendSmsConfig);
	}
}

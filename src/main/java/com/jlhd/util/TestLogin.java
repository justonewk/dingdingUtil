package com.jlhd.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class TestLogin {
	/**
	 * 执行http请求
	 * @param httpRequest
	 * @return
	 */
	private static String executeAndGetResponse(HttpRequestBase httpRequest) {
		httpRequest.addHeader("Content-Type", "application/json; charset=utf-8");
		CloseableHttpClient httpClient = HttpClients.custom().build();		        
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpRequest);
			if(response.getStatusLine().getStatusCode() == 200){
				return EntityUtils.toString(response.getEntity(), "utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(httpClient!=null){
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	/**
	 * 获取ACCESS_TOKEN
	 * @return
	 */
	private static String get_access_token() {
		HttpGet httpGet = new HttpGet("https://oapi.dingtalk.com/sns/gettoken?appid=dingoaby4ui6ftbjflufhe&appsecret=1-chbdiJCljaCQReoUg9w9aE8mr1DW9bWf9GL0yNHmwaT-3AIRx6Qye7DCu2pSrp");
		String result = executeAndGetResponse(httpGet);
		JSONObject object = new  JSONObject(result);
		String ACCESS_TOKEN = object.getString("access_token");
		System.out.println(result);
		return ACCESS_TOKEN;
	}
	/**
	 * 5.在你的web系统获取到代表用户的code之后，使用第3步获取的AccessToken及code获取当前钉钉用户授权给你的持久授权码，此授权码目前无过期时间，可反复使用，参数code只能使用一次。
	 * @return
	 */
	private static String get_persistent_code() {
		HttpPost httpPost = new HttpPost("https://oapi.dingtalk.com/sns/get_persistent_code?access_token=9f8dc04819233b69a6f540d34b956f0d");
		Map<String,Object> sendData = new HashMap<String,Object>();
		sendData.put("tmp_auth_code", "7810f2cf0fda33b8a5123caa2d1b90f9");
		
		StringEntity sEntity = new StringEntity(JSONObject.valueToString(sendData), "utf-8");
		httpPost.setEntity(sEntity);
		String result = executeAndGetResponse(httpPost);
		/*
		 * {"errcode":0,"errmsg":"ok",
		 * "unionid":"HVUtEMniSaBViPt2zmVlFiSjAiEiE",
		 * "openid":"mMVxu9P32mbVrRns0kiPJ5wiEiE",
		 * "persistent_code":"vJaQIVB37OIwSX5_vB3Mx_Z4gLSVKPbSr-fADdZd3Z9S_8_ADy-zX6ys0f9rrnms"}
		 */

		return result;
	}
	/**
	 * 6:在获得钉钉用户的持久授权码后，通过以下接口获取该用户授权的SNS_TOKEN，此token的有效时间为2小时，重复获取不会续期。
	 */
	private static String get_sns_token() {
		HttpPost httpPost = new HttpPost("https://oapi.dingtalk.com/sns/get_sns_token?access_token=9f8dc04819233b69a6f540d34b956f0d");
		Map<String,Object> sendData = new HashMap<String,Object>();
		sendData.put("openid", "mMVxu9P32mbVrRns0kiPJ5wiEiE");
		sendData.put("persistent_code", "vJaQIVB37OIwSX5_vB3Mx_Z4gLSVKPbSr-fADdZd3Z9S_8_ADy-zX6ys0f9rrnms");
		StringEntity sEntity = new StringEntity(JSONObject.valueToString(sendData), "utf-8");
		httpPost.setEntity(sEntity);
		String result = executeAndGetResponse(httpPost);
		return result;
	}
	/**
	 * 7:在获得钉钉用户的SNS_TOKEN后，通过以下接口获取该用户的个人信息。
	 */
	private static String get_unionid_and_name() {
		HttpGet httpPost = new HttpGet("https://oapi.dingtalk.com/sns/getuserinfo?sns_token="+"110e399e02ac37edab52932e72df87ec");
		String result = executeAndGetResponse(httpPost);
		return result;
		/* {"errcode":0,"errmsg":"ok","user_info":{
		* "nick":"吴丹",
		* "unionid":"9hCdbMEnJiiu96ylaLR6HKQiEiE",
		* "dingId":"$:LWCP_v1:$7UJXfQBTdEqVf9AXjs8o/Q==",
		* "openid":"eCXxbIQbMRPezCYwReb3WgiEiE"}}
		*/
	}
	/**
	 * 8:根据unionid获取成员的userid
	 */
	private static String get_userid_by_unionid() {
		HttpGet httpPost = new HttpGet("https://oapi.dingtalk.com/user/getUseridByUnionid?access_token=f934285862173b5a9388cd1577c11d9f&unionid=HVUtEMniSaBViPt2zmVlFiSjAiEiE");
		String result = executeAndGetResponse(httpPost);
		return result;
		// 1022524501688261
	}

	public static void main(String[] args) {
//		System.out.println(get_access_token());
//		System.out.println(get_persistent_code());
//		System.out.println(get_sns_token());
//		System.out.println(get_unionid_and_name());
		System.out.println(get_userid_by_unionid());
		
	}
}

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

public class TestAuth {
	static String cid = "ding834dd528202d8e4735c2f4657eb6378f";
	static String ckey = "22VAHgVUuV9kwIRe040ZMz9sXWoL10y0GEwq0zOEalptLxtSSSW8DI17n4uT-eNt";
	//正常情况下AccessToken有效期为7200秒，有效期内重复获取返回相同结果，并自动续期。
	static String ACCESS_TOKEN=get_access_token();
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
		if (ACCESS_TOKEN==null) {
			StringBuilder sbuilder = new StringBuilder();
			sbuilder.append("https://oapi.dingtalk.com/gettoken?").append("corpid=").append(cid).append("&corpsecret=").append(ckey);
			
			HttpGet httpGet = new HttpGet(sbuilder.toString());
			String result = executeAndGetResponse(httpGet);
			JSONObject object = new  JSONObject(result);
			ACCESS_TOKEN = object.getString("access_token");
			System.out.println("access_token="+ACCESS_TOKEN);
		}
		return ACCESS_TOKEN;
	}
	/**
	 * 获取CorpSecret授权范围
	 */
	public static String get_auth_scope(){
		StringBuilder sBuilder=new StringBuilder();
		String url=sBuilder
				.append("https://oapi.dingtalk.com/auth/scopes?access_token=")
				.append(ACCESS_TOKEN)
				.toString();
		HttpGet httpGet = new HttpGet(url);
		String result = executeAndGetResponse(httpGet);
		return result;
	}

	public static void main(String[] args) {
		System.out.println(get_auth_scope());
	}
}

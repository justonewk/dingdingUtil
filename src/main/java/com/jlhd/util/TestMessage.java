package com.jlhd.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.CorpMessageCorpconversationAsyncsendRequest;
import com.dingtalk.api.response.CorpMessageCorpconversationAsyncsendResponse;
import com.taobao.api.ApiException;

public class TestMessage {
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
	 * @throws ApiException 
	 */
	public static String send_msg() throws ApiException{
		DingTalkClient client = new DefaultDingTalkClient("https://eco.taobao.com/router/rest");
		CorpMessageCorpconversationAsyncsendRequest req = new CorpMessageCorpconversationAsyncsendRequest();
		req.setMsgtype("text");
//		req.setMsgtype("oa");
		req.setAgentId(155889886L);
		req.setUseridList("1022524501688261");
//		req.setDeptIdList("123,456");
		req.setToAllUser(false);
//		req.setMsgcontentString("{\"message_url\": \"http://dingtalk.com\",\"head\": {\"bgcolor\": \"FFBBBBBB\",\"text\": \"头部标题\"},\"body\": {\"title\": \"正文标题\",\"form\": [{\"key\": \"姓名:\",\"value\": \"张三\"},{\"key\": \"爱好:\",\"value\": \"打球、听音乐\"}],\"rich\": {\"num\": \"15.6\",\"unit\": \"元\"},\"content\": \"大段文本大段文本大段文本大段文本大段文本大段文本大段文本大段文本大段文本大段文本大段文本大段文本\",\"image\": \"@lADOADmaWMzazQKA\",\"file_count\": \"3\",\"author\": \"李四 \"}}");
		req.setMsgcontentString("{\"content\": \"验证码\"}");
		CorpMessageCorpconversationAsyncsendResponse rsp = client.execute(req, ACCESS_TOKEN);
		return rsp.getBody();
	}

	public static void main(String[] args) throws ApiException, UnsupportedEncodingException {
//		System.out.println(send_msg());
		System.out.print("https://oapi.dingtalk.com/connect/oauth2/sns_authorize?appid=dingoaby4ui6ftbjflufhe&response_type=code&scope=snsapi_login&state=STATE&redirect_uri=");
		System.out.println(URLEncoder.encode("http://woooooody.applinzi.com/", "utf-8"));
		//?code=109c746f124835e38ce32bb72dbf6e97&state=STATE 
		//https://oapi.dingtalk.com/sns/gettoken?appid=dingoaby4ui6ftbjflufhe&appsecret=1-chbdiJCljaCQReoUg9w9aE8mr1DW9bWf9GL0yNHmwaT-3AIRx6Qye7DCu2pSrp	

	}
}

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

public class TestUser {
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
	 * 获取管理员列表
	 */
	public static String get_admin(){
		StringBuilder sBuilder=new StringBuilder();
		String url=sBuilder.append("https://oapi.dingtalk.com/user/get_admin?access_token=").append(ACCESS_TOKEN).toString();
		HttpGet httpGet = new HttpGet(url);
		String result = executeAndGetResponse(httpGet);
		return result;
	}
	/**
	 * 获取部门成员
	 */
	public static String get_userlist_by_dep(int dep_id){
		/*
		{"name":"王昆","userid":"0921636620943067"},
		{"name":"于妮妮","userid":"152668293620060366"},
		{"name":"于晓清","userid":"manager1821"}
		*/
		StringBuilder sBuilder=new StringBuilder();
		String url=sBuilder
				.append("https://oapi.dingtalk.com/user/simplelist?access_token=")
				.append(ACCESS_TOKEN)
				.append("&department_id=")
				.append(dep_id)
				.toString();
		HttpGet httpGet = new HttpGet(url);
		String result = executeAndGetResponse(httpGet);
		return result;
	}
	/**
	 * 获取部门成员列表（详情）
	 * {"errcode":0,"errmsg":"ok","userlist":[
	 * 		{"unionid":"9hCdbMEnJiiu96ylaLR6HKQiEiE",
	 * 		"openId":"9hCdbMEnJiiu96ylaLR6HKQiEiE",
	 * 		"dingId":"$:LWCP_v1:$7UJXfQBTdEqVf9AXjs8o/Q==",
	 * 		"isLeader":true,
	 * 		"mobile":"18040360252",
	 * 		"active":true,
	 * 		"isAdmin":false,
	 * 		"avatar":"",
	 * 		"userid":"1022524501688261",
	 * 		"isHide":false,
	 * 		"isBoss":false,
	 * 		"name":"吴丹",
	 * 		"department":[58873230],
	 * 		"order":180017761137183260}
	 * ]}
	 */
	public static String get_userlist_detail(int dep_id){
		StringBuilder sBuilder=new StringBuilder();
		String url=sBuilder
				.append("https://oapi.dingtalk.com/user/list?access_token=")
				.append(ACCESS_TOKEN)
				.append("&department_id=")
				.append(dep_id)
				.toString();
		HttpGet httpGet = new HttpGet(url);
		String result = executeAndGetResponse(httpGet);
		return result;
	}
	/**
	 * 获取成员详情
	 */
	public static String get_user_detail(String userid){
		StringBuilder sBuilder=new StringBuilder();
		String url=sBuilder
				.append("https://oapi.dingtalk.com/user/get?access_token=")
				.append(ACCESS_TOKEN)
				.append("&userid=")
				.append(userid)
				.toString();
		HttpGet httpGet = new HttpGet(url);
		String result = executeAndGetResponse(httpGet);
		return result;
	}
	/**更新部门成员详情
	 */
	public static String update_user_detail(){
		StringBuilder sBuilder=new StringBuilder();
		String url=sBuilder
				.append("https://oapi.dingtalk.com/user/update?access_token=")
				.append(ACCESS_TOKEN)
				.toString();
		HttpPost httpPost = new HttpPost(url);
		
		Map<String,Object> sendData = new HashMap<String,Object>();
		sendData.put("userid", "manager1821");
//		sendData.put("name", "吴丹");
//		sendData.put("remark", "备注");
//		sendData.put("jobnumber", "员工工号");
		sendData.put("position", "aaaa");
//		sendData.put("department", new Integer[]{1,58873230,58817269});
		
		StringEntity sEntity = new StringEntity(JSONObject.valueToString(sendData), "utf-8");
		httpPost.setEntity(sEntity);
		String result = executeAndGetResponse(httpPost);
		return result;
	}

	public static void main(String[] args) {
		//System.out.println(get_admin());
		//System.out.println(get_userlist_by_dep(58873230));
		//System.out.println(get_userlist_detail(58873230));
		System.out.println(get_user_detail("1022524501688261"));
		//System.out.println(update_user_detail());
	}
}

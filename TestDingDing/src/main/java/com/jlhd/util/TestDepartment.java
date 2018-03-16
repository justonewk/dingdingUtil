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

public class TestDepartment {
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
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append("https://oapi.dingtalk.com/gettoken?").append("corpid=").append(cid).append("&corpsecret=").append(ckey);
		
		HttpGet httpGet = new HttpGet(sbuilder.toString());
		String result = executeAndGetResponse(httpGet);
		JSONObject object = new  JSONObject(result);
		return object.getString("access_token");
	}
	/**
	 * 获取部门列表
	 * {"errcode":0,"department":[
	 * 		{"createDeptGroup":true,"name":"开发者工作室","id":1,"autoAddUser":true},
	 * 		{"createDeptGroup":false,"name":"部门1.1","id":58800288,"autoAddUser":false,"parentid":58873230},
	 * 		{"createDeptGroup":false,"name":"部门2","id":58817269,"autoAddUser":false,"parentid":1},
	 * 		{"createDeptGroup":false,"name":"部门1","id":58873230,"autoAddUser":false,"parentid":1}
	 * ],"errmsg":"ok"}
	 */
	public static String get_dep_list(){
		StringBuilder sBuilder=new StringBuilder();
		String url=sBuilder
				.append("https://oapi.dingtalk.com/department/list?access_token=")
				.append(ACCESS_TOKEN)
				.toString();
		HttpGet httpGet = new HttpGet(url);
		String result = executeAndGetResponse(httpGet);
		return result;
	}
	/**
	 * 获取部门详情
	 */
	public static String get_dep_detail(int id){
		StringBuilder sBuilder=new StringBuilder();
		String url=sBuilder
				.append("https://oapi.dingtalk.com/department/get?access_token=")
				.append(ACCESS_TOKEN)
				.append("&id="+id)
				.toString();
		HttpGet httpGet = new HttpGet(url);
		String result = executeAndGetResponse(httpGet);
		return result;
	}
	/**
	 * 创建部门
	 */
	public static String create_dep(){
		
		/* {"createDeptGroup":true,"name":"开发者工作室","id":1,"autoAddUser":true},
		 * {"createDeptGroup":false,"name":"部门2","id":58817269,"autoAddUser":false,"parentid":1},
		 * {"createDeptGroup":false,"name":"部门1","id":58873230,"autoAddUser":false,"parentid":1}
		 * 全部 
		 * 部门1 58873230
		 * 部门2 58817269
		 * 部门1.1 58800288
		 */
		StringBuilder sBuilder=new StringBuilder();
		String url=sBuilder
				.append("https://oapi.dingtalk.com/department/create?access_token=")
				.append(ACCESS_TOKEN)
				.toString();
		HttpPost httpPost = new HttpPost(url);
		
		Map<String,Object> sendData = new HashMap<String,Object>();
		sendData.put("parentid", "58873230");
		sendData.put("name", "部门1.1");
		
		StringEntity sEntity = new StringEntity(JSONObject.valueToString(sendData), "utf-8");
		httpPost.setEntity(sEntity);	
		String result = executeAndGetResponse(httpPost);
		return result;
	}
	/**
	 * 更新部门
	 */
	public static String update_dep(){
		StringBuilder sBuilder=new StringBuilder();
		String url=sBuilder
				.append("https://oapi.dingtalk.com/department/update?access_token=")
				.append(ACCESS_TOKEN)
				.toString();
		HttpPost httpPost = new HttpPost(url);
		
		Map<String,Object> sendData = new HashMap<String,Object>();
		sendData.put("id", 58873230);
		sendData.put("deptManagerUseridList", "1022524501688261");
		
		StringEntity sEntity = new StringEntity(JSONObject.valueToString(sendData), "utf-8");
		httpPost.setEntity(sEntity);	
		String result = executeAndGetResponse(httpPost);
		return result;
	}

	public static void main(String[] args) {
		System.out.println(get_dep_list());
		System.out.println(get_dep_detail(1));
//		System.out.println(create_dep());
//		System.out.println(update_dep());
	}
}

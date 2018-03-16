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

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.CorpRoleListRequest;
import com.dingtalk.api.request.CorpRoleSimplelistRequest;
import com.dingtalk.api.response.CorpRoleListResponse;
import com.dingtalk.api.response.CorpRoleSimplelistResponse;
import com.taobao.api.ApiException;

public class TestRole {
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
	 * 获取员工角色列表
	 * {"dingtalk_corp_role_list_response":{"result":{"has_more":"false","list":{
	 * "role_groups":[
	 * 		{"group_name":"默认","roles":{"roles":[
	 * 			{"id":273625306,"role_name":"主管理员"},
	 * 			{"id":273625307,"role_name":"子管理员"},
	 * 			{"id":273625308,"role_name":"负责人"},
	 * 			{"id":273625309,"role_name":"主管"}
	 * 		]}},
	 * 		{"group_name":"职务","roles":{"roles":[
	 * 			{"id":273625311,"role_name":"财务"},
	 * 			{"id":273625312,"role_name":"人事"},
	 * 			{"id":273625313,"role_name":"出纳"},
	 * 			{"id":273625314,"role_name":"销售"},
	 * 			{"id":273625315,"role_name":"客服"},
	 * 			{"id":273625316,"role_name":"质检"},
	 * 			{"id":273625317,"role_name":"研发"},
	 * 			{"id":273625318,"role_name":"行政"},
	 * 			{"id":273625319,"role_name":"设计"},
	 * 			{"id":273625320,"role_name":"产品"}
	 * 		]}},
	 * 		{"group_name":"岗位","roles":{"roles":[
	 * 			{"id":273625322,"role_name":"普通员工"},
	 * 			{"id":273625323,"role_name":"经理"},
	 * 			{"id":273625324,"role_name":"科长"},
	 * 			{"id":273625325,"role_name":"部长"},
	 * 			{"id":273625326,"role_name":"总监"},
	 * 			{"id":273625327,"role_name":"管理层"},
	 * 			{"id":273625328,"role_name":"高级管理者"},
	 * 			{"id":273625329,"role_name":"总经理"}
	 * 		]}}
	 * ]}},
	 * "request_id":"47s39gxm51k3"}}
	 */
	public static String get_role_list() throws ApiException{
		DingTalkClient client = new DefaultDingTalkClient("https://eco.taobao.com/router/rest");
		CorpRoleListRequest req = new CorpRoleListRequest();
		req.setSize(20L);
		req.setOffset(0L);
		CorpRoleListResponse rsp = client.execute(req, ACCESS_TOKEN);
		return rsp.getBody();
	}
	/**
	 * 查看角色下的所有用户
	 * @return
	 * @throws ApiException
	 */
	public static String get_user_list_by_role(long roleId) throws ApiException{
		DingTalkClient client = new DefaultDingTalkClient("https://eco.taobao.com/router/rest");
		CorpRoleSimplelistRequest req = new CorpRoleSimplelistRequest();
		req.setRoleId(roleId);
		CorpRoleSimplelistResponse rsp = client.execute(req, ACCESS_TOKEN);
		return rsp.getBody();
	}

	public static void main(String[] args) throws ApiException {
		System.out.println(get_role_list());
		//System.out.println(get_user_list_by_role(273625306L));
		//System.out.println(get_user_list_by_role(273625307L));
	}
}

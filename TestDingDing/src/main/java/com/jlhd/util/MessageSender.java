package com.jlhd.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.core.util.JsonUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.CorpMessageCorpconversationAsyncsendRequest;
import com.dingtalk.api.request.CorpRoleSimplelistRequest;
import com.dingtalk.api.response.CorpMessageCorpconversationAsyncsendResponse;
import com.dingtalk.api.response.CorpReportListResponse.JsonObject;
import com.dingtalk.api.response.CorpRoleSimplelistResponse;
import com.taobao.api.ApiException;

public class MessageSender {
	String cid = "ding834dd528202d8e4735c2f4657eb6378f";
	String ckey = "22VAHgVUuV9kwIRe040ZMz9sXWoL10y0GEwq0zOEalptLxtSSSW8DI17n4uT-eNt";

	public void testSend(){
		DingTalkClient client = new DefaultDingTalkClient("https://eco.taobao.com/router/rest");
		
		String access_token = getAccessTocken();		
		
//		String chatid = createChatGroup(access_token);
//		String result = chatInGroup(access_token, chatid, "hello 测试");
//		System.err.println(chatid +"\r\n" +result);
		
		String result = sendNotice(access_token);
		System.err.println("\r\n" +result); 
		
//		CorpMessageCorpconversationAsyncsendRequest req = new CorpMessageCorpconversationAsyncsendRequest();
//		req.setMsgtype("text");
//		req.setAgentId(1234L);
////		req.setUseridList("zhangsan,lisi");
//		req.setDeptIdList("1");
//		req.setToAllUser(true);
//		
//		JSONObject o = new JSONObject();
//		o.put("content", "通知重要....12223ssss");
//		
//		req.setMsgcontentString(o.toString());
//		CorpMessageCorpconversationAsyncsendResponse rsp;
//		try {
//			rsp = client.execute(req, access_token);
//			System.out.println(rsp.getBody());
//		} catch (ApiException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
//		DingTalkClient client = new DefaultDingTalkClient("https://eco.taobao.com/router/rest");
//		CorpMessageCorpconversationAsyncsendRequest req = new CorpMessageCorpconversationAsyncsendRequest();
//		req.setMsgtype("oa");
//		req.setAgentId(1234L);
//		req.setUseridList("zhangsan,lisi");
//		req.setDeptIdList("1");
//		req.setToAllUser(true);
//		req.setMsgcontentString("{\"message_url\": \"http://dingtalk.com\",\"head\": {\"bgcolor\": \"FFBBBBBB\",\"text\": \"头部标题\"},\"body\": {\"title\": \"正文标题\",\"form\": [{\"key\": \"姓名:\",\"value\": \"张三\"},{\"key\": \"爱好:\",\"value\": \"打球、听音乐\"}],\"rich\": {\"num\": \"15.6\",\"unit\": \"元\"},\"content\": \"大段文本大段文本大段文本大段文本大段文本大段文本大段文本大段文本大段文本大段文本大段文本大段文本\",\"image\": \"@lADOADmaWMzazQKA\",\"file_count\": \"3\",\"author\": \"李四 \"}}");
//		CorpMessageCorpconversationAsyncsendResponse rsp = client.execute(req, access_token);
//		System.out.println(rsp.getBody());
		
		
//		CorpRoleSimplelistRequest req = new CorpRoleSimplelistRequest();
//		req.setRoleId(1203141L);
//		req.setSize(20L);
//		req.setOffset(0L);
//		CorpRoleSimplelistResponse rsp;
//		try {
//			rsp = client.execute(req, access_token);
//			System.out.println(rsp.getBody());
//		} catch (ApiException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	private static String executeAndGetResponse(HttpRequestBase httpRequest) {
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	/**
	 * 获取tocken通信用
	 * @return
	 */
	public String getAccessTocken(){
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append("https://oapi.dingtalk.com/gettoken?").append("corpid=").append(cid).append("&corpsecret=").append(ckey);
		
		HttpGet httpGet = new HttpGet(sbuilder.toString());
		httpGet.addHeader("Content-Type", "application/json; charset=utf-8");
		String result = executeAndGetResponse(httpGet);
		JSONObject object = new  JSONObject(result);
		String access_token = object.getString("access_token");
		return access_token;
	}
	

	/**
	 * 取用户列表
	 * @param accessTocken
	 * @return
	 */
	public String getUserList(String accessTocken){
		StringBuilder sbuilder = new StringBuilder("https://oapi.dingtalk.com/user/list?");
		sbuilder.append("access_token=").append(accessTocken).append("&department_id=1");
		HttpGet httpGet = new HttpGet(sbuilder.toString());
		httpGet.addHeader("Content-Type", "application/json; charset=utf-8");
		String result = executeAndGetResponse(httpGet);
		JSONObject object = new  JSONObject(result);
		return result;
	}
	
	/**
	 * 取部门ID信息
	 * @param accessTocken
	 * @return
	 */
	public String getScopes(String accessTocken){
		StringBuilder sbuilder = new StringBuilder("https://oapi.dingtalk.com/auth/scopes?");
		sbuilder.append("access_token=").append(accessTocken);
		
		HttpGet httpGet = new HttpGet(sbuilder.toString());
		httpGet.addHeader("Content-Type", "application/json; charset=utf-8");
		String result = executeAndGetResponse(httpGet);
		JSONObject object = new  JSONObject(result);
		return result;
	}
	
	
	/**
	 * 发送普通消息
	 * @param accessTocken
	 * @return
	 */
	public String sendToUser(String accessTocken, String srcUserId, String destUserId){
		StringBuilder sbuilder = new StringBuilder("https://oapi.dingtalk.com/auth/scopes?");
		sbuilder.append("access_token=").append(accessTocken);
		
		HttpGet httpGet = new HttpGet(sbuilder.toString());
		httpGet.addHeader("Content-Type", "application/json; charset=utf-8");
		String result = executeAndGetResponse(httpGet);
		JSONObject object = new  JSONObject(result);
		return result;
	}
	
	/**
	 * 创建群聊
	 * @param accessTocken
	 * @return
	 */
	public String createChatGroup(String accessTocken){
		StringBuilder sbuilder = new StringBuilder("https://oapi.dingtalk.com/chat/create?");
		sbuilder.append("access_token=").append(accessTocken);
		
		Map<String,String> sendData = new HashMap<String,String>();
		List<String> userList = new ArrayList<String>();
		userList.add("manager1821");
		userList.add("152668293620060366");
		JSONArray array = new JSONArray(userList);
		
		sendData.put("name", "全员群");
		sendData.put("owner", "manager1821");
		sendData.put("useridlist", array.toString());
		
		HttpPost httpPost = new HttpPost(sbuilder.toString());
		httpPost.addHeader("Content-Type", "application/json; charset=utf-8");
		
		StringEntity sEntity = new StringEntity(JSONObject.valueToString(sendData), "utf-8");
		httpPost.setEntity(sEntity);
		
		String result = executeAndGetResponse(httpPost);
		JSONObject object = new  JSONObject(result);
		
		return object.getString("chatid");
	}
	
	
	public String chatInGroup(String accessTocken, String chatid, String msg){
		StringBuilder sbuilder = new StringBuilder("https://oapi.dingtalk.com/chat/send?");
		sbuilder.append("access_token=").append(accessTocken);
		
		Map<String,String> sendData = new HashMap<String,String>();
		
		sendData.put("chatid", chatid);
		sendData.put("msgtype", "text");
		
		JSONObject o = new JSONObject();
		o.put("content", msg);
		
		sendData.put("text", o.toString());
		
		HttpPost httpPost = new HttpPost(sbuilder.toString());
		httpPost.addHeader("Content-Type", "application/json; charset=utf-8");
		
		StringEntity sEntity = new StringEntity(JSONObject.valueToString(sendData), "utf-8");
		httpPost.setEntity(sEntity);
		
		String result = executeAndGetResponse(httpPost);
		JSONObject object = new  JSONObject(result);
		
		return result;
	}
	
	public String sendNotice(String accessTocken){
		StringBuilder sbuilder = new StringBuilder("https://oapi.dingtalk.com/message/send?");
		sbuilder.append("access_token=").append(accessTocken);
		
		Map<String,String> sendData = new HashMap<String,String>();
		
//		userList.add("manager1821");
//		userList.add("152668293620060366");
//		
		sendData.put("agentid", "155889886");
//		sendData.put("touser", "manager1821|152668293620060366");
		
		
		sendData.put("touser", "0418496430937967");
		sendData.put("msgtype", "text");
		
//		JSONObject o = new JSONObject();
//		o.put("content", "胡涛sb？");
		
		sendData.put("text", "胡涛sb？");
		
		HttpPost httpPost = new HttpPost(sbuilder.toString());
		httpPost.addHeader("Content-Type", "application/json; charset=utf-8");
		
		StringEntity sEntity = new StringEntity(JSONObject.valueToString(sendData), "utf-8");
		httpPost.setEntity(sEntity);
		
		String result = executeAndGetResponse(httpPost);
		JSONObject object = new  JSONObject(result);
		
		return result;
		
	}
	public String sendNotice(String touser,JSONObject sendData){
		StringBuilder sbuilder = new StringBuilder("https://oapi.dingtalk.com/message/send?");
		sbuilder.append("access_token=").append(getAccessTocken());
		//Map<String,String> sendData=new HashMap<>();	
		sendData.put("agentid", "155889886");
		sendData.put("touser", touser);
		
		HttpPost httpPost = new HttpPost(sbuilder.toString());
		httpPost.addHeader("Content-Type", "application/json; charset=utf-8");
		
		StringEntity sEntity = new StringEntity(JSONObject.valueToString(sendData), "utf-8");
		httpPost.setEntity(sEntity);
		
		String result = executeAndGetResponse(httpPost);
		System.err.println(result);
		return result;
		
	}
	public static void main(String[] args){
		String touser="0921636620943067";
		String msgtype="text";
		JSONObject jObject=new JSONObject("{\"markdown\":{\"text\":\"### 充值成功 \\n- **金　额** ¥ 98.0 \\n- **充值人** 王刚 \\n- **游　戏** 尘缘 \\n- **角　色** 美术5 \\n- **账单号** 18020717280111 \\n- **时　间** 2018-02-10 04:06:36 \\nRechargeEntity [Id=113, roleId=4, rechargeTime=1518206796, rechargeNumber=18020717280111, rechargeAmount=98.0, ownergmid=100, admingmid=100, state=0, gameid=14]\",\"title\":\"充值成功\"},\"msgtype\":\"markdown\"}");
		
		for(int i=0;i<1000000000;i++){
			String content="胡涛sb"+i;
			
			MessageSender sender = new MessageSender();
			sender.sendNotice(touser,jObject);
		}
		
	}
}

class DingUser {
	String userId ;
	String name;
	public DingUser(String userId, String name) {
		super();
		this.userId = userId;
		this.name = name;
	}
	public String getUserId() {
		return userId;
	}
	public String getName() {
		return name;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}

package com.jlhd.util;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 消息推送机器人
 * 
 * @author wk
 * @date 2017年12月1日
 */
public class MessageRobertUtil {
	private final static Logger log=LogManager.getLogger(MessageRobertUtil.class);
	public static String WEBHOOK_TOKEN = "https://oapi.dingtalk.com/robot/send?access_token=a45db7a3dd8a2484c6c4a831efd083ddfc6b7a781bda3bc2bb57a2d5b792553c";

	private static HttpPost createHttpPost(Map<String, Object> dingDingMarkdownMessage) {
		if (WEBHOOK_TOKEN==null||WEBHOOK_TOKEN.trim().isEmpty()) {
			return null;
		}
		HttpPost httpPost = new HttpPost(WEBHOOK_TOKEN);
		httpPost.addHeader("Content-Type", "application/json; charset=utf-8");
		StringEntity sEntity = new StringEntity(JSONObject.valueToString(dingDingMarkdownMessage), "utf-8");
		httpPost.setEntity(sEntity);
		return httpPost;
	}

	/**
	 * 指定@多人发送
	 * @param mobiles
	 * @param text
	 */
	public static void sendMarkdownToDingDing(String title ,String text,List<String> mobiles) {
		Map<String, Object> dingDingMarkdownMessage=getDingDingMarkdownMessage(title, text,mobiles);
		HttpPost httpPost = createHttpPost(dingDingMarkdownMessage);
		if (httpPost==null) {
			
			return;
		}
		log.info("send mark down message to ding ding. title:{}, mobiles:{}, text:{}",title,mobiles.toString(),text);
		executeAndGetResponse(httpPost);
	}
	/**
	 * 全群发送并
	 * @param string
	 */
	public static void sendMarkdownToDingDing(String title, String text) {
		Map<String, Object> dingDingMarkdownMessage=getDingDingMarkdownMessage(title, text, true);
		HttpPost httpPost = createHttpPost(dingDingMarkdownMessage);
		if (httpPost==null) {
			log.error("|resp error|title:{} text:{}|ding ding robot token is null. ",title,text);
			return;
		}
		log.info("send mark down message to ding ding. title:{}, text:{}",title,text);
		executeAndGetResponse(httpPost);
	}
	/**
	 * 发送并获得响应
	 * @param httpPost
	 */
	private static void executeAndGetResponse(HttpPost httpPost) {
		CloseableHttpClient httpClient = HttpClients.custom().build();		        
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpPost);
			String result = EntityUtils.toString(response.getEntity(), "utf-8");
			log.info("dingding server result:"+result);
			httpClient.close();
		} catch (Exception e) {
			log.error(e.getMessage(),e);
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
	}
	/**
	 * 创建markdown格式的map,不指定@某人，可选@所有人
	 * @param title
	 * @param text
	 * @param isAtAll
	 */
	private static Map<String, Object> getDingDingMarkdownMessage(String title,String text,boolean isAtAll) {
		Map<String, Object> map=new HashMap<>();
		map.put("msgtype", "markdown");
		
		Map<String, Object> markdown=new HashMap<>();
		markdown.put("title", title);
		markdown.put("text", text);
		map.put("markdown", markdown);
		
		Map<String, Object> at=new HashMap<>();
		at.put("isAtAll", false);
		map.put("at", at);
		
		return map;
	}
	/**
	 * 创建markdown格式的map,可指定@多人
	 * @param title
	 * @param text
	 * @param atMobiles
	 */
	private static Map<String, Object> getDingDingMarkdownMessage(String title,String text,List<String> atMobiles) {
		Map<String, Object> map=new HashMap<>();
		map.put("msgtype", "markdown");
		
		Map<String, Object> markdown=new HashMap<>();
		markdown.put("title", title);
		markdown.put("text", text);
		map.put("markdown", markdown);
		
		Map<String, Object> at=new HashMap<>();
		at.put("atMobiles", atMobiles);
		at.put("isAtAll", false);
		map.put("at", at);
		
		return map;
	}
	
	
	public static void main(String[] args){
		MessageRobertUtil.sendMarkdownToDingDing(args[0], args[1]);
	}
}

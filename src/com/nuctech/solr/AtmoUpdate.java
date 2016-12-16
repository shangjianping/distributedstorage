package com.nuctech.solr;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.solr.client.solrj.SolrServerException;

public class AtmoUpdate {
	String Base_URL;
	
	AtmoUpdate(String Base_URL){
		this.Base_URL = Base_URL;
	}
	
	private String sendHttpMessage(String url, String contents){
		return sendHttpMessage(url,"POST",contents);
	}
	
	private static String sendHttpMessage(String url, String method, String contents) {
		try {
			
			URL serverUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) serverUrl
					.openConnection();
			conn.setConnectTimeout(20000);
	
			conn.setRequestMethod(method);// "POST" ,"GET"
			
			conn.addRequestProperty("Accept", "*/*");
			conn.addRequestProperty("Accept-Language", "zh-cn");
			conn.addRequestProperty("Accept-Encoding", "gzip, deflate");
			conn.addRequestProperty("Cache-Control", "no-cache");
			conn.addRequestProperty("Accept-Charset", "UTF-8");
			conn.addRequestProperty("Content-type", "application/json");			
			
			if (method.equalsIgnoreCase("GET")) {
				conn.connect();
			}
	
			else if (method.equalsIgnoreCase("POST")) {
	
				conn.setDoOutput(true);
				conn.connect();
				conn.getOutputStream().write(contents.getBytes());
			} else {
				throw new RuntimeException("your method is not implement");
			}
	
			InputStream ins = conn.getInputStream();
	
			// 处理GZIP压缩的
			if (null != conn.getHeaderField("Content-Encoding")
					&& conn.getHeaderField("Content-Encoding").equals("gzip")) {
				byte[] b = null;
				GZIPInputStream gzip = new GZIPInputStream(ins);
				byte[] buf = new byte[1024];
				int num = -1;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				while ((num = gzip.read(buf, 0, buf.length)) != -1) {
					baos.write(buf, 0, num);
				}
				b = baos.toByteArray();
				baos.flush();
				baos.close();
				gzip.close();
				ins.close();
				return new String(b, "UTF-8").trim();
			}
	
			String charset = "UTF-8";
			InputStreamReader inr = new InputStreamReader(ins, charset);
			BufferedReader br = new BufferedReader(inr);
	
			String line = "";
			StringBuffer sb = new StringBuffer();
			do {
				sb.append(line);
				line = br.readLine();
			} while (line != null);			
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();			
			return "";
		}
	
	}
	
	public void AddIndex() {
		JSONArray content = new JSONArray();
		JSONObject json = new JSONObject();
		try {
			json.put("id", "book2");
			json.put("copies_i", 5);
			json.put("cat_ss", "Science Fiction");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		content.add(json);
		sendHttpMessage(Base_URL + "/update", content.toString());				
	}
	
	public void UpdateIndex() {
		JSONArray content = new JSONArray();
		JSONObject json = new JSONObject();
		JSONObject set = new JSONObject();
		JSONObject inc = new JSONObject();
		JSONObject add = new JSONObject();
		
		try {
			set.put("set", "Neal Stephenson1"); // {"set":"Neal Stephenson"}
			inc.put("inc", "3"); //{"inc":3}
			add.put("add", "Cyberpunk"); //{"add":"Cyberpunk"}			
			json.put("id", "book2");
			json.put("author_s", set);
			json.put("type_i", inc);
			json.put("cat_ss", add);
			content.add(json);
			System.out.println(content);
			System.out.println(json);
		} catch (final JSONException e) {
		}
		sendHttpMessage(Base_URL + "/update", content.toString());
	}
	
	public static void main(String[] args) throws SolrServerException, IOException {

		AtmoUpdate atmoUpdate = new AtmoUpdate("http://master.node:8983/solr/core0");
		//atmoUpdate.AddIndex();
		atmoUpdate.UpdateIndex();

	}
}


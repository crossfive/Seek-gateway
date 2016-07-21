package com.crossfive.framework.exec;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpHeaders.Values;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.fastjson.JSONObject;

public class TestThread implements Runnable {

	public static AtomicInteger ids = new AtomicInteger(0);
	
	private URL url;
	
	private int id;
	
	public TestThread(URL url) {
		this.url = url;
		this.id = ids.incrementAndGet();
	}
	
	@Override
	public void run() {
		 try {
             HttpURLConnection httpURLConnection = (HttpURLConnection) url
                 .openConnection();
             httpURLConnection.setConnectTimeout(30000);
             httpURLConnection.setDoInput(true);// 从服务器获取数据
             httpURLConnection.setDoOutput(true);// 向服务器写入数据

             JSONObject jo = new JSONObject();
             jo.put("action", "testAction");
             jo.put("method", "test2");
             jo.put("id", id);
             jo.put("username", "gaoyf");
             jo.put("password", "a12345");

             byte[] mydata = jo.toJSONString().getBytes();
             
             // 设置请求体的类型
             httpURLConnection.setRequestProperty("Content-Type",
                 "application/x-www-form-urlencoded");
             httpURLConnection.setRequestProperty("Content-Lenth",
                 String.valueOf(mydata.length));
             httpURLConnection.setRequestProperty(HttpHeaders.Names.CONNECTION, Values.KEEP_ALIVE);

             // 获得输出流，向服务器输出数据
             OutputStream outputStream = (OutputStream) httpURLConnection
                 .getOutputStream();
             outputStream.write(mydata);

             // 获得服务器响应的结果和状态码
             int responseCode = httpURLConnection.getResponseCode();
             if (responseCode == 200) {

            	 byte[] result = new byte[128];
               // 获得输入流，从服务器端获得数据
               InputStream inputStream = (InputStream) httpURLConnection
                   .getInputStream();
               inputStream.read(result);
               System.out.println(new String(result));
               return;

             }

           } catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
           }
	}

}

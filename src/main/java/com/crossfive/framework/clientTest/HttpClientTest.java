package com.crossfive.framework.clientTest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import com.alibaba.fastjson.JSONObject;

public class HttpClientTest {

	public static void main(String[] a) {
		sendMessage();
	}
	
	public static void sendMessage() {
		
		URL uri = null;
		try {
			uri = new URL("http://127.0.0.1:8082/root/gateway.action?command=wish@read&id=2");
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  
//		ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(10);
//		for (int i = 0; i <= 10; i++) {
//			pool.scheduleWithFixedDelay(new TestThread(uri), 0, 5, TimeUnit.SECONDS);
//		}
		 try {
             HttpURLConnection httpURLConnection = (HttpURLConnection) uri
                 .openConnection();
             httpURLConnection.setConnectTimeout(5000);
             httpURLConnection.setReadTimeout(5000);
             httpURLConnection.setDoInput(true);// 从服务器获取数据
             httpURLConnection.setDoOutput(true);// 向服务器写入数据

             JSONObject jo = new JSONObject();
             jo.put("action", "wishAction");
             jo.put("method", "read");
             jo.put("username", "gaoyf2");
             jo.put("password", "1234");
             jo.put("id", 3);
//             jo.put("vip", 1);

             byte[] mydata = jo.toJSONString().getBytes();
             
             // 设置请求体的类型
             httpURLConnection.setRequestProperty("Content-Type",
                 "application/x-www-form-urlencoded");
             httpURLConnection.setRequestProperty("Content-Lenth",
                 String.valueOf(mydata.length));
             httpURLConnection.setRequestProperty("Connection", "keep-alive");
             long start = System.currentTimeMillis();
             // 获得输出流，向服务器输出数据
             OutputStream outputStream = (OutputStream) httpURLConnection
                 .getOutputStream();
             outputStream.write(mydata);
             
             // 获得服务器响应的结果和状态码
             int responseCode = httpURLConnection.getResponseCode();
             if (responseCode == 200) {
            	System.out.println("response cost:"+ (System.currentTimeMillis()-start));
               // 获得输入流，从服务器端获得数据
               InputStream inputStream = (InputStream) httpURLConnection
                   .getInputStream();
               int count = 0;    
               while (count == 0) {    
                //count = in.available();    
                count = httpURLConnection.getContentLength();//(HttpResponse response)    
               }    
               System.out.println("@@@count ===" +count);
               int readCount = 0; // 已经成功读取的字节的个数   
               byte[] result = new byte[count];
               while (readCount < count) {    
                readCount += inputStream.read(result, readCount, count - readCount);    
               }
              
               int value;    
               int offset = 0;
//               value = (int) ( ((result[offset] & 0xFF)<<24)  
//                       |((result[offset+1] & 0xFF)<<16)  
//                       |((result[offset+2] & 0xFF)<<8)  
//                       |(result[offset+3] & 0xFF));
               byte[] len = Arrays.copyOfRange(result, 0, 4);
               value = (int) ( ((len[offset] & 0xFF)<<24)  
                     |((len[offset+1] & 0xFF)<<16)  
                     |((len[offset+2] & 0xFF)<<8)  
                     |(len[offset+3] & 0xFF));
               System.out.println(value);
               byte[] command = Arrays.copyOfRange(result, 4, 4+32);
               System.out.println(new String(command));
               byte[] content = Arrays.copyOfRange(result, 36, readCount);
//               System.arraycopy(result, 4+32+1, content, readCount - 1, readCount-4-32);
               System.out.println(new String(content));
               System.out.println("cost:"+ (System.currentTimeMillis()-start));
               inputStream.close();
               outputStream.close();
               httpURLConnection.disconnect();
               return;

             }

           } catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
           }
	}
}

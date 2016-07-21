package com.crossfive.framework;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ClientTest {
	
	public static String ssotoken = "c684f08e22c63b9a6a96cd2c54fce05c";
	
	public static void main(String[] args) {
//		getSSOTOKEN();
		
		Runnable r = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					sendMessage();
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					testQuerySIMCardByCommunicateNumbers();
				}
			}
		};
		for (int i = 0; i< 100; i++) {
			Thread t1 = new Thread(r);
			t1.start();
		}
		while(true) {}
//		sendMessage();
	}
//	private static void getSSOTOKEN() {
//			URL uri = null;
//	try {
//		uri = new URL("http://123.56.12.228:8000/rest/iov.signin/10092/0?userId=101&rpcOperatorId=1394&ssoToken=61fe8983bcff6a0971ae62cdd031379f&_dataCharset=UTF-8&signinName=super%40e6gps.com&pass=123456&userType=1");
//	} catch (MalformedURLException e1) {
//		// TODO Auto-generated catch block
//		e1.printStackTrace();
//	}  
////	ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(10);
////	for (int i = 0; i <= 10; i++) {
////		pool.scheduleWithFixedDelay(new TestThread(uri), 0, 5, TimeUnit.SECONDS);
////	}
//	 try {
//         HttpURLConnection httpURLConnection = (HttpURLConnection) uri
//             .openConnection();
//         httpURLConnection.setConnectTimeout(5000);
//         httpURLConnection.setReadTimeout(5000);
//         httpURLConnection.setDoInput(true);// 从服务器获取数据
//         httpURLConnection.setDoOutput(true);// 向服务器写入数据
//
////         JSONObject jo = new JSONObject();
////         jo.put("action", "wishAction");
////         jo.put("method", "read");
////         jo.put("username", "gaoyf2");
////         jo.put("password", "1234");
////         jo.put("id", 3);
//////         jo.put("vip", 1);
////
////         byte[] mydata = jo.toJSONString().getBytes();
////         
////         // 设置请求体的类型
////         httpURLConnection.setRequestProperty("Content-Type",
////             "application/x-www-form-urlencoded");
////         httpURLConnection.setRequestProperty("Content-Lenth",
////             String.valueOf(mydata.length));
//         httpURLConnection.setRequestProperty("Connection", "keep-alive");
//         long start = System.currentTimeMillis();
//         // 获得输出流，向服务器输出数据
//         OutputStream outputStream = (OutputStream) httpURLConnection
//             .getOutputStream();
////         outputStream.write(mydata);
//         
//         // 获得服务器响应的结果和状态码
//         int responseCode = httpURLConnection.getResponseCode();
//         if (responseCode == 200) {
//        	System.out.println("response cost:"+ (System.currentTimeMillis()-start));
//           // 获得输入流，从服务器端获得数据
//           InputStream inputStream = (InputStream) httpURLConnection
//               .getInputStream();
//           int count = 0;    
//           while (count == 0) {    
//            //count = in.available();    
//            count = httpURLConnection.getContentLength();//(HttpResponse response)    
//           }    
//           int readCount = 0; // 已经成功读取的字节的个数   
//           byte[] result = new byte[count];
//           while (readCount < count) {    
//            readCount += inputStream.read(result, readCount, count - readCount);    
//           }
//           System.out.println("cost:"+ (System.currentTimeMillis()-start));
//           JSONObject jo = JSONObject.parseObject(new String(result));
//           ssotoken = jo.getString("ssoToken");
//           System.out.println(ssotoken);
//           inputStream.close();
//           outputStream.close();
//           httpURLConnection.disconnect();
//           return;
//
//         }
//
//       } catch (IOException e) {
//         // TODO Auto-generated catch block
//         e.printStackTrace();
//       }
//	
//	}
	public static void sendMessage() {
		
		URL uri = null;
		try {
			uri = new URL("http://172.16.196.7:8000/rest/iov.vehicle.searchVehicle/10092/0?userId=101&rpcOperatorId=1394&ssoToken="+ssotoken+"&_dataCharset=UTF-8&conditions=%7B%22TransportCorporationId%22%3A%222452%22%7D&pageNum=0&pageSize=40");
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

//             JSONObject jo = new JSONObject();
//             jo.put("action", "wishAction");
//             jo.put("method", "read");
//             jo.put("username", "gaoyf2");
//             jo.put("password", "1234");
//             jo.put("id", 3);
////             jo.put("vip", 1);
//
//             byte[] mydata = jo.toJSONString().getBytes();
//             
//             // 设置请求体的类型
//             httpURLConnection.setRequestProperty("Content-Type",
//                 "application/x-www-form-urlencoded");
//             httpURLConnection.setRequestProperty("Content-Lenth",
//                 String.valueOf(mydata.length));
             httpURLConnection.setRequestProperty("Connection", "keep-alive");
             long start = System.currentTimeMillis();
             // 获得输出流，向服务器输出数据
             OutputStream outputStream = (OutputStream) httpURLConnection
                 .getOutputStream();
//             outputStream.write(mydata);
             
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

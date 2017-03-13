package com.plmz.fl.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class HttpUtil {
	private final static int TIME_OUT = 10 * 1000;
	private final static String CHARSET = "UTF-8";
	private final static String BOUNDARY = UUID.randomUUID().toString();
	private final static String CONTENT_TYPE = "multipart/form-data";

	public static String connect(String urlStr) {
		String back = null;
//		DataOutputStream dos = null;
		String paramsStr = null;
		BufferedReader reader = null;
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Charset", CHARSET);
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);
//			dos = new DataOutputStream(conn.getOutputStream());
//			dos.write(paramsByte);
//			 dos.write(LINE_END.getBytes());
//			dos.flush();
//			dos.close();
			int res = conn.getResponseCode();
			if (res == 200) {
				StringBuilder buffer = new StringBuilder();
				reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"GBK"));
				String line=null;
				while((line = reader.readLine())!=null){
					buffer.append(line);
				}
				back = buffer.toString();

//				InputStream input = conn.getInputStream();
//				StringBuffer sb1 = new StringBuffer();
//				int ss;
//				while ((ss = input.read()) != -1) {
//					sb1.append((char) ss);
//				}
//				back = sb1.toString();
				System.out.println("back==" + back);
			}
		} catch (MalformedURLException e) {
			System.out.println("httpUtil异常：" + e.getMessage());
			back = null;
		} catch (IOException e) {
			System.out.println("httpUtil异常：" + e.getMessage());
			back = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("httpUtil异常：" + e.getMessage());
			back = null;
		} finally {
			if(null!=reader) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return back;
	}
}

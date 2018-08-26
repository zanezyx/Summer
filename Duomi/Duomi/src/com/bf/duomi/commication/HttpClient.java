package com.bf.duomi.commication;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.SyncBasicHttpContext;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpClient {
	private static final int DEFAULT_MAX_CONNECTIONS = 10;
	private static final int DEFAULT_SOCKET_TIMEOUT = 10 * 1000;
	private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;
	private static String PROXY_IP = "http://10.0.0.172:80";
	private static BasicHttpParams httpParams;
	private static final DefaultHttpClient httpClient;
	private static final int TIME_OUT = 30 * 1000; // 超时时间
	private static final String CHARSET = "utf-8"; // 设置编码

	private static HttpContext httpContext = new SyncBasicHttpContext(
			new BasicHttpContext());

	static {

		httpParams = new BasicHttpParams();

		ConnManagerParams.setTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT);
		ConnManagerParams.setMaxConnectionsPerRoute(httpParams,
				new ConnPerRouteBean(DEFAULT_MAX_CONNECTIONS));
		ConnManagerParams.setMaxTotalConnections(httpParams,
				DEFAULT_MAX_CONNECTIONS);

		HttpConnectionParams.setSoTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT);
		HttpConnectionParams.setConnectionTimeout(httpParams,
				DEFAULT_SOCKET_TIMEOUT);
		HttpConnectionParams.setTcpNoDelay(httpParams, true);
		HttpConnectionParams.setSocketBufferSize(httpParams,
				DEFAULT_SOCKET_BUFFER_SIZE);

		HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
		// HttpProtocolParams.setUserAgent(httpParams, "yunlai");

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", SSLSocketFactory
				.getSocketFactory(), 443));
		ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(
				httpParams, schemeRegistry);

		httpClient = new DefaultHttpClient(cm, httpParams);

	}

	private static String execute(HttpUriRequest httpRequest)
			throws IOException, ClientProtocolException, HttpResponseException {
		HttpResponse httpResponse = httpClient
				.execute(httpRequest, httpContext);
		StatusLine status = httpResponse.getStatusLine();
		String responseBody = null;
		HttpEntity entity = null;
		HttpEntity temp = httpResponse.getEntity();
		if (temp != null) {
			entity = new BufferedHttpEntity(temp);
			responseBody = EntityUtils.toString(entity, "UTF-8");
		}

		if (status.getStatusCode() >= 300) {
			throw new HttpResponseException(status.getStatusCode(),
					status.getReasonPhrase());
		}
		return responseBody;

	}

	public static String get(String url) throws ClientProtocolException,
			IOException {
		HttpConnectionParams.setSoTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT);
		HttpConnectionParams.setConnectionTimeout(httpParams,
				DEFAULT_SOCKET_TIMEOUT);
		HttpUriRequest httpRequest = new HttpGet(url);

		return execute(httpRequest);
	}

	public static String getViaBadNetwork(String url)
			throws ClientProtocolException, IOException {
		HttpConnectionParams.setSoTimeout(httpParams,
				DEFAULT_SOCKET_TIMEOUT * 3);
		HttpConnectionParams.setConnectionTimeout(httpParams,
				DEFAULT_SOCKET_TIMEOUT * 3);
		HttpUriRequest httpRequest = new HttpGet(url);
		return execute(httpRequest);
	}

	public static String get(String url, RequestParams params)
			throws HttpResponseException, ClientProtocolException, IOException {
		return get(getUrlWithQueryString(url, params));
	}

	public static String getViaCmwap(String url) throws IOException {
		URL targetUrl = new URL(url);
		URL proxyUrl = new URL(PROXY_IP + targetUrl.getPath() + "?"
				+ targetUrl.getQuery());
		Log.i("网络连接", proxyUrl.toString());
		HttpURLConnection httpUrlConnection = (HttpURLConnection) proxyUrl
				.openConnection();

		httpUrlConnection.setRequestProperty("X-Online-Host",
				targetUrl.getHost() + ":" + targetUrl.getPort());
		httpUrlConnection.setRequestMethod("GET");
		httpUrlConnection.setRequestProperty("Accept", "text/html");
		httpUrlConnection.setRequestProperty("Pragma", "No-cache");
		httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
		httpUrlConnection.setConnectTimeout(30 * 1000);
		byte[] rspData;
		try {
			if (httpUrlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new HttpResponseException(
						httpUrlConnection.getResponseCode(),
						httpUrlConnection.getResponseMessage());
			}

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			BufferedInputStream input = new BufferedInputStream(
					httpUrlConnection.getInputStream());
			int ch = 0;
			while ((ch = input.read()) != -1) {
				bos.write(ch);
			}
			rspData = bos.toByteArray();
		} finally {
			httpUrlConnection.disconnect();
		}

		return rspData == null ? "{}" : new String(rspData);

	}

	public static String post(String url) throws HttpResponseException,
			ClientProtocolException, IOException {
		HttpPost httpRequest = new HttpPost(url);
		return execute(httpRequest);
	}

	public static String post(String url, InputStream stream, String key)
			throws HttpResponseException, ClientProtocolException, IOException {
		HttpPost httpRequest = new HttpPost(url);
		RequestParams params = new RequestParams();
		params.put(key, stream);
		httpRequest.setEntity(params.getEntity());
		return execute(httpRequest);
	}

	public static String post(String url, File file, String key)
			throws HttpResponseException, ClientProtocolException, IOException {
		HttpPost httpRequest = new HttpPost(url);
		RequestParams params = new RequestParams();
		params.put(key, file);
		httpRequest.setEntity(params.getEntity());
		return execute(httpRequest);
	}

	public static String post(String url, RequestParams params)
			throws HttpResponseException, ClientProtocolException, IOException {
		HttpPost httpRequest = new HttpPost(url);
		httpRequest.setEntity(params.getEntity());
		return execute(httpRequest);
	}

	public static String getUrlWithQueryString(String url, RequestParams params) {
		if (params != null) {
			String paramString = params.getParamString();
			if (url.indexOf("?") == -1) {
				url += "?" + paramString;
			} else {
				url += "&" + paramString;
			}
		}

		return url;
	}

	/**
	 * android上传文件到服务器
	 * 
	 * @param file
	 *            需要上传的文件
	 * @param RequestURL
	 *            请求的rul
	 * @return 返回响应的内容
	 */
	public static String getContentByPost(String urlStr,
			Map<String, String> textMap,
			// List<File> bitmaps,
			File bitmaps, String nameType) {
		String res = "";
		HttpURLConnection conn = null;
		String BOUNDARY = "---------------------------123821742118716"; // boundary就是request头和上传文件内容的分隔符
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(TIME_OUT);
			conn.setReadTimeout(TIME_OUT);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + BOUNDARY);

			OutputStream out = new DataOutputStream(conn.getOutputStream());
			// text
			if (textMap != null) {
				StringBuffer strBuf = new StringBuffer();
				Iterator iter = textMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					strBuf.append("\r\n").append("--").append(BOUNDARY)
							.append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ inputName + "\"\r\n\r\n");
					strBuf.append(inputValue);
				}
				out.write(strBuf.toString().getBytes());
			}

			// 单张图片
			if (bitmaps != null) {
				String filename = bitmaps.getName();
				String contentType = "application/octet-stream";

				StringBuffer strBuf = new StringBuffer();
				strBuf.append("\r\n").append("--").append(BOUNDARY)
						.append("\r\n");
				strBuf.append("Content-Disposition: form-data; name=\""
						+ nameType + "\"; filename=\"" + filename + "\"\r\n");
				strBuf.append("Content-Type:" + contentType + "\r\n\r\n");

				out.write(strBuf.toString().getBytes());

				DataInputStream in = new DataInputStream(new FileInputStream(
						bitmaps));
				int bytes = 0;
				byte[] bufferOut = new byte[1024];
				while ((bytes = in.read(bufferOut)) != -1) {
					out.write(bufferOut, 0, bytes);
				}
				in.close();

			}

			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();

			// 读取返回数据
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			res = strBuf.toString();
			reader.close();
			reader = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return res;
	}
}

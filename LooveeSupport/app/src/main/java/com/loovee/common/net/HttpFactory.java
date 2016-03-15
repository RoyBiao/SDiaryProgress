package com.loovee.common.net;

import android.content.Context;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.util.PreferencesCookieStore;

import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class HttpFactory {
	public static int CONN_TIMEOUT = 60 * 1000;
	public static int THREAD_POOL_SIZE = 3;
	private static int maxRetries = 5;
	private static String CHARSET = "utf-8";

	/**
	 * 创建一个默认的HttpUtils
	 * 
	 * @param context
	 *            上下文
	 * @param cookieSupport
	 *            是否支持cookie
	 * @return
	 */
	public static HttpUtils createDefault(Context context, boolean cookieSupport) {
		HttpUtils http = null;
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			http = new HttpUtils(CONN_TIMEOUT);
			if (cookieSupport) {
				http.configCookieStore(new PreferencesCookieStore(context));
			}
			http.configRequestThreadPoolSize(THREAD_POOL_SIZE);
			http.configRegisterScheme(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			http.configRegisterScheme(new Scheme("https", sf, 8443));
			http.configRequestRetryCount(maxRetries);
			http.configResponseTextCharset(CHARSET);
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return http;
	}
	
	public static HttpUtils createDefault(Context context, boolean cookieSupport,int httpsPort) {
		HttpUtils http = null;
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			http = new HttpUtils(CONN_TIMEOUT);
			if (cookieSupport) {
				http.configCookieStore(new PreferencesCookieStore(context));
			}
			http.configRequestThreadPoolSize(THREAD_POOL_SIZE);
			http.configRegisterScheme(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			http.configRegisterScheme(new Scheme("https", sf, httpsPort));
			http.configRequestRetryCount(maxRetries);
			http.configResponseTextCharset(CHARSET);
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return http;
	}

}

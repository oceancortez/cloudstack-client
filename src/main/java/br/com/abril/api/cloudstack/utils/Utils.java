package br.com.abril.api.cloudstack.utils;

import java.io.IOException;
import java.net.URLEncoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

public class Utils {
	private static final Logger log = Logger.getLogger(Utils.class);
	
	public static String signRequest(String request, String key) {
		
		try {
			Mac mac = Mac.getInstance("HmacSHA1");
			SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(),
					"HmacSHA1");
			mac.init(keySpec);
			mac.update(request.getBytes());
			byte[] encryptedBytes = mac.doFinal();
			return URLEncoder.encode(Base64.encodeBase64String(encryptedBytes),
					"UTF-8");
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return null;
	}
	
	public static void clientHttpGet(String host, String finalUrl, String  userId, String  apiKey) {

		DefaultHttpClient httpclient = new DefaultHttpClient();

		try {

			httpclient.getCredentialsProvider().setCredentials(
					new AuthScope(host, 80),
					new UsernamePasswordCredentials(userId, apiKey));

			HttpGet httpget = new HttpGet(finalUrl);
			log.info("Request " + httpget.getURI());
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody;
			responseBody = httpclient.execute(httpget, responseHandler);
			log.info("Responser : " + responseBody);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}

	}
}

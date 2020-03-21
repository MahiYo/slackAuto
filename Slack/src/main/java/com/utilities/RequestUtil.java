package com.utilities;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class RequestUtil {
	private static HttpClient httpClient;
	
	public static HttpResponse postRequest(String restURL, List<NameValuePair> headers, List<NameValuePair> queryParams,
			JSONObject payloadJson) throws Exception, URISyntaxException {
		httpClient = HttpClients.createDefault();
		URIBuilder builder = new URIBuilder(restURL);

		if (queryParams != null) {
			for (NameValuePair param : queryParams) {
				builder.setParameter(param.getName(), param.getValue());
			}
		}

		HttpPost httpPost = new HttpPost(builder.build());
		if (headers != null) {
			for (NameValuePair headerPair : headers) {

				httpPost.setHeader(headerPair.getName(), headerPair.getValue());
			}
		}

		System.out.println("Post Request URL : " + httpPost.getRequestLine().getUri());
		if (payloadJson != null) {
			System.out.println("Post Request Payload : " + payloadJson);
			StringEntity JsonEntityObj = new StringEntity(payloadJson.toString(), "UTF-8");
			httpPost.setEntity(JsonEntityObj);
		}
		HttpResponse httpResponse = httpClient.execute(httpPost);
		return httpResponse;
	}
	
	
	public static HttpResponse getRequest(String restUrl, List<NameValuePair> headers, List<NameValuePair> queryParams)
			throws Exception, URISyntaxException {
		httpClient = HttpClients.createDefault();
		URIBuilder builder = new URIBuilder(restUrl);

		if (queryParams != null) {
			for (NameValuePair param : queryParams) {
				builder.setParameter(param.getName(), param.getValue());
			}
		}
		System.out.println("URL: " + builder);
		HttpGet httpGet = new HttpGet(builder.build());
		if (headers != null) {
			for (NameValuePair headerPair : headers) {

				httpGet.setHeader(headerPair.getName(), headerPair.getValue());
			}
		}

		HttpResponse getResponse = httpClient.execute(httpGet);
		return getResponse;
	}
	
	public static JSONObject convert_HttpResponse_to_JsonObject(HttpResponse httpResponse)
			throws Exception, ParseException, IOException, JSONException {
		HttpEntity responseEntity = httpResponse.getEntity();

		String responseString = EntityUtils.toString(responseEntity, "UTF-8");
		System.out.println("Response String : " + responseString);
		return new JSONObject(responseString);

	}
}

package testcases;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;

import com.configuration.PathConfiguration;
import com.utilities.RequestUtil;

public class SlackUtilities {
	public static String restUrl;

	public static int createRandomNumer() {
		Random randomGen = new Random();
		int value = randomGen.nextInt(99) + 1;
		return value;
	}

	public static JSONObject addNewChannel(String token, String name) throws URISyntaxException, Exception {
		restUrl = PathConfiguration.url + PathConstants.CREATE_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));

		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));
		query.add(new BasicNameValuePair("name", name));

		HttpResponse httpResponse = RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200, "Response code mismatch");
		JSONObject response = RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getJSONObject("channel").getString("name"), name, "name mismatch");
		Assert.assertNotNull(response.getJSONObject("channel").getString("id"), "id not present");
		Assert.assertTrue(response.getJSONObject("channel").getBoolean("is_channel"), "is_channel is not enabled");
		Assert.assertFalse(response.getJSONObject("channel").getBoolean("is_group"), "is_group is enabled");
		Assert.assertTrue(response.getBoolean("ok"), "ok mismatch");
		return response.getJSONObject("channel");
	}

	public static JSONArray getAllChannel(String token) throws URISyntaxException, Exception {
		restUrl = PathConfiguration.url + PathConstants.GET_ALL_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));

		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));

		HttpResponse httpResponse = RequestUtil.getRequest(restUrl, headers, query);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200, "Response code mismatch");
		JSONObject response = RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		JSONArray channels = response.getJSONArray("channels");
		Assert.assertTrue(channels.length() > 0, "No channel present");
		for (int i = 0; i < channels.length(); i++) {
			Assert.assertNotNull(channels.getJSONObject(i).getString("id"), "id not present");
			Assert.assertNotNull(channels.getJSONObject(i).getString("name"), "id not present");
			Assert.assertTrue(channels.getJSONObject(i).getBoolean("is_channel"), "is_channel is not enabled");
		}
		Assert.assertTrue(response.getBoolean("ok"), "ok mismatch");
		return channels;
	}
	
	public static void archieveChannel(String token,String channelId) throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.ARCHIEVE_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));
		query.add(new BasicNameValuePair("channel", channelId));
		
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertTrue(response.getBoolean("ok"),"ok mismatch");
	}
}

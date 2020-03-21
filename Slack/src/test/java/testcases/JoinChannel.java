package testcases;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.internal.remote.SlavePool;

import com.configuration.PathConfiguration;
import com.utilities.RequestUtil;

public class JoinChannel {
	public static String restUrl;
	public static String token=PathConfiguration.accesstoken;
	public static String channelId;
	
	@Test(enabled=true,priority=1)
	public void joinChannelSuccessfully() throws URISyntaxException, Exception {
		String name="newchannelname_"+SlackUtilities.createRandomNumer();
		JSONObject newChannelResponse=SlackUtilities.addNewChannel(token, name);
		channelId=newChannelResponse.getString("id");
		restUrl=PathConfiguration.url+PathConstants.JOIN_CHANNEL;
		
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));
		query.add(new BasicNameValuePair("channel", channelId));
		
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getJSONObject("channel").getString("name"), name,"name mismatch");
		Assert.assertNotNull(response.getJSONObject("channel").getString("id"),"id not present");
		Assert.assertTrue(response.getBoolean("ok"),"ok mismatch");
		Assert.assertEquals(response.getString("warning"),"already_in_channel","warning mismatch");
	}
	
	@Test(enabled=true,priority=2)
	public void joinChannelWithBlankToken() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.JOIN_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", ""));
		query.add(new BasicNameValuePair("channel", channelId));
		
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "not_authed","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
	
	@Test(enabled=true,priority=3)
	public void joinChannelWithInvalidToken() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.JOIN_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", "sdklsa@"));
		query.add(new BasicNameValuePair("channel", channelId));
		
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "invalid_auth","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
	
	@Test(enabled=true,priority=4)
	public void joinChannelWithNullValueToken() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.JOIN_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		String value=null;
		query.add(new BasicNameValuePair("token", value));
		query.add(new BasicNameValuePair("channel", channelId));
		
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "not_authed","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
	
	@Test(enabled=true,priority=5)
	public void joinChannelWithoutToken() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.JOIN_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("channel", channelId));
		
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "not_authed","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
		SlackUtilities.archieveChannel(token, channelId);
	}
	
	@Test(enabled=true)
	public void joinChannelWithBlankChannelId() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.JOIN_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));
		query.add(new BasicNameValuePair("channel", ""));
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "channel_not_found","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
	
	@Test(enabled=true)
	public void joinChannelInvalidStringChannelId() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.JOIN_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));
		query.add(new BasicNameValuePair("channel", "skldjsklf"));
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "channel_not_found","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
		
	@Test(enabled=true)
	public void joinChannelInvalidIntValueChannelId() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.JOIN_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));
		query.add(new BasicNameValuePair("channel", "10"));
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "channel_not_found","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
	
	@Test(enabled=true)
	public void joinChannelWithNullValueChannelId() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.JOIN_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));
		String channel=null;
		query.add(new BasicNameValuePair("channel",channel));
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "channel_not_found","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
	
	@Test(enabled=true)
	public void joinChannelWithoutChannelId() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.JOIN_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "channel_not_found","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
}

package testcases;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.configuration.PathConfiguration;
import com.utilities.RequestUtil;

public class ArchiveChannel {
	public static String restUrl;
	public static String token=PathConfiguration.accesstoken;
	public static String channelId;
	
	@BeforeClass
	public void createChannel() throws URISyntaxException, Exception
	{
		String name="channelcreation_"+SlackUtilities.createRandomNumer();
		JSONObject newChannelResponse=SlackUtilities.addNewChannel(token, name);
		channelId=newChannelResponse.getString("id");
	}
	
	@Test(enabled=true,priority=5)
	public void archieveChannelSuccessfully() throws URISyntaxException, Exception {
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
		int count=0;
		JSONArray channels=SlackUtilities.getAllChannel(token);
		for(int i=0;i<channels.length();i++)
		{
			if(channels.getJSONObject(i).getString("id").equals(channelId))
			{
				Assert.assertTrue(channels.getJSONObject(i).getBoolean("is_archived"), "archived not done");
				count=count+1;
			}
		}
		Assert.assertTrue(count==1,"Channel is not archieve");
	}
	
	@Test(enabled=true,priority=6)
	public void archieveChannelWithAlreadyarchieve() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.ARCHIEVE_CHANNEL;
		
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));
		query.add(new BasicNameValuePair("channel", channelId));
		
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "already_archived","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
		
	}
	
	@Test(enabled=true,priority=1)
	public void archieveChannelWithBlankToken() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.ARCHIEVE_CHANNEL;
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
	
	@Test(enabled=true,priority=2)
	public void archieveChannelWithInvalidToken() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.ARCHIEVE_CHANNEL;
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
	
	@Test(enabled=true,priority=3)
	public void archieveChannelWithNullValueToken() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.ARCHIEVE_CHANNEL;
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
	
	@Test(enabled=true,priority=4)
	public void archieveChannelWithoutToken() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.ARCHIEVE_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("channel", channelId));
		
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "not_authed","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
	
	@Test(enabled=true)
	public void archieveChannelWithBlankChannelId() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.ARCHIEVE_CHANNEL;
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
	public void archieveChannelInvalidStringChannelId() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.ARCHIEVE_CHANNEL;
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
	public void archieveChannelInvalidIntValueChannelId() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.ARCHIEVE_CHANNEL;
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
	public void archieveChannelWithNullValueChannelId() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.ARCHIEVE_CHANNEL;
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

}

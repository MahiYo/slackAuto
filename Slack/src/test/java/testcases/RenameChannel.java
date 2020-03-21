package testcases;

import static org.testng.Assert.assertEquals;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.configuration.PathConfiguration;
import com.utilities.RequestUtil;

public class RenameChannel {
	public static String restUrl;
	public static String token=PathConfiguration.accesstoken;
	public static String channelId;
	public static String name; 
	
	@BeforeClass
	public void createChannel() throws URISyntaxException, Exception
	{
		name="newchannel_"+SlackUtilities.createRandomNumer();
		JSONObject newChannelResponse=SlackUtilities.addNewChannel(token, name);
		channelId=newChannelResponse.getString("id");
	}
	
	@AfterClass
	public void deleteChannel() throws URISyntaxException, Exception
	{
		SlackUtilities.archieveChannel(token, channelId);
	}
	@Test(enabled=true,priority=2)
	public void renameChannelSuccessfully() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.RENAME_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));
		query.add(new BasicNameValuePair("channel", channelId));
		String rename="xyz_-"+SlackUtilities.createRandomNumer();
		query.add(new BasicNameValuePair("name", rename));
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getJSONObject("channel").getString("name"), rename,"name mismatch");
		Assert.assertEquals(response.getJSONObject("channel").getString("id"),channelId,"id not present");
		Assert.assertTrue(response.getBoolean("ok"),"ok mismatch");
		int count=0;
		JSONArray channels=SlackUtilities.getAllChannel(token);
		for(int i=0;i<channels.length();i++)
		{
			if(channels.getJSONObject(i).getString("id").equals(channelId))
			{
				assertEquals(channels.getJSONObject(i).getString("name"), rename,"name is not changed");
				count++;
			}
		}
		Assert.assertTrue(count==1,"Channel is not present");
	}
	
	//Bug : it should throw error as u r trying to rename with same name
	@Test(enabled=true,priority=1)
	public void renameChannelWithTheOriginalChannelName() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.RENAME_CHANNEL;
		
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));
		query.add(new BasicNameValuePair("channel", channelId));
		query.add(new BasicNameValuePair("name",name));
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getJSONObject("channel").getString("name"), name,"name mismatch");
		Assert.assertEquals(response.getJSONObject("channel").getString("id"),channelId,"id not present");
		Assert.assertTrue(response.getBoolean("ok"),"ok mismatch");
	}
	
	@Test(enabled=true,priority=3)
	public void renameChannelWithBlankToken() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.RENAME_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", ""));
		query.add(new BasicNameValuePair("channel", channelId));
		String rename="xyz_"+SlackUtilities.createRandomNumer();
		query.add(new BasicNameValuePair("name", rename));
		
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "not_authed","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
	
	@Test(enabled=true,priority=4)
	public void renameChannelWithInvalidToken() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.RENAME_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", "sdklsa@"));
		query.add(new BasicNameValuePair("channel", channelId));
		String rename="xyz_"+SlackUtilities.createRandomNumer();
		query.add(new BasicNameValuePair("name", rename));
		
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "invalid_auth","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
	
	@Test(enabled=true,priority=5)
	public void renameChannelWithNullValueToken() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.RENAME_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		String value=null;
		query.add(new BasicNameValuePair("token", value));
		query.add(new BasicNameValuePair("channel", channelId));
		String rename="xyz_"+SlackUtilities.createRandomNumer();
		query.add(new BasicNameValuePair("name", rename));
		
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "not_authed","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
	
	@Test(enabled=true,priority=6)
	public void renameChannelWithoutToken() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.RENAME_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("channel", channelId));
		String rename="xyz_"+SlackUtilities.createRandomNumer();
		query.add(new BasicNameValuePair("name", rename));
		
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "not_authed","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
	
	@Test(enabled=true)
	public void renameChannelWithBlankChannelId() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.RENAME_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));
		query.add(new BasicNameValuePair("channel", ""));
		String rename="xyz_"+SlackUtilities.createRandomNumer();
		query.add(new BasicNameValuePair("name", rename));
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "channel_not_found","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
	
	@Test(enabled=true)
	public void renameChannelInvalidStringValueChannelId() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.RENAME_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));
		query.add(new BasicNameValuePair("channel", "skldjsklf@"));
		String rename="xyz_"+SlackUtilities.createRandomNumer();
		query.add(new BasicNameValuePair("name", rename));
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "channel_not_found","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
		
	@Test(enabled=true)
	public void renameChannelInvalidIntValueChannelId() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.RENAME_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));
		query.add(new BasicNameValuePair("channel", "12"));
		String rename="xyz_"+SlackUtilities.createRandomNumer();
		query.add(new BasicNameValuePair("name", rename));
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "channel_not_found","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
	
	@Test(enabled=true)
	public void renameChannelWithNullValueChannelId() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.RENAME_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));
		String channel=null;
		query.add(new BasicNameValuePair("channel",channel));
		String rename="xyz_"+SlackUtilities.createRandomNumer();
		query.add(new BasicNameValuePair("name", rename));
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "channel_not_found","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
	
	@Test(enabled=true)
	public void renameChannelWithoutChannelId() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.RENAME_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));
		String rename="xyz_"+SlackUtilities.createRandomNumer();
		query.add(new BasicNameValuePair("name", rename));
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "internal_error","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
	
	@Test(enabled=true,priority=7)
	public void renameChannelWithBlankRename() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.RENAME_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));
		query.add(new BasicNameValuePair("channel", channelId));
		query.add(new BasicNameValuePair("name", ""));
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "invalid_name_required","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
	
	@Test(enabled=true,priority=8)
	public void renameChannelSpecialCharacterInRename() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.RENAME_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));
		query.add(new BasicNameValuePair("channel", channelId));
		query.add(new BasicNameValuePair("name", "@$#"));
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "invalid_name_specials","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
		
	@Test(enabled=true,priority=9)
	public void renameChannelWithNulValueInRename() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.RENAME_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));
		query.add(new BasicNameValuePair("channel",channelId));
		String rename=null;
		query.add(new BasicNameValuePair("name", rename));
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "invalid_name_required","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
	
	@Test(enabled=true,priority=10)
	public void renameChannelWithoutRename() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.RENAME_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));
		query.add(new BasicNameValuePair("channel",channelId));
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "invalid_name_required","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
		
	}
	
	@Test(enabled=true,priority=11)
	public void renameChannelWithUpperCaseInRename() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.RENAME_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));
		query.add(new BasicNameValuePair("channel", channelId));
		query.add(new BasicNameValuePair("name", "ETATSTA"+SlackUtilities.createRandomNumer()+"_"+SlackUtilities.createRandomNumer()));
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "invalid_name_specials","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
	
	@Test(enabled=true,priority=12)
	public void renameChannelWithUpperAndLowerCaseInRename() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.RENAME_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));
		query.add(new BasicNameValuePair("channel", channelId));
		query.add(new BasicNameValuePair("name", "ETasddfdSTA"+SlackUtilities.createRandomNumer()+"_"+SlackUtilities.createRandomNumer()));
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "invalid_name_specials","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
	
	@Test(enabled=true,priority=13)
	public void renameChannelWithRenameMoreThan80() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.RENAME_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));
		query.add(new BasicNameValuePair("channel", channelId));
		query.add(new BasicNameValuePair("name", "sasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasaasdf"));
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "invalid_name_maxlength","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
	
	@Test(enabled=true,priority=14)
	public void renameChannelWithRenameEqualTo80() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.RENAME_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));
		query.add(new BasicNameValuePair("channel", channelId));
		query.add(new BasicNameValuePair("name","ssssssasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasas"+SlackUtilities.createRandomNumer()));
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getJSONObject("channel").getString("id"),channelId,"id not present");
		Assert.assertTrue(response.getBoolean("ok"),"ok mismatch");
	}
}

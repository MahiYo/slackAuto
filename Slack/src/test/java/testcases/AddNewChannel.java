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
import org.testng.annotations.Test;

import com.configuration.PathConfiguration;
import com.utilities.RequestUtil;

public class AddNewChannel {
	public static String restUrl;
	public static String token=PathConfiguration.accesstoken;
	
	@Test(enabled=true)
	public void addNewChannelSuccessfully() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.CREATE_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));
		String name="channelnew1_-"+SlackUtilities.createRandomNumer();
		query.add(new BasicNameValuePair("name", name));
		
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getJSONObject("channel").getString("name"), name,"name mismatch");
		Assert.assertNotNull(response.getJSONObject("channel").getString("id"),"id not present");
		Assert.assertTrue(response.getJSONObject("channel").getBoolean("is_channel"), "is_channel is not enabled");
		Assert.assertFalse(response.getJSONObject("channel").getBoolean("is_group"), "is_group is enabled");
		Assert.assertTrue(response.getBoolean("ok"),"ok mismatch");
		int count=0;
		JSONArray channels=SlackUtilities.getAllChannel(token);
		for(int i=0;i<channels.length();i++)
		{
			if(channels.getJSONObject(i).getString("id").equals(response.getJSONObject("channel").getString("id")))
			{
				assertEquals(channels.getJSONObject(i).getString("name"), name,"channel is not created");
				count++;
			}
		}
		Assert.assertTrue(count==1,"Channel is not present");
		SlackUtilities.archieveChannel(token, response.getJSONObject("channel").getString("id"));
	}
	
	@Test(enabled=true)
	public void addNewChannelWithAlreadyRegisteredName() throws URISyntaxException, Exception {
		String name="channelnew_"+SlackUtilities.createRandomNumer();
		JSONObject newResponse=SlackUtilities.addNewChannel(token, name);
		restUrl=PathConfiguration.url+PathConstants.CREATE_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));
		query.add(new BasicNameValuePair("name", name));
		
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "name_taken","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
		SlackUtilities.archieveChannel(token, newResponse.getString("id"));
	}
	
	
	@Test(enabled=true)
	public void addNewChannelWithNameInUpperCase() throws URISyntaxException, Exception {
		String name="CHANNEL_"+SlackUtilities.createRandomNumer();
		restUrl=PathConfiguration.url+PathConstants.CREATE_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));
		query.add(new BasicNameValuePair("name", name.toUpperCase()));
		
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "invalid_name_specials","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
	
	@Test(enabled=true)
	public void addNewChannelWithNameMoreThan80() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.CREATE_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));
		query.add(new BasicNameValuePair("name","sasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasaasdf"));
		
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "invalid_name_maxlength","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
	
	@Test(enabled=true)
	public void addNewChannelWithNameEqualTo80() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.CREATE_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));
		query.add(new BasicNameValuePair("name","asasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasas"+SlackUtilities.createRandomNumer()));
		
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertNotNull(response.getJSONObject("channel").getString("name"),"name not present");
		Assert.assertNotNull(response.getJSONObject("channel").getString("id"),"id not present");
		Assert.assertTrue(response.getJSONObject("channel").getBoolean("is_channel"), "is_channel is not enabled");
		Assert.assertFalse(response.getJSONObject("channel").getBoolean("is_group"), "is_group is enabled");
		SlackUtilities.archieveChannel(token, response.getJSONObject("channel").getString("id"));
	}
	
	@Test(enabled=true)
	public void addNewChannelWithNameCombinationUpperLowerCase() throws URISyntaxException, Exception {
		String name="CHanNEL_"+SlackUtilities.createRandomNumer();
		restUrl=PathConfiguration.url+PathConstants.CREATE_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));
		query.add(new BasicNameValuePair("name", name.toUpperCase()));
		
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "invalid_name_specials","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
	
	@Test(enabled=true)
	public void addNewChannelWithBlankToken() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.CREATE_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", ""));
		String name="channel_"+SlackUtilities.createRandomNumer();
		query.add(new BasicNameValuePair("name", name));
		
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "not_authed","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
	
	@Test(enabled=true)
	public void addNewChannelWithInvalidToken() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.CREATE_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", "sdklsa@"));
		String name="channel_"+SlackUtilities.createRandomNumer();
		query.add(new BasicNameValuePair("name", name));
		
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "invalid_auth","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
	
	@Test(enabled=true)
	public void addNewChannelWithNullValueToken() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.CREATE_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		String value=null;
		query.add(new BasicNameValuePair("token", value));
		String name="channel_"+SlackUtilities.createRandomNumer();
		query.add(new BasicNameValuePair("name", name));
		
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "not_authed","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
	
	@Test(enabled=true)
	public void addNewChannelWithoutToken() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.CREATE_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		String name="channel_"+SlackUtilities.createRandomNumer();
		query.add(new BasicNameValuePair("name", name));
		
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "not_authed","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
	
	@Test(enabled=true)
	public void addNewChannelWithBlankName() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.CREATE_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));
		query.add(new BasicNameValuePair("name", ""));
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "invalid_name_required","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
	
	@Test(enabled=true)
	public void addNewChannelWithSpecialCharacterName() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.CREATE_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));
		query.add(new BasicNameValuePair("name", "#@@$$"));
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "invalid_name_specials","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
	
	@Test(enabled=true)
	public void addNewChannelWithNullValueName() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.CREATE_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));
		String name=null;
		query.add(new BasicNameValuePair("name",name ));
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "invalid_name_required","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
	
	@Test(enabled=true)
	public void addNewChannelWithoutName() throws URISyntaxException, Exception {
		restUrl=PathConfiguration.url+PathConstants.CREATE_CHANNEL;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Content-Type", "application/json"));
		
		List<NameValuePair> query = new ArrayList<NameValuePair>();
		query.add(new BasicNameValuePair("token", token));
		
		HttpResponse httpResponse=RequestUtil.postRequest(restUrl, headers, query, null);
		Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200,"Response code mismatch");
		JSONObject response=RequestUtil.convert_HttpResponse_to_JsonObject(httpResponse);
		Assert.assertEquals(response.getString("error"), "invalid_name_required","error mismatch");
		Assert.assertFalse(response.getBoolean("ok"),"ok mismatch");
	}
}

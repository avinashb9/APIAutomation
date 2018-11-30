package com.qa.tests;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.base.TestBase;
import com.qa.client.RestClient;
import com.qa.data.Users;

public class PutAPITest extends TestBase{
	
	TestBase testBase;
	String url;
	RestClient restClient;
	CloseableHttpResponse closebaleHttpResponse;
	
	@BeforeTest
	public void Setup(){
		testBase = new TestBase();
		url = prop.getProperty("serviceURL")+prop.getProperty("putapiURL");
	}
	
	@Test
	public void postAPITest() throws JsonGenerationException, JsonMappingException, IOException{
		restClient = new RestClient();
		HashMap<String,String> header = new HashMap();
		header.put("Content-Type", "application/json");
		
		ObjectMapper mapper = new ObjectMapper();
		Users userReq = new Users("morpheus","zion resident");
		mapper.writeValue(new File("/Users/NaveenKhunteta/Documents/APIAutomationHTTPClient/APIAutomationUsingHTTPClient/src/main/java/com/qa/data/users1.json"), userReq);
		
		String usersJsonAsString = mapper.writeValueAsString(userReq);
		System.out.println(usersJsonAsString);
		
		closebaleHttpResponse = restClient.put(url,usersJsonAsString,header);
		
		//Assert the Status Code
		int statusCode = closebaleHttpResponse.getStatusLine().getStatusCode();
		Assert.assertEquals(statusCode, RESPONSE_STATUS_CODE_200,"Status Code is not 200");
		
		//Assert the JSON Object
		String responseString = EntityUtils.toString(closebaleHttpResponse.getEntity(),"UTF-8");
		
		JSONObject JSONResponseObject = new JSONObject(responseString);
		System.out.println("The Response from PUT CALL:"+JSONResponseObject);
		
		Users userResObj = mapper.readValue(responseString, Users.class);
		
		Assert.assertTrue(userReq.getName().equals(userResObj.getName()));
		
		//Asser the Header
		Header[] allHeaders = closebaleHttpResponse.getAllHeaders();
		HashMap<String,String> headerMap = new HashMap();
		for(Header headerArray:allHeaders){
			headerMap.put(headerArray.getName(), headerArray.getValue());
		}
		
	}
}

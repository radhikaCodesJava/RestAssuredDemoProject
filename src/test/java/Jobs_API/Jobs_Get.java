package Jobs_API;








import java.util.LinkedHashMap;

import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class Jobs_Get {
	
	@BeforeClass
	public void initializations() throws Exception {
		configJobs.setup();
	}
	
	@Test
	public void getAllJobs() throws Exception, JsonProcessingException {
		
		String reqUri= configJobs.base_url+"/Jobs";
		System.out.println("reqUri :"+reqUri);
		
		Response res= RestAssured
				        .given()
				        
				        .when()
				        .get(reqUri)
				        
				        .then()
				        .extract()
				        .response();
		
		System.out.println("statuscode is :"+res.getStatusCode());
		
		Assert.assertEquals(res.getStatusCode(), 200);
		
		
		String responseString= res.getBody().asString();
		System.out.println("response String is: "+responseString); 
		
		String NewResponseReplacingNaN= responseString.replaceAll("NaN", "\"7 hours ago\"");
		System.out.println("new response String is: "+NewResponseReplacingNaN);
		//JsonSchema validation 
		 //assertThat(NewResponseReplacingNaN,matchesJsonSchemaInClasspath("jobsSchemaAll.json"));
		
		//extracting data from response json to linkedHashMap
		ObjectMapper mapper = new ObjectMapper();
		LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, String>>> parsedResponseToLinkeHashMap = 
				mapper.readValue(NewResponseReplacingNaN, new TypeReference<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String , String>>>>(){}); 
		
		System.out.println("number of keys :"+parsedResponseToLinkeHashMap.size() +"\n");
		
		//get all keys as set, printing keys-values
		Set<String> allKeys = parsedResponseToLinkeHashMap.keySet();
		//printing keys-values
		for(String Keys :allKeys){
			String value = parsedResponseToLinkeHashMap.get(Keys).toString();
			System.out.println(Keys +":"+ value+"\n");
		}
			
		//converting allKeys set to array 
		String[] keyArray = allKeys.toArray(new String[allKeys.size()]);
		for(String element: keyArray)
		System.out.println("keys are:"+ element +"\n");
		
		//printing innerkeys-value pair
		LinkedHashMap<String, LinkedHashMap<String,String>> innerKeys  =null;
		for(int i=0; i<allKeys.size(); i++) {
			 innerKeys = parsedResponseToLinkeHashMap.get(keyArray[i]);
			System.out.println("number of innerKeys :"+innerKeys.size());
			
			for(String innerKey: innerKeys.keySet()) {
				String valuein = innerKeys.get(innerKey).toString();
				System.out.println(innerKey +":" + valuein +"\n");
			}
		}
		
		// converting innerKeys from hashmap to set and printing
		Set<String> innerAllSubKeys = innerKeys.keySet();
		//printing keys-values
		//for(String subKey :innerAllSubKeys){
		//	String value = innerKeys.get(subKey).toString();
		//	System.out.println(subKey +":"+ value+"\n");
		//}
			
		//converting innerSubKeys set to array 
		String[] innerAllSubKeyArray = innerAllSubKeys.toArray(new String[innerAllSubKeys.size()]);
		for(String element: innerAllSubKeyArray)
		System.out.println("keys are:"+ element +"\n");
		
		//extracting and printing as key-value pair
		LinkedHashMap<String, String> innerKeysMap  =null;
		for(int i=0; i<innerAllSubKeys.size(); i++) {
			innerKeysMap = innerKeys.get(innerAllSubKeyArray[i]);
			System.out.println("number of innerKeys :"+innerKeysMap.size());
			
			for(String innerSubKey: innerKeysMap.keySet()) {
				String valuein = innerKeysMap.get(innerSubKey).toString();
				System.out.println(innerSubKey +":" + valuein +"\n");
			}
		}
				
		
					
	   //validating if nodes are present
		Assert.assertEquals(parsedResponseToLinkeHashMap.containsKey("data"), true);
				  Assert.assertEquals(innerKeys.containsKey("Job Id"), true);
				   Assert.assertEquals(innerKeys.containsKey("Job Title"), true);
				   Assert.assertEquals(innerKeys.containsKey("Job Location"), true);
				   Assert.assertEquals(innerKeys.containsKey("Job Type"), true);
				   Assert.assertEquals(innerKeys.containsKey("Job Posted time"), true);
				   Assert.assertEquals(innerKeys.containsKey("Job Company Name"), true);
				   Assert.assertEquals(innerKeys.containsKey("Job Description"), true); 
				   
		
		JsonPath nodes= res.jsonPath();
		//can access data through nodes also
		
	}

}

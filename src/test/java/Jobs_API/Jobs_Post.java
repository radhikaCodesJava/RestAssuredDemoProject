package Jobs_API;



import static org.junit.Assert.assertThat;

import java.util.LinkedHashMap;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import com.aventstack.extentreports.gherkin.model.Given;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class Jobs_Post {
	
	@BeforeClass
	public void initializations() throws Exception {
		configJobs.setup();
	}
	
	@Test
	public void testPostJobs(String Id, String jobTitle, String jobLocation, String jobCompanyName, String jobType, String jobPostedTime, String jobDescription) throws JsonMappingException, JsonProcessingException
	{
		String reqURI = configJobs.base_url+"/Jobs";
		
		Response res= RestAssured
				.given()
				.queryParam("Job Id", Id)
				.queryParam("Job Title", jobTitle)
				.queryParam("Job Location", jobLocation)
				.queryParam("Job Company Name", jobCompanyName)
				.queryParam("Job Type", jobType)
				.queryParam("Job Posted time", jobPostedTime)
				.queryParam("Job Description", jobDescription)
				
				.when()
				.post(reqURI)
				
				.then()
				 .assertThat()
				 .statusCode(200)
                 .extract()
                 .response();
		
		System.out.println("statuscode is :"+res.getStatusCode());
		Assert.assertEquals(res.getStatusCode(), 200);
		
		
		String responseString= res.getBody().asString();
		System.out.println("response String is: "+responseString); 
		
		String NewResponseReplacingNaN= responseString.replaceAll("NaN", "\"7 hours ago\"");
		System.out.println("new response String is: "+NewResponseReplacingNaN);
		
		//JsonSchema validation 
		 assertThat(NewResponseReplacingNaN,matchesJsonSchemaInClasspath("jobsSchemaAll.json"));
		 
		//extracting data from response json to linkedHashMap
			ObjectMapper mapper = new ObjectMapper();
			LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, String>>> parsedResponseToLinkeHashMap = 
					mapper.readValue(NewResponseReplacingNaN, new TypeReference<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String , String>>>>(){}); 
			//to Set
			Set<String> allKeys = parsedResponseToLinkeHashMap.keySet();
			
			//to Array
			String[] keyArray = allKeys.toArray(new String[allKeys.size()]);
			for(String element: keyArray)
				System.out.println("keys are:"+ element +"\n");
			
			
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
				
				//converting innerSubKeys set to array 
				String[] innerAllSubKeyArray = innerAllSubKeys.toArray(new String[innerAllSubKeys.size()]);
				
				//validating if nodes are present
				Assert.assertEquals(parsedResponseToLinkeHashMap.containsKey("data"), true);
						  Assert.assertEquals(innerKeys.containsKey("Job Id"), true);
						   Assert.assertEquals(innerKeys.containsKey("Job Title"), true);
						   Assert.assertEquals(innerKeys.containsKey("Job Location"), true);
						   Assert.assertEquals(innerKeys.containsKey("Job Type"), true);
						   Assert.assertEquals(innerKeys.containsKey("Job Posted time"), true);
						   Assert.assertEquals(innerKeys.containsKey("Job Company Name"), true);
						   Assert.assertEquals(innerKeys.containsKey("Job Description"), true); 
						   
						 //*******Fetch the key of the last record to assert post************
						   String lastKey = null;
						   for (String s : innerAllSubKeys) {
							    lastKey = s;
							}
						   
						   System.out.println("last key is :"+lastKey);
						   
						   //asserting last key data
						  // Assert.assertEquals(innerKeys.get(lastKey).toString(), Id);
						 //  Assert.assertEquals(innerAllSubKeys.get("Job Id"), null);
						   
						   /*Assert.assertEquals(JobTitleMap.get(lastId),title);
							 Assert.assertEquals(JobCompanyMap.get(lastId),name);
							 Assert.assertEquals(JobLocationMap.get(lastId),location);
							 Assert.assertEquals(JobTypeMap.get(lastId),type);
							 Assert.assertEquals(JobTimeMap.get(lastId),time);
							 Assert.assertEquals(JobDescMap.get(lastId),desc);*/
			
	}

}

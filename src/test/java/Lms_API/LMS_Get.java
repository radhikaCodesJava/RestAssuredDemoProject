 package Lms_API;

import static org.testng.Assert.assertEquals;

import java.io.FileInputStream;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.utils.XLUtils;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import io.restassured.response.Response;
import junit.framework.Assert;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import  io.restassured.module.jsv.JsonSchemaValidator;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


public class LMS_Get {
	//configLms configlms;
	@BeforeClass
	public void initalizations() throws Exception{
		configLms.setup();
	}
	

	@Test
public void getAllPrograms() 
{   
		
		String reqURI =configLms.base_url+"/programs";
		System.out.println(reqURI);
	 Response response = RestAssured.
			                      given().
			                      auth().
			                      basic(configLms.userName,configLms.password).
			                      
			                      when().
			                      get(reqURI).
			                      
			                      then()
			                      .assertThat()
			                      .body(matchesJsonSchemaInClasspath("lmsAllProgramsSchema.json"))
			                      .body("[0]", hasKey("programDescription"))
			          		      .body("[0]", hasKey("online"))
			                      .statusCode(200)
			                      
			                      .extract()
			                      .response();
			                      //.getBody().asString().contains("programId");
	 
	 //.body("$", hasKey("programId"))
	 assertEquals(200,response.getStatusCode());
	 
	 JsonPath nodes = response.jsonPath();
	 
	 
	 System.out.println("programId of node 0 is: "+nodes.getString("[0].programId")); 
	 System.out.println("programDescription of node 0 is: "+nodes.getString("[0].programDescription")); 
	 System.out.println("program name  is: "+nodes.getString("[0].ProgramName")); 
	 System.out.println("online is :"+nodes.getString("[0].online"));
	 
	 
  }
	
	@Test(dataProvider = "getProgramData")
	public void getProgramById(String programId) {
		
		String reqURI =configLms.base_url+"/programs/"+programId;
		System.out.println(reqURI);
		
		Response response = RestAssured.
                given().
                  auth().
                  basic(configLms.userName,configLms.password).
                
                when().
                  get(reqURI)
                
                .then()
                   .assertThat()
                   .statusCode(200)
                   .extract()
                   .response();
		
		//check if body is null or id of program is tallying
		 boolean existing =response.getBody().asString().contains("programId");
		 if (!existing)
			 System.out.println("programId is not existing");
		 else {
			System.out.println("programId is existing");
			 
				  Assert.assertEquals(200, response.getStatusCode());
					
					String responseBody = response.getBody().asString();
					 System.out.println("response Body is: "+ responseBody);
					 
			       String JsonString = response.toString();
				     System.out.println("response is: "+JsonString); 
					 
				     //check programId is tallying using JsonPath
			//JsonPath nodes = new JsonPath(JsonString); = not working
				     JsonPath nodes = response.jsonPath();
					 //way1
					 String prgId =nodes.getString("programId");
					Assert.assertEquals(programId, prgId);
					//other way = nodes.get is not same as nodes.getString so don't use
					//Assert.assertEquals(programId,String.valueOf(nodes.get("programId")));
		        
										 
				// assertThat("Updating programDescription", nodes.get("programDescription"));
					// Assert.assertEquals(false, String.valueOf(nodes.get("online")));
					 
				 //response.then()
			     //  .body("programId",equalTo("7646"), "programName",equalTo("Updating ProgramName for 272"));
					 
					//JsonSchema validation without rest-assured
				 //assertThat(JsonString,matchesJsonSchemaInClasspath("lmsSingleProgramById.json"));
					 //JsonSchema validation with rest-assured
				 JsonSchemaValidator.matchesJsonSchemaInClasspath("lmsSingleProgramById.json");
									 
				 }
	}
	
	@DataProvider(name="getProgramData")
	public Object[][] getProgramData() throws Exception
	{
		int noOfRows = XLUtils.getRowCount(configLms.filePath, "Get");
		int noOfColumns = XLUtils.getCellCount(configLms.filePath, "Get", 1);
		String data[][] = new String[noOfRows][noOfColumns];
		
		for(int i = 1; i<=noOfRows; i++) {
			
			for(int j=0;j<noOfColumns;j++) {
				
				data[i-1][j]=XLUtils.GetCellData(configLms.filePath, "Get", i, j);
			}
		}
		System.out.println("data in excel sheet:");
		for(String[] eachRec: data)
			for(String value: eachRec)
			  System.out.println(value);
				System.out.println("\n\n");
		
		return data;
	}
	
}

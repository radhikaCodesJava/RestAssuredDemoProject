package Lms_API;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


import java.io.IOException;


import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.utils.XLUtils;

import io.restassured.RestAssured;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class LMS_Post{
	
	int PutRowCnt=0;
	int DelRowCnt =0;
	
	@BeforeClass
	public void intializations() throws Exception {
		configLms.setup();
	}
	
	
	
	@Test(dataProvider = "getPostData")
	public void testPostProgram(String ProgramName, String ProgramDescription, String online)throws IOException {
		String reqURI = configLms.base_url+"/programs";
		System.out.println("requri: "+reqURI);
		
		//String reqBody = you can send reqbody as string or json object
		JSONObject requestparams = new JSONObject();
		requestparams.put("programName", ProgramName);
		requestparams.put("programDescription", ProgramDescription);
		requestparams.put("online", true);
		
		
				
		Response res = RestAssured
				         .given()
				             .auth()
				             .basic(configLms.userName,configLms.password)
				             .header("Content-Type", "application/json")
				             .body(requestparams)
				             
				          .when()
				             .post(reqURI)
				             
				           .then()
				           .statusCode(200)
				          .body(matchesJsonSchemaInClasspath("lmsSingleProgramById.json"))
				           
				           .extract()
				           .response();
		
		System.out.println("statusCode is "+res.getStatusCode());
		
			String body= res.getBody().asPrettyString();
			System.out.println("body: "+body);
			
			JsonPath nodes=res.jsonPath();
			
			Assert.assertEquals(ProgramName,nodes.get("programName"));
			System.out.println("programName :"+ nodes.get("programName"));
			
			//Assert.assertEquals(nodes.getString("online"), online);
			
			
			//String progId = String.valueOf(nodes.get("programId"));
			
			String progId = nodes.getString("programId");
			System.out.println("data type of programId:"+ nodes.getString("programId").getClass().getName());
			
			
			PutRowCnt =PutRowCnt+1;
			XLUtils.SetCellData(configLms.filePath, "Put", PutRowCnt, 0, progId);
			
			DelRowCnt =DelRowCnt+1;
			XLUtils.SetCellData(configLms.filePath, "Delete", DelRowCnt, 0, progId);
		}
	
	@DataProvider(name="getPostData")
	public Object[][] getPostDataMethod() throws Exception {
		int noOfRows = XLUtils.getRowCount(configLms.filePath, "Post");
		int noOfColumns = XLUtils.getCellCount(configLms.filePath, "Post", 1);
		String data[][] = new String[noOfRows][noOfColumns];
		
		for(int i = 1; i<=noOfRows; i++) {
			
			for(int j=0;j<noOfColumns;j++) {
				
				data[i-1][j]=XLUtils.GetCellData(configLms.filePath, "Post", i, j);
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

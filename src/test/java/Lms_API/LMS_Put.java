package Lms_API;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.utils.XLUtils;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import java.io.IOException;

public class LMS_Put {
	@BeforeClass
	public void initalizations() throws Exception{
		configLms.setup();
	}
	
	@Test(dataProvider="getPutData")
	public void UpdateTest(String ProgId4mEXCEL,String ProgDesc4mEXCEL, String ProgName4mEXCEL, String Online4mEXCEL) {
		
		String reqURI = configLms.base_url+"/programs/"+ProgId4mEXCEL;
		System.out.println("reqURI :  " +reqURI);
		
		JSONObject requestparams = new JSONObject();
		
		requestparams.put("programName", ProgName4mEXCEL);
		requestparams.put("programDescription", ProgDesc4mEXCEL);
		requestparams.put("online", Online4mEXCEL);
		
		
	
		Response res= RestAssured
				         .given()
				         .auth()
				         .basic(configLms.userName, configLms.password)
				         .body(requestparams)
				         .contentType("application/json")
				         
				         .when()
				         .put(reqURI)
				         
				         .then()
				         .assertThat()
				         .statusCode(200)
				         .body(matchesJsonSchemaInClasspath("lmsSingleProgramById.json"))
				         .extract()
				         .response();
		
				        Boolean contains=res.getBody().asString().contains(ProgId4mEXCEL);
				        if(contains)
				       System.out.println("programId :"+ProgId4mEXCEL+" exists");
				        else
				        	System.out.println("progrId :"+ProgId4mEXCEL+" not exists");
				        
				       JsonPath nodes= res.jsonPath();
				        
				        Assert.assertEquals(ProgDesc4mEXCEL,nodes.getString("programDescription"));
				        Assert.assertEquals(ProgName4mEXCEL ,nodes.getString("programName"));
				        Assert.assertEquals(Online4mEXCEL,nodes.getString("online").toUpperCase());
				        
				        
		}
	
	@DataProvider(name="getPutData")
	public Object[][] getPutData() throws IOException{
		
		int noOfRows = XLUtils.getRowCount(configLms.filePath, "Put");
		int noOfColumns = XLUtils.getCellCount(configLms.filePath, "Put", 1);
		String data[][] = new String[noOfRows][noOfColumns];
		
		for(int i = 1; i<=noOfRows; i++) {
			
			for(int j=0;j<noOfColumns;j++) {
				
				data[i-1][j]=XLUtils.GetCellData(configLms.filePath, "Put", i, j);
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

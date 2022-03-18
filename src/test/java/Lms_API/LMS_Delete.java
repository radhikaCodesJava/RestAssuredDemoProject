package Lms_API;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.utils.XLUtils;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class LMS_Delete {
	String reqUri;
	
	@BeforeClass
	public void initalizations() throws Exception{
		configLms.setup();
	}
	
	@Test(dataProvider = "getDeleteData")	
	public void DeleteTest(String programId)
	{
		reqUri= configLms.base_url+"/programs/"+programId;
		System.out.println("reqUri for Delete"+ reqUri);
		
		Response res= RestAssured
				        .given()
				        .auth()
				        .basic(configLms.userName, configLms.password)
				        
				        .when()
				          .delete(reqUri)
				          
				          .then()
				          .assertThat()
				          .statusCode(200)
				          .extract()
				          .response();
		
		String responseBody = res.getBody().asString();
		Assert.assertEquals(responseBody, "");
		

				        
		
	}
	
	@DataProvider(name="getDeleteData")
	Object[] getData() throws Exception
	{
		int rowNum = XLUtils.getRowCount(configLms.filePath, "Delete");
		String [] data = new String [rowNum];
		for(int i=1;i<=rowNum;i++)
			
			{
				data[i-1] = XLUtils.GetCellData(configLms.filePath, "Delete", i, 0);
				System.out.println(data[i-1]);
			}
		
		System.out.println("data in excel sheet:");
		for(String value: data)
			  System.out.println(value);
				//System.out.println("\n\n");
		
		return data;
	}

}

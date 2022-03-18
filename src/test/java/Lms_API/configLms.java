package Lms_API;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.testng.annotations.BeforeClass;

public class configLms {
	
	protected static String base_url;
	protected static String reqURI;
	protected static String userName;
	protected static String password;
	//private static int port;
    static Properties properties ;
	static String filePath;
	
		
	
	//@BeforeClass
	    public static void setup() throws Exception {
	    	
	        	properties = new Properties();
	        	
			FileInputStream FIS = new FileInputStream(
					"/Users/balum/eclipse-workspace/restassuredDemo/src/test/resources/lmsProperties.properties");
			//"User.dir"+src\\test\\resources\\lmsProperties.properties
			properties.load(FIS);
		      System.out.println("Reading all properties from the file");
		      
			try {
				properties.load(FIS);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			base_url = properties.getProperty("lms_base_url");
			userName = properties.getProperty("USERNAME");
			password = properties.getProperty("PASSWORD");
			filePath = properties.getProperty("EXCEL_PATH");
			
			
			System.out.println(userName);
			System.out.println(password);
			System.out.println(base_url);
			System.out.println(filePath);
	    }
	}



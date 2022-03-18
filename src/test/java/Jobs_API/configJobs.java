package Jobs_API;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class configJobs {
	
	 static Properties properties ;
		static String filePath;
		static String base_url;
		
	public static void setup() throws Exception {
    	
    	properties = new Properties();
    	
	FileInputStream FIS = new FileInputStream(
          "/Users/balum/eclipse-workspace/restassuredDemo/src/test/resources/jobsProperties.properties");
			
	  properties.load(FIS);
      System.out.println("Reading all properties from the file");
      
	try {
		properties.load(FIS);
	} catch (IOException e) {
		e.printStackTrace();
	}
	
	base_url = properties.getProperty("jobs_base_url");
	
	filePath = properties.getProperty("EXCEL_PATH");
	
	
	System.out.println(base_url);
	System.out.println(filePath);
}

}

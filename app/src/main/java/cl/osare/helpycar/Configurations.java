package cl.osare.helpycar;

public class Configurations {
	
	public static final String PACKAGE_NAME = "cl.osare.helpycar";
	
	//URL
	//public static final String SERVER_GET = "http://dicomtec.cl/getData.php";
	//public static final String SERVER_SET = "http://dicomtec.cl/setData.php";
	public static final String SERVER_GET = "http://192.168.0.57/sites/helpycar/getData.php";
	public static final String SERVER_SET = "http://192.168.0.57/sites/helpycar/setData.php";
	
	//Database
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "LocalDB";
	public static final String DATABASE_PATH = "//data/data/"+PACKAGE_NAME+"/databases/"+DATABASE_NAME;

}

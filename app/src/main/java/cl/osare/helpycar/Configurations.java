package cl.osare.helpycar;

public class Configurations {
	
	public static final String PACKAGE_NAME = "cl.osare.helpycar";
	
	//URL
	//public static final String SERVER_GET = "http://dicomtec.cl/getData.php";
	//public static final String SERVER_SET = "http://dicomtec.cl/setData.php";
	public static final String SERVER = "http://190.100.152.2/sites/helpycar/";
	public static final String SERVER_GET = SERVER+"getData.php";
	public static final String SERVER_SET = SERVER+"setData.php";
	public static final String SERVER_LOGOS = SERVER+"logos/";
	public static final String SERVER_PHOTOS = SERVER+"photos/";
	
	//Database
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "LocalDB";
	public static final String DATABASE_PATH = "//data/data/"+PACKAGE_NAME+"/databases/"+DATABASE_NAME;

}

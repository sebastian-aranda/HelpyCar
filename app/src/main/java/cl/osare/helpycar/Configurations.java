package cl.osare.helpycar;

public class Configurations {
	
	public static final String PACKAGE_NAME = "cl.osare.helpycar";
	
	//URL
	public static final String SERVER_GET = "http://dicomtec.cl/getData.php";
	public static final String SERVER_SET = "http://dicomtec.cl/setData.php";
	
	//Local Types
	public static final int TYPE_BENCINA = 1;
	public static final int TYPE_ACCESORIOS = 2;
	public static final int TYPE_GRUA = 3;
	public static final int TYPE_LAVADO = 4;
	public static final int TYPE_MOTO = 5;
	public static final int TYPE_MECANICO = 6;
	public static final int TYPE_MECANICO_ADOMICILIO = 7;
	public static final int TYPE_VULCANIZACION = 8;
	public static final int TYPE_AUTOMOTORA = 9;
	public static final int TYPE_POLICIA = 10;
	public static final int TYPE_ESTACIONAMIENTO = 11;
	public static final int TYPE_RENT_A_CAR = 12;
	public static final int TYPE_AMBULANCIA = 13;
	public static final int TYPE_BOMBEROS = 14;
	
	
	//Others
	
	//Database
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "LocalDB";
	public static final String DATABASE_PATH = "//data/data/"+PACKAGE_NAME+"/databases/"+DATABASE_NAME;

}

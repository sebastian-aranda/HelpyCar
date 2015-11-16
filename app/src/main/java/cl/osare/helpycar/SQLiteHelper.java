package cl.osare.helpycar;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
	
	private static final int DATABASE_VERSION = Configurations.DATABASE_VERSION;
	private static final String DATABASE_NAME = Configurations.DATABASE_NAME;
	private static final String DATABASE_PATH = Configurations.DATABASE_PATH;

	public SQLiteHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
    public void onCreate(SQLiteDatabase db) {
        reloadLocales(db);
        reloadVersion(db);
        reloadCalificaciones(db);
        
    }
	
	public SQLiteDatabase getDatabase(){
		SQLiteDatabase db = this.getReadableDatabase();
		return db;
	}
	
	public boolean checkDataBase() {
	    SQLiteDatabase checkDB = null;
	    try {
	        checkDB = SQLiteDatabase.openDatabase(DATABASE_PATH, null,SQLiteDatabase.OPEN_READONLY);
	        checkDB.close();
	    } catch (SQLiteException e) {
	        // database doesn't exist yet.
	    }
	    return checkDB != null ? true : false;
	}
	
	@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.onCreate(db);
    }

	//Reload Configurations
	public void reloadLocales(SQLiteDatabase db){
		db.execSQL("DROP TABLE IF EXISTS locales");

		String sql = "CREATE TABLE locales ( " +
				"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"nombre TEXT, "+
				"localizacion TEXT, "+
				"direccion INTEGER, "+
				"telefono TEXT, "+
				"horario TEXT, "+
				"mail TEXT, "+
				"logo TEXT, "+
				"photo TEXT, "+
				"descripcion TEXT, "+
				"premium TEXT )";

		db.execSQL(sql);
	}

	public void reloadVersion(SQLiteDatabase db){
		db.execSQL("DROP TABLE IF EXISTS version");

		String sql = "CREATE TABLE version (" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"comentario TEXT )";

		db.execSQL(sql);
	}

	public void reloadCalificaciones(SQLiteDatabase db){
		db.execSQL("DROP TABLE IF EXISTS calificaciones");
		

		String create_calificaciones = "CREATE TABLE calificaciones (" +
        		"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        		"dispositivo TEXT, " +
        		"id_local INTEGER, " +
        		"nota INTEGER )";
		
		db.execSQL(create_calificaciones);
	}

	//inserting functions
	public void addVersion(int id, String comentario) {
		SQLiteDatabase db = this.getWritableDatabase();		

		ContentValues values = new ContentValues();
		values.put("id", id);
		values.put("comentario", comentario);

		db.insert("version", null, values);

		db.close();
	}
	
	public void addCalificacion(int id, String dispositivo, int id_local, int nota){
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("id", id);
		values.put("dispositivo", dispositivo);
		values.put("id_local", id_local);
		values.put("nota", nota);

		db.insert("calificaciones", null, values);

		db.close();
	}

	/* addCalificacion es para poblar la tabla desde el servidor externo, addNewCalificacion es para agregar una calificacion realizada por el usuario*/
	public void addNewCalificacion(String dispositivo, int id_local, int nota){
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("dispositivo", dispositivo);
		values.put("id_local", id_local);
		values.put("nota", nota);

		db.insert("calificaciones", null, values);

		db.close();
	}
	
	public void addLocal(Local local){
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("nombre", local.getNombre());
		values.put("localizacion", local.getLocalizacion());
		values.put("direccion", local.getDireccion());
		values.put("telefono", local.getTelefono());
		values.put("horario", local.getHorario());
		values.put("mail", local.getMail());
		values.put("logo", local.getLogo());
		values.put("photo", local.getPhoto());
		values.put("descripcion", local.getDescripcion());
		values.put("premium", local.getPremium());

		db.insert("locales", null, values);

		db.close();
	}

	//getting functions
	public Local getLocal(int id){
	    SQLiteDatabase db = this.getReadableDatabase();

		String query = "SELECT * FROM locales WHERE id = "+String.valueOf(id);
		Cursor cursor = db.rawQuery(query, null);

	    if (cursor != null)
	        cursor.moveToFirst();

	    Local local = new Local();
	    local.setId(Integer.parseInt(cursor.getString(0)));
	    local.setNombre(cursor.getString(1));
	    local.setLocalizacion(cursor.getString(2));
	    local.setDireccion(cursor.getString(3));
	    local.setTelefono(cursor.getString(4));
	    local.setHorario(cursor.getString(5));
	    local.setMail(cursor.getString(6));
		local.setLogo(cursor.getString(7));
		local.setPhoto(cursor.getString(8));
	    local.setDescripcion(cursor.getString(9));
		local.setPremium(Integer.parseInt(cursor.getString(10)));

	    return local;
	}
	
	public ArrayList<Local> getLocalesByTipo(int tipo){
		ArrayList<Local> locales = new ArrayList<Local>();
		
		String query = "SELECT * FROM locales WHERE tipo = "+String.valueOf(tipo);
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		
		if (cursor.moveToFirst()) {
           do {
        	   Local local = new Local();
        	   local.setId(Integer.parseInt(cursor.getString(0)));
        	   local.setNombre(cursor.getString(1));
        	   local.setLocalizacion(cursor.getString(2));
        	   local.setTipo(Integer.parseInt(cursor.getString(3)));
        	   local.setDireccion(cursor.getString(4));
        	   local.setTelefono(cursor.getString(5));
        	   local.setHorario(cursor.getString(6));
        	   local.setMail(cursor.getString(7));
        	   local.setDescripcion(cursor.getString(8));
 
               locales.add(local);
           } while (cursor.moveToNext());
        }
		
		return locales;
	}
	
	public int getCalificacion(int id_local){
		
		String query = "SELECT * FROM calificaciones WHERE id_local = ?";
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(id_local)});
		
		int suma = 0;
		int cantidad = 0;
		float promedio = -1;
		
		
		if (cursor.moveToFirst()) {
           do {
        	   suma += Integer.parseInt(cursor.getString(3));
        	   cantidad++;
           } while (cursor.moveToNext());
        }
		
		if (cantidad != 0)
			promedio = (float) suma/cantidad;
		
		return Math.round(promedio);
	}
	
	public int getCalificacionByDispositivo(String dispositivo, int id_local){
		
		String query = "SELECT * FROM calificaciones WHERE dispositivo = ? AND id_local = ?";
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(query, new String[] { dispositivo, String.valueOf(id_local) });
	    
	    int nota = -1;
	    if (cursor.moveToFirst())
	    	nota = Integer.parseInt(cursor.getString(3));
	    	
	    return nota;
	}
	
	public int updateCalificacion(String dispositivo, int id_local, int nota) {
		 
	    // 1. get reference to writable DB
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    // 2. create ContentValues to add key "column"/value
	    ContentValues values = new ContentValues();
	    values.put("nota", nota);
	 
	    // 3. updating row
	    int i = db.update("calificaciones", //table
	            values, // column/value
	            " dispositivo = ? AND id_local = ?", // selections
	            new String[] { dispositivo, String.valueOf(id_local) }); //selection args
	 
	    // 4. close
	    db.close();
	 
	    return i;
	}
	
	public List<Local> getAllLocales() {
       List<Local> locales = new LinkedList<Local>();
 
       // 1. build the query
       String query = "SELECT  * FROM locales";
 
       // 2. get reference to writable DB
       SQLiteDatabase db = this.getWritableDatabase();
       Cursor cursor = db.rawQuery(query, null);
 
       // 3. go over each row, build book and add it to list
       if (cursor.moveToFirst()) {
           do {
        	   Local local = new Local();
        	   local.setId(Integer.parseInt(cursor.getString(0)));
        	   local.setNombre(cursor.getString(1));
        	   local.setLocalizacion(cursor.getString(2));
        	   local.setTipo(Integer.parseInt(cursor.getString(3)));
        	   local.setDireccion(cursor.getString(4));
        	   local.setTelefono(cursor.getString(5));
        	   local.setHorario(cursor.getString(6));
        	   local.setMail(cursor.getString(7));
        	   local.setDescripcion(cursor.getString(8));
 
               locales.add(local);
           } while (cursor.moveToNext());
       }
 
       //Log.d("getAllBooks()", books.toString());
 
       // return books
       return locales;
	}
	
	public int countLocales(){
	    
		String query = "SELECT  COUNT(*) AS count FROM locales";
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(query, null);
	    
	    if (cursor != null)
	        cursor.moveToFirst();
	 
	    return Integer.parseInt(cursor.getString(0));
	}
	
	public String[] getVersion(){
	    
		String query = "SELECT * FROM version";
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(query, null);
	    
	    if (cursor != null)
	        cursor.moveToFirst();
	 
	    String[] version = {cursor.getString(0), cursor.getString(1)};
	    return version;
	}
	
	public int updateLocal(Local local) {
		 
	    // 1. get reference to writable DB
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    // 2. create ContentValues to add key "column"/value
	    ContentValues values = new ContentValues();
	    values.put(COLUMNS_LOCALES[1], local.getNombre());
		values.put(COLUMNS_LOCALES[2], local.getLocalizacion());
		values.put(COLUMNS_LOCALES[3], local.getTipo());
		values.put(COLUMNS_LOCALES[4], local.getDireccion());
		values.put(COLUMNS_LOCALES[5], local.getTelefono());
		values.put(COLUMNS_LOCALES[6], local.getHorario());
		values.put(COLUMNS_LOCALES[7], local.getMail());
		values.put(COLUMNS_LOCALES[8], local.getDescripcion());
	 
	    // 3. updating row
	    int i = db.update("locales", //table
	            values, // column/value
	            " id = ?", // selections
	            new String[] { String.valueOf(local.getId()) }); //selection args
	 
	    // 4. close
	    db.close();
	 
	    return i;
	}
	
	public void deleteLocal(Local local) { 
	    
	    // 1. get reference to writable DB
	    SQLiteDatabase db = this.getWritableDatabase();

	    // 2. delete
	    db.delete("locales", //table name
	                " id = ?",  // selections
	                new String[] { String.valueOf(local.getId()) }); //selections args

	    // 3. close
	    db.close();

	    //log
	    //Log.d("deleteBook", book.toString());
	}
	

}

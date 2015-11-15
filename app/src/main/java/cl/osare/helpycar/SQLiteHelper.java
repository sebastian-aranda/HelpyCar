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
	
	private static final String[] COLUMNS_LOCALES = {"id", "nombre", "localizacion", "tipo", "direccion", "telefono", "horario", "mail", "descripcion"};

	public SQLiteHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String create_locales = "CREATE TABLE locales ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, "+
                "localizacion TEXT, "+
                "tipo INTEGER, "+
                "direccion INTEGER, "+
                "telefono TEXT, "+
                "horario TEXT, "+
                "mail TEXT, "+
                "descripcion TEXT )";
        
        String create_version = "CREATE TABLE version (" +
        		"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        		"comentario TEXT )";
 
        db.execSQL(create_locales);
        db.execSQL(create_version);
        
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
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS locales");
        db.execSQL("DROP TABLE IF EXISTS version");
        
        // create fresh books table
        this.onCreate(db);
    }
	
	public void reloadCalificaciones(SQLiteDatabase db){
		//Droping Table
		db.execSQL("DROP TABLE IF EXISTS calificaciones");
		
		//Creating table
		String create_calificaciones = "CREATE TABLE calificaciones (" +
        		"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        		"dispositivo TEXT, " +
        		"id_local INTEGER, " +
        		"nota INTEGER )";
		
		db.execSQL(create_calificaciones);
	}
	
	public void addVersion(int id, String comentario) {
		
		SQLiteDatabase db = this.getWritableDatabase();		
		
		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put("id", id);
		values.put("comentario", comentario);
				
		// 3. insert
		db.insert("version", // table
	        null, //nullColumnHack
	        values); // key/value -> keys = column names/ values = column values
		
		// 4. close
		db.close();
	}
	
	public void addCalificacion(int id, String dispositivo, int id_local, int nota){
		
		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		
		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put("id", id);
		values.put("dispositivo", dispositivo);
		values.put("id_local", id_local);
		values.put("nota", nota);
		
		// 3. insert
		db.insert("calificaciones", // table
		        null, //nullColumnHack
		        values); // key/value -> keys = column names/ values = column values
		
		// 4. close
		db.close();
	}
	
	public void addNewCalificacion(String dispositivo, int id_local, int nota){
		
		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		
		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put("dispositivo", dispositivo);
		values.put("id_local", id_local);
		values.put("nota", nota);
		
		// 3. insert
		db.insert("calificaciones", // table
		        null, //nullColumnHack
		        values); // key/value -> keys = column names/ values = column values
		
		// 4. close
		db.close();
	}
	
	public void addLocal(Local local){
        //for logging
		//Log.d("addBook", book.toString());
		
		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		
		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put("nombre", local.getNombre());
		values.put("localizacion", local.getLocalizacion());
		values.put("tipo", local.getTipo());
		values.put("direccion", local.getDireccion());
		values.put("telefono", local.getTelefono());
		values.put("horario", local.getHorario());
		values.put("mail", local.getMail());
		values.put("descripcion", local.getDescripcion());
		
		// 3. insert
		db.insert("locales", // table
		        null, //nullColumnHack
		        values); // key/value -> keys = column names/ values = column values
		
		// 4. close
		db.close();
	}
	
	public Local getLocal(int id){
	    // 1. get reference to readable DB
	    SQLiteDatabase db = this.getReadableDatabase();
	 
	    // 2. build query
	    Cursor cursor =
	            db.query("locales", // a. table
	            COLUMNS_LOCALES, // b. column names
	            " id = ?", // c. selections
	            new String[] { String.valueOf(id) }, // d. selections args
	            null, // e. group by
	            null, // f. having
	            null, // g. order by
	            null); // h. limit
	 
	    // 3. if we got results get the first one
	    
	    if (cursor != null)
	        cursor.moveToFirst();
	 
	    // 4. build book object
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
	    
	    //log
	    //Log.d("getBook("+id+")", book.toString());
	 
	    // 5. return book
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

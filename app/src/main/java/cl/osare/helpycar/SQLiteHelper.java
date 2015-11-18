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
		reloadRubrosLocales(db);
		reloadCalificaciones(db);
		reloadVersion(db);
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

	public void reloadRubrosLocales(SQLiteDatabase db){
		db.execSQL("DROP TABLE IF EXISTS rubros_locales");


		String sql = "CREATE TABLE rubros_locales(" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"id_local INTEGER, " +
				"id_rubro INTEGER )";

		db.execSQL(sql);
	}

	public void reloadCalificaciones(SQLiteDatabase db){
		db.execSQL("DROP TABLE IF EXISTS calificaciones");
		

		String sql = "CREATE TABLE calificaciones (" +
        		"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        		"dispositivo TEXT, " +
        		"id_local INTEGER, " +
        		"nota INTEGER )";
		
		db.execSQL(sql);
	}

	public void reloadVersion(SQLiteDatabase db){
		db.execSQL("DROP TABLE IF EXISTS version");

		String sql = "CREATE TABLE version (" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"comentario TEXT )";

		db.execSQL(sql);
	}

	//inserting functions
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

	public void addRubroLocal(int id, int id_local, int id_rubro){
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("id", id);
		values.put("id_local", id_local);
		values.put("id_rubro", id_rubro);

		db.insert("rubros_locales", null, values);

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

	public void addVersion(int id, String comentario) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("id", id);
		values.put("comentario", comentario);

		db.insert("version", null, values);

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

	public ArrayList<Local> getAllLocales() {
		ArrayList<Local> locales = new ArrayList<Local>();

		String query = "SELECT  * FROM locales";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		if (cursor.moveToFirst()) {
			do {
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

				locales.add(local);
			} while (cursor.moveToNext());
		}

		return locales;
	}

	public ArrayList<Local> getLocalesByRubro(int rubro) {
		ArrayList<Local> locales = new ArrayList<Local>();

		String query = "SELECT * FROM locales l INNER JOIN rubros_locales rl ON l.id = rl.id_local WHERE rl.id_rubro = "+String.valueOf(rubro);

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		if (cursor.moveToFirst()) {
			do {
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

				locales.add(local);
			} while (cursor.moveToNext());
		}

		return locales;
	}
	
	public ArrayList<Integer> getRubrosLocal(int local){
		ArrayList<Integer> rubros = new ArrayList<Integer>();
		
		String query = "SELECT id_rubro FROM rubros_locales WHERE id_local = "+String.valueOf(local);
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		
		if (cursor.moveToFirst()) {
           do {
               rubros.add(Integer.parseInt(cursor.getString(0)));
           } while (cursor.moveToNext());
        }
		
		return rubros;
	}

	public ArrayList<Integer> getLocalesRubro(int id_rubro){
		ArrayList<Integer> locales = new ArrayList<Integer>();

		String query = "SELECT id_local FROM rubros_locales WHERE id_rubro = "+String.valueOf(id_rubro);

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		if (cursor.moveToFirst()) {
			do {
				locales.add(Integer.parseInt(cursor.getString(0)));
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
	
	public int getCalificacionByDispositivo(String dispositivo, int id_local) {
		String query = "SELECT * FROM calificaciones WHERE dispositivo = ? AND id_local = ?";

		SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(query, new String[] { dispositivo, String.valueOf(id_local) });
	    
	    int nota = -1;
	    if (cursor.moveToFirst())
	    	nota = Integer.parseInt(cursor.getString(3));
	    	
	    return nota;
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

	//UPDATE
	public int updateLocal(Local local) {
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

		int i = db.update("locales", values, " id = ?", new String[] { String.valueOf(local.getId()) });

		db.close();
		return i;
	}

	public int updateCalificacion(String dispositivo, int id_local, int nota) {
	    SQLiteDatabase db = this.getWritableDatabase();

	    ContentValues values = new ContentValues();
	    values.put("nota", nota);

	    int i = db.update("calificaciones", values, " dispositivo = ? AND id_local = ?", new String[]{dispositivo, String.valueOf(id_local)});

	    db.close();
	    return i;
	}

	//DELETE
	public void deleteLocal(Local local) {
		SQLiteDatabase db = this.getWritableDatabase();

		db.delete("locales", " id = ?", new String[] { String.valueOf(local.getId()) });

		db.close();
	}

	//Other SQL Querys
	public int countLocales(){
		String query = "SELECT  COUNT(*) AS count FROM locales";
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(query, null);
	    
	    if (cursor != null)
	        cursor.moveToFirst();
	 
	    return Integer.parseInt(cursor.getString(0));
	}
}

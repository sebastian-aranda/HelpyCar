package cl.osare.helpycar;
 
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Window;
import android.widget.Toast;
 
public class SplashScreenActivity extends Activity {
	
	private static final long SPLASH_SCREEN_DELAY = 2000;
  	private static final String url_get = Configurations.SERVER_GET;
  	private static SQLiteHelper db;

  	@Override
  	protected void onResume() {
  		super.onResume();
  	}

	/*protected void onResume() {
		AlertDialog dialog = setInternetDialog();
	*/
  	
  	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        db = new SQLiteHelper(this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
 
        setContentView(R.layout.splash_screen);
        
        if (isOnline()){
        	new GetVersionTask().execute(new ApiConnector());
			//openRegisterActivity();
        }
        else{
        	AlertDialog dialog = setInternetDialog();
        }        
    }

	private class GetVersionTask extends AsyncTask<ApiConnector,Long,JSONArray> {
		protected JSONArray doInBackground(ApiConnector... params) {
			return params[0].getAllData(url_get+"?version=1");
		}

		protected void onPostExecute(JSONArray jsonArray) {
			if (compareVersions(jsonArray, db))
				new GetCalificacionesTask().execute(new ApiConnector());
			else
				new GetLocalesTask().execute(new ApiConnector());
		}
	}
    
    private class GetLocalesTask extends AsyncTask<ApiConnector,Long,JSONArray> {
        protected JSONArray doInBackground(ApiConnector... params) {
			return params[0].getAllData(url_get+"?locales=1");
        }

        protected void onPostExecute(JSONArray jsonArray) {
            loadLocalesDB(jsonArray, db);
            new GetRubrosTask().execute(new ApiConnector());
        }
    }

	private class GetRubrosTask extends AsyncTask<ApiConnector,Long,JSONArray> {
		protected JSONArray doInBackground(ApiConnector... params) {
			return params[0].getAllData(url_get+"?rubros=1");
		}

		protected void onPostExecute(JSONArray jsonArray) {
			loadRubrosLocalesDB(jsonArray, db);
			new GetCalificacionesTask().execute(new ApiConnector());
		}
	}
    
    private class GetCalificacionesTask extends AsyncTask<ApiConnector,Long,JSONArray> {   
    	
		@Override
        protected JSONArray doInBackground(ApiConnector... params) {
            return params[0].getAllData(url_get+"?calificaciones=1");
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            loadCalificacionesDB(jsonArray, db);
            openMainActivity();
        }
    }

	private boolean compareVersions(JSONArray jsonArray, SQLiteHelper db){
		int versionInternal;
		String comentarioInternal;

		if (db.checkDataBase()){
			String[] version = db.getVersion();
			versionInternal = Integer.parseInt(version[0]);
			comentarioInternal = version[1];
		}
		else{
			versionInternal = 0;
			comentarioInternal = "Sin base de datos";
		}

		int versionExternal = versionInternal;
		String comentarioExternal = comentarioInternal;
		try {
			versionExternal = jsonArray.getJSONObject(0).getInt("id");
			comentarioExternal = jsonArray.getJSONObject(0).getString("comentario");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		if (versionInternal == versionExternal){
			return true;
		}

		db.onUpgrade(db.getDatabase(), 1, 1);
		db.addVersion(versionExternal, comentarioExternal);
		Toast.makeText(this, "Base de datos actualizada \nVersion: "+versionExternal+"\nComentario: "+comentarioExternal, Toast.LENGTH_LONG).show();

		return false;
	}

    private void loadLocalesDB(JSONArray jsonArray, SQLiteHelper db) {
        for(int i=0; i<jsonArray.length();i++){
            JSONObject json = null;
            try {
                json = jsonArray.getJSONObject(i);
                Local local = new Local(json.getInt("id"), json.getString("nombre"), 
                		json.getString("localizacion"), json.getString("direccion"),
						json.getString("telefono"), json.getString("horario"),
						json.getString("mail"), json.getString("logo"),
						json.getString("photo"), json.getString("descripcion"),
						json.getInt("premium"));
                
                
                db.addLocal(local);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

	private void loadRubrosLocalesDB(JSONArray jsonArray, SQLiteHelper db) {
		for(int i=0; i<jsonArray.length();i++){
			JSONObject json = null;
			try {
				json = jsonArray.getJSONObject(i);
				db.addRubroLocal(json.getInt("id"), json.getInt("id_local"), json.getInt("id_rubro"));
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}
    
    private void loadCalificacionesDB(JSONArray jsonArray, SQLiteHelper db) {
    	db.reloadCalificaciones(db.getDatabase());
        
    	if (jsonArray == null)
        	return;
        
    	for(int i=0; i<jsonArray.length();i++){
            JSONObject json = null;
            try {
                json = jsonArray.getJSONObject(i);
                db.addCalificacion(json.getInt("id"), json.getString("dispositivo"), json.getInt("id_local"), json.getInt("nota"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    
    private void openMainActivity(){
    	Intent intent= new Intent().setClass(SplashScreenActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

	private void openRegisterActivity(){
		Intent intent = new Intent().setClass(SplashScreenActivity.this, RegisterActivity.class);
		startActivity(intent);
		finish();
	}
    
    private AlertDialog setInternetDialog(){
    	AlertDialog alert_dialog = new AlertDialog.Builder(this)
    		.setTitle("Sin conexión a internet")
    		.setMessage("Estar conectado a internet le permitirá tener la información más actualizada ¿Desea conectarse?")
    		.setPositiveButton("Si", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) { 
    				conectarse();
    			}
    		})
    		
    		.setNegativeButton("No", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) { 
    				if (db.checkDataBase())
    					continuar();
    				else
    					finish();
    			}
    		})
    		
    		.setIcon(android.R.drawable.ic_dialog_alert)
    		.show();
    	
    	return alert_dialog;
    }
    
    private void conectarse(){
    	ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

    	if (!mWifi.isConnected()) {
    		Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
    		startActivity(intent);
    	}
    	

    	mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    	if (!mWifi.isConnected()){
    		this.finish();
    	}
    }
    
    private void continuar(){
    	TimerTask task = new TimerTask() {
            @Override
            public void run() {
            	openMainActivity();
            }
        };
 
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }
    
 
}
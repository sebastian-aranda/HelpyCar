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
  		AlertDialog dialog = setInternetDialog();
  	}
  	
  	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        db = new SQLiteHelper(this);
        
        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
 
        setContentView(R.layout.splash_screen);
        
        if (isOnline()){
        	new GetLocalTask().execute(new ApiConnector());
        }
        else{
        	//Alerta recomendar estar conectado
        	AlertDialog dialog = setInternetDialog();
        }        
    }
    
    private class GetLocalTask extends AsyncTask<ApiConnector,Long,JSONArray> {   
    	
		@Override
        protected JSONArray doInBackground(ApiConnector... params) {
			return params[0].getAllData(url_get+"?version=1");
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            loadLocalesDB(jsonArray, db);
            new GetCalificacionesTask().execute(new ApiConnector());
            //openMainActivity();
        }
    }
    
    private class GetCalificacionesTask extends AsyncTask<ApiConnector,Long,JSONArray> {   
    	
		@Override
        protected JSONArray doInBackground(ApiConnector... params) {
            return params[0].getAllData(url_get+"?calificacion=1");
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            loadCalificacionesDB(jsonArray, db);
            openMainActivity();
        }
    }

    
    private void loadLocalesDB(JSONArray jsonArray, SQLiteHelper db) {
    	
    	int versionInternal;
    	String comentarioInternal;
    	if (db.checkDataBase()){
    		String[] version = db.getVersion();
    		versionInternal = Integer.parseInt(version[0]);
    		comentarioInternal = version[1];
    	}
    	else{
    		versionInternal = 0;
    		comentarioInternal = "";
    	}
    	
    	int versionExternal = versionInternal;
    	String comentarioExternal = comentarioInternal;
		try {
			versionExternal = jsonArray.getJSONObject(0).getInt("version_id");
			comentarioExternal = jsonArray.getJSONObject(0).getString("version_comentario");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
    	if (versionInternal == versionExternal){
    		return;
    	}
    	
    	db.onUpgrade(db.getDatabase(), 1, 1);
    	db.addVersion(versionExternal, comentarioExternal);
    	Toast.makeText(this, "Base de datos actualizada \nVersion: "+versionExternal+"\nComentario: "+comentarioExternal, Toast.LENGTH_LONG).show();
    	
        for(int i=0; i<jsonArray.length();i++){
            JSONObject json = null;
            try {
                json = jsonArray.getJSONObject(i);
                Local local = new Local(json.getInt("id"), json.getString("nombre"), 
                		json.getString("localizacion"), json.getInt("tipo"), 
                		json.getString("direccion"), json.getString("telefono"),
                		json.getString("horario"), json.getString("mail"),
                		json.getString("descripcion"));
                
                
                db.addLocal(local);
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
    	Intent mainIntent = new Intent().setClass(SplashScreenActivity.this, MainActivity.class);
        startActivity(mainIntent);
        // Close the activity so the user won't able to go back this activity pressing Back button
        finish();
    }
    
    private AlertDialog setInternetDialog(){
    	
    	AlertDialog alert_dialog = new AlertDialog.Builder(this)
    		.setTitle("Sin conexi�n a internet")
    		.setMessage("Estar conectado a internet le permitir� tener la informaci�n m�s actualizada �Desea conectarse?")
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
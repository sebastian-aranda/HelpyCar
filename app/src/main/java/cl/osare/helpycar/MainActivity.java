package cl.osare.helpycar;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;


public class MainActivity extends Activity implements 
	OnInfoWindowClickListener,
	OnMarkerClickListener{
	
	public final static String LOCAL_ID = "cl.osare.helpycar.LOCAL_ID";
	
	private static GoogleMap mMap;
	private GPSTracker gps;
	private List<Local> locales = new ArrayList<Local>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		SQLiteHelper db = new SQLiteHelper(this);
		
		//Setting buttons actions
		setButtonsClickListeners();
		
		//Map Section
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();	
		mMap.setMyLocationEnabled(true);
		mMap.setPadding(0, 0, 0, 90);
		
		mMap.setOnInfoWindowClickListener(this);
		mMap.setOnMarkerClickListener(this);
		
		//Getting Device Location
		gps = new GPSTracker(this);
	    Location location = gps.getLocation();
	    
	    //Camera Initialization
	    if (location != null){
		    LatLng coordinates = new LatLng(gps.getLatitude(), gps.getLongitude());
		    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 13));
	    }
	    else{
	    	LatLng coordinates = new LatLng(-33.437130, -70.634200); //Coordenados de Plaza Italia
	    	mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 13));
	    }
	    
	    locales = db.getAllLocales();
	    setMarkers(locales);	
	    
	    Intent intent = getIntent();
	    if (intent.hasExtra(ListMenuActivity.STORE_TYPE)){
	    	String extra = intent.getStringExtra(ListMenuActivity.STORE_TYPE);
		    int extra_value = Integer.parseInt(extra);
		    showStores(extra_value);
	    }
	    
	    else if (intent.hasExtra(LocalActivity.STORE_ID_EXTRA)){
	    	String extra = intent.getStringExtra(LocalActivity.STORE_ID_EXTRA);
		    int extra_value = Integer.parseInt(extra);
		    showStoreById(extra_value);
	    }
	  
	}
	
	public void showStoreById(int id){
		for (Local local : locales){
			local.setMarkerVisible(false);
			if (local.getId() == id){
				String[] splits = local.getLocalizacion().split(",");
	            double latitude = Double.parseDouble(splits[0]);
	            double longitude = Double.parseDouble(splits[1]);
	            LatLng coordinates = new LatLng(latitude, longitude);
				local.setMarkerVisible(true);
				local.getMarker().showInfoWindow();
				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 14));
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.menu_activity) {
			Intent intent = new Intent(this, MenuActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void setMarkers(List<Local> locales) {
		for (Local local : locales){
	    	String[] splits = local.getLocalizacion().split(",");
            double latitude = Double.parseDouble(splits[0]);
            double longitude = Double.parseDouble(splits[1]);
            
            Marker marker = mMap.addMarker(new MarkerOptions()
            	.position(new LatLng(latitude, longitude))
            	.title(local.getNombre())
            	.flat(false)
            );
            
            local.setMarker(marker);
	    }
    }
	
	public void setButtonsClickListeners(){
		ImageButton button_1 = (ImageButton)findViewById(R.id.b1);
		button_1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				showStores(Configurations.TYPE_BENCINA);
			}
		});
		
		ImageButton button_2 = (ImageButton)findViewById(R.id.b2);
		button_2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				showStores(Configurations.TYPE_LAVADO);
			}
		});
		
		ImageButton button_3 = (ImageButton)findViewById(R.id.b3);
		button_3.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				showStores(Configurations.TYPE_VULCANIZACION);
			}
		});
		
		ImageButton button_4 = (ImageButton)findViewById(R.id.b4);
		button_4.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				showStores(Configurations.TYPE_MECANICO);
			}
		});
		
		ImageButton button_5 = (ImageButton)findViewById(R.id.b5);
		button_5.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				showStores(Configurations.TYPE_ACCESORIOS);
			}
		});
	}
	
	public void showAllMarkers(){
		for (Local local : locales){
			local.setMarkerVisible(true);
		}
	}
	
	public void showStores(int type){
		for (Local local : locales){
			local.setMarkerVisible(false);
			if (local.getTipo() == type){
				local.setMarkerVisible(true);
			}
		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		int id = 0;
		for (Local local : locales){
			if (local.getMarker().getId().equals(marker.getId())){
				id = local.getId();
				break;
			}
		}
		Intent intent = new Intent(this, LocalActivity.class);
		intent.putExtra(LOCAL_ID,String.valueOf(id));
		startActivity(intent);
	}
	
	
}

package cl.osare.helpycar;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;


public class MainActivity extends AppCompatActivity implements
	OnInfoWindowClickListener,
	OnMarkerClickListener{
	
	public final static String LOCAL_ID = "cl.osare.helpycar.LOCAL_ID";
	public final static String RUBRO_CLASS = "cl.osare.helpycar.RUBRO_CLASS";
	
	private static GoogleMap mMap;
	private GPSTracker gps;

	private List<Local> locales = new ArrayList<Local>();
	private int[] filtros = {1,2,3,4,5};

	private static SQLiteHelper db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.logo);

		db = new SQLiteHelper(this);
		
		//Setting buttons actions
		setButtonsClickListeners();
		
		//Map Section
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();	
		mMap.setMyLocationEnabled(true);
		mMap.setPadding(0, 160, 0, 160);
		
		mMap.setOnInfoWindowClickListener(this);
		mMap.setOnMarkerClickListener(this);
		
		//Getting Device Location
		gps = new GPSTracker(this);
	    Location location = gps.getLocation();
	    
	    //Camera Initialization
	    if (location != null){
		    LatLng coordinates = new LatLng(gps.getLatitude(), gps.getLongitude());
		    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15));
	    }
	    else{
	    	LatLng coordinates = new LatLng(-33.437130, -70.634200); //Coordenados de Plaza Italia
	    	mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15));
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_menu_1:
				openMenuActivity(1);
				return true;

			case R.id.action_menu_2:
				openMenuActivity(2);
				return true;

			default:
				return super.onOptionsItemSelected(item);
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
				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 17));
			}
		}
	}

	public void setMarkers(List<Local> locales) {
		for (Local local : locales){
	    	String[] splits = local.getLocalizacion().split(",");
            double latitude = Double.parseDouble(splits[0]);
            double longitude = Double.parseDouble(splits[1]);

			BitmapDescriptor markerIcon;
			if (local.getPremium() == 0)
				markerIcon = BitmapDescriptorFactory.fromResource(R.drawable.marker_1);
			else if (local.getPremium() < 3)
				markerIcon = BitmapDescriptorFactory.fromResource(R.drawable.marker_2);
			else{
				markerIcon = BitmapDescriptorFactory.fromResource(R.drawable.marker_3);
			}

            Marker marker = mMap.addMarker(new MarkerOptions()
            	.position(new LatLng(latitude, longitude))
            	.title(local.getNombre())
				.icon(markerIcon));

			local.setMarker(marker);

			if (local.getPremium() >= 3){
				new DownloadMarkerLogoTask(getApplicationContext(), local, 80, 60).execute(Configurations.SERVER_MARKERS+local.getMarker_logo());
			}
	    }
    }
	
	public void setButtonsClickListeners(){
		ImageButton button_1 = (ImageButton)findViewById(R.id.b1);
		button_1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				showStores(filtros[0]);
			}
		});
		
		ImageButton button_2 = (ImageButton)findViewById(R.id.b2);
		button_2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				showStores(filtros[1]);
			}
		});
		
		ImageButton button_3 = (ImageButton)findViewById(R.id.b3);
		button_3.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				showStores(filtros[2]);
			}
		});
		
		ImageButton button_4 = (ImageButton)findViewById(R.id.b4);
		button_4.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				showStores(filtros[3]);
			}
		});
		
		ImageButton button_5 = (ImageButton)findViewById(R.id.b5);
		button_5.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				showStores(filtros[4]);
			}
		});
	}
	
	public void showAllMarkers(){
		for (Local local : locales){
			local.setMarkerVisible(true);
		}
	}
	
	public void showStores(int rubro){
		List<Integer> id_locales = db.getLocalesRubro(rubro);
		for (Local local : locales){
			local.setMarkerVisible(false);
			if (id_locales.contains(local.getId())){
				local.setMarkerVisible(true);
			}
		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
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

	private void openMenuActivity(int extra){
		Intent intent= new Intent().setClass(MainActivity.this, MenuActivity.class);
		intent.putExtra(RUBRO_CLASS,extra);
		startActivity(intent);
		//finish();
	}
	
}

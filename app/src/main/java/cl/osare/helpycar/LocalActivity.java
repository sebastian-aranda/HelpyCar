package cl.osare.helpycar;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.RawContacts;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class LocalActivity extends AppCompatActivity {
	
	private static final String url_set = Configurations.SERVER_SET;
	private static final String url_get = Configurations.SERVER_GET;
	private static SQLiteHelper db;
	private static String macAddress;
	private Local local;

	//Views
	private ProgressBar loadingLogo;
	private ImageView logo;

	private TextView nombre;
	private TextView direccion;

	private TextView horario;
	private TextView mail;
	private TextView telefono;

	private RatingBar rateBar;
	private int promedio = 0;
	private int myCalification = 0;

	private LinearLayout ofertasLayout;
	private ArrayList<Oferta> ofertas;

	private TextView descripcion;

	private ProgressBar loadingPhoto;
	private ImageView photo;

	private String denuncia;

	private InterstitialAd interstitial;

	public final static String STORE_ID_EXTRA = "cl.osare.helpycar.local.STORE_ID";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_local);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		db = new SQLiteHelper(this);
		macAddress = getMacAddress();

		interstitial = new InterstitialAd(this);
		interstitial.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice("EF5124893544330E953A1E369725D2D4")
				.build();

		interstitial.loadAd(adRequest);

		Intent intent = getIntent();
		String id = intent.getStringExtra(MainActivity.LOCAL_ID);
		this.local = db.getLocal(Integer.parseInt(id));

		this.loadingLogo = (ProgressBar)findViewById(R.id.loadingLogo);
		this.logo = (ImageView)findViewById(R.id.logo);
		new DownloadImageTask(getApplicationContext(), loadingLogo, logo, 96, 96).execute(Configurations.SERVER_LOGOS+local.getLogo());

		this.loadingPhoto = (ProgressBar)findViewById(R.id.loadingPhoto);
		this.photo = (ImageView)findViewById(R.id.photo);
		new DownloadImageTask(getApplicationContext(), loadingPhoto, photo, 96, 240).execute(Configurations.SERVER_PHOTOS+local.getPhoto());

		this.nombre = (TextView)findViewById(R.id.name);
		this.direccion = (TextView)findViewById(R.id.address);
		this.horario = (TextView)findViewById(R.id.horarioText);
		this.mail = (TextView)findViewById(R.id.mailText);
		this.telefono = (TextView)findViewById(R.id.phone_numberText);

		this.rateBar = (RatingBar)findViewById(R.id.ratingBar);
		this.promedio = db.getCalificacion(local.getId());
		this.myCalification = db.getCalificacionByDispositivo(macAddress, local.getId());

		this.descripcion = (TextView)findViewById(R.id.localDescriptionText);

		ofertasLayout = (LinearLayout) findViewById(R.id.ofertas);
		ofertas = new ArrayList<Oferta>();
		new getOfertasTask().execute(new ApiConnector());

		setValues(local);

		new setVisitaTask().execute(new ApiConnector());
		
		rateBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

				if (isOnline()) {
					//Ingresar nueva nota
					if (myCalification == -1) {
						db.addNewCalificacion(macAddress, local.getId(), Math.round(rating));
						myCalification = Math.round(rating);
						new setCalificacionTask().execute(new ApiConnector());
					} else {
						//Actualizar nota
						if (Math.round(rating) != myCalification) {
							db.updateCalificacion(macAddress, local.getId(), Math.round(rating));
							myCalification = Math.round(rating);
							new updateCalificacionTask().execute(new ApiConnector());
						}
					}
				} else {
					Toast.makeText(LocalActivity.this, "Debe estar conectado a internet para poder calificar", Toast.LENGTH_SHORT).show();
					ratingBar.setRating(promedio);
				}

			}
		});
		
		Button callingButton = (Button)findViewById(R.id.callingButton);
		callingButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:" + local.getTelefono()));
				startActivity(callIntent);*/
				if (interstitial.isLoaded()) {
					interstitial.show();
				}
			}
		});
		
		Button contactButton = (Button) findViewById(R.id.contactButton);
		contactButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String contact = addContact(LocalActivity.this, local.getNombre(), local.getDireccion(), local.getTelefono());
				if (contact != null)
					Toast.makeText(LocalActivity.this, "Usuario agregado correctamente", Toast.LENGTH_SHORT).show();
			}
		});
		
		Button mapButton = (Button) findViewById(R.id.mapButton);
		mapButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				openMap(Integer.toString(local.getId()));
			}
		});

		Button reportButton = (Button) findViewById(R.id.button_report);
		reportButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				reportDialog();
			}
		});
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}*/

	private void reportDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Denunciar Local");

		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(input);

		builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				denuncia = input.getText().toString();
				new setDenunciaTask().execute(new ApiConnector());
				Toast.makeText(getApplicationContext(), "Denuncia enviada exitósamente", Toast.LENGTH_SHORT).show();
			}
		});
		builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		builder.show();
	}
	
	private String getMacAddress(){
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo wInfo = wifiManager.getConnectionInfo();
		String macAddress = wInfo.getMacAddress(); 

		return macAddress;
	}
	
	private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
	
	public void setValues(Local local) {
        this.nombre.setText(local.getNombre());
        this.direccion.setText(local.getDireccion());
        this.horario.setText(local.getHorario());
        this.mail.setText(local.getMail());
        this.telefono.setText(local.getTelefono());
        this.descripcion.setText(local.getDescripcion());
        this.rateBar.setRating(promedio);
    }
	
	public String addContact(Activity mAcitvity, String name, String address, String number) {  
        int contactID = -1;  
       
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();  
        int rawContactID = ops.size();  
        
        // Adding insert operation to operations list  
        // to insert a new raw contact in the table ContactsContract.RawContacts  
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)  
             .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null).withValue(RawContacts.ACCOUNT_NAME, null).build());  
        
        // Adding insert operation to operations list  
        // to insert display name in the table ContactsContract.Data  
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)  
             .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)  
             .withValue(ContactsContract.Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE).withValue(StructuredName.DISPLAY_NAME, name).build());  
        
        // Adding insert operation to operations list  
        // to insert Mobile Number in the table ContactsContract.Data  
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)  
             .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)  
             .withValue(ContactsContract.Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE).withValue(Phone.NUMBER, number)  
             .withValue(Phone.TYPE, CommonDataKinds.Phone.TYPE_MOBILE).build());  
        
        // Adding insert operation to operations list  
        // to insert Mobile Number in the table ContactsContract.Data  
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)  
             .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)  
             .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)  
             .withValue(ContactsContract.CommonDataKinds.StructuredPostal.STREET, address).build());  
        
        // Adding insert operation to operations list  
        // to insert Mobile Number in the table ContactsContract.Data  
        /*ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)  
             .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)  
             .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)  
             .withValue(ContactsContract.CommonDataKinds.Note.NOTE, mNote).build());*/  
  
        /*ByteArrayOutputStream stream = new ByteArrayOutputStream();  
        mPhoto.compress(CompressFormat.JPEG, 100, stream);  
        byte[] bytes = stream.toByteArray();  
        // Adding insert operation to operations list  
        // to insert Mobile Number in the table ContactsContract.Data  
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)  
             .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)  
             .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)  
             .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, bytes).build()); */
        
        try {  
             ContentResolver mResolver = mAcitvity.getContentResolver();  
             ContentProviderResult[] mlist = mResolver.applyBatch(ContactsContract.AUTHORITY, ops);  
             Uri myContactUri = mlist[0].uri;  
             int lastSlash = myContactUri.toString().lastIndexOf("/");  
             int length = myContactUri.toString().length();  
             contactID = Integer.parseInt((String) myContactUri.toString().subSequence(lastSlash + 1, length));  
        } catch (Exception e) {  
             e.printStackTrace();  
        }  
        
        return String.valueOf(contactID);  
   }

	public void openMap(String id){
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(STORE_ID_EXTRA,id);
		startActivity(intent);
	}

	//API CONNECTIONS FUNCTIONS
	private class getOfertasTask extends AsyncTask<ApiConnector,Long,JSONArray> {
		protected JSONArray doInBackground(ApiConnector... params) {
			return params[0].getAllData(url_get+"?ofertas=1&id_local="+local.getId());
		}

		protected void onPostExecute(JSONArray jsonArray) {
			if (jsonArray != null){
				loadOfertas(jsonArray);
				OfferAdapter offerAdapter = new OfferAdapter(getApplicationContext(),ofertas);

				int count = offerAdapter.getCount();
				for (int i=0;i<count;i++){
					View item = offerAdapter.getView(i, null, null);
					ofertasLayout.addView(item);
				}
			}

		}
	}

	private class setVisitaTask extends AsyncTask<ApiConnector,Long,JSONArray> {

		@Override
		protected JSONArray doInBackground(ApiConnector... params) {
			return params[0].getAllData(url_set+"?visita=1"+"&id_local="+local.getId()+"&dispositivo="+macAddress);
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			//Nothing to do
		}
	}
	
	private class setCalificacionTask extends AsyncTask<ApiConnector,Long,JSONArray> {   
    	
		@Override
        protected JSONArray doInBackground(ApiConnector... params) {
			return params[0].getAllData(url_set+"?dispositivo="+macAddress+"&id_local="+local.getId()+"&nota="+myCalification);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
        	Toast.makeText(LocalActivity.this, "Calificacion guardada", Toast.LENGTH_SHORT).show();
        }
    }
	
	private class updateCalificacionTask extends AsyncTask<ApiConnector,Long,JSONArray> {   
    	
		@Override
        protected JSONArray doInBackground(ApiConnector... params) {
			return params[0].getAllData(url_set+"?dispositivo="+macAddress+"&id_local="+local.getId()+"&nota="+myCalification+"&update=1");
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
        	Toast.makeText(LocalActivity.this, "Calificacion guardada", Toast.LENGTH_SHORT).show();
        }
    }

	private class setDenunciaTask extends AsyncTask<ApiConnector,Long,JSONArray> {

		@Override
		protected JSONArray doInBackground(ApiConnector... params) {
			String query = "";
			try {
				query = URLEncoder.encode(denuncia, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			return params[0].getAllData(url_set+"?denuncia=1"+"&id_local="+local.getId()+"&comentario="+query);
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			//Nothing to do
		}
	}

	private void loadOfertas(JSONArray jsonArray) {
		for(int i=0; i<jsonArray.length();i++){
			JSONObject json = null;
			try {
				json = jsonArray.getJSONObject(i);
				Oferta oferta = new Oferta(json.getString("oferta"), json.getInt("precio"));
				ofertas.add(oferta);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}
}

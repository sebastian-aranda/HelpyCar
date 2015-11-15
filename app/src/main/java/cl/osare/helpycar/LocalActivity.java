package cl.osare.helpycar;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

public class LocalActivity extends Activity{
	
	private static final String url_set = Configurations.SERVER_SET;
	private static SQLiteHelper db;
	private static String macAddress;
	
	//TextViews
	private TextView name;
	private TextView address;
	private TextView phone_number;
	private TextView horarioText;
	private TextView mailText;
	private TextView localDescriptionText;
	private RatingBar rateBar;
	
	private Local local;
	private int promedio = 0;
	private int myCalification = 0;
	
	public final static String STORE_ID_EXTRA = "cl.osare.helpycar.local.STORE_ID";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_local);
		
		db = new SQLiteHelper(this);
		macAddress = getMacAddress();
		
		this.name = (TextView)findViewById(R.id.name);
		this.address = (TextView)findViewById(R.id.address);
		this.horarioText = (TextView)findViewById(R.id.horarioText);
		this.mailText = (TextView)findViewById(R.id.mailText);
		this.phone_number = (TextView)findViewById(R.id.phone_numberText);
		this.localDescriptionText = (TextView)findViewById(R.id.localDescriptionText);
		this.rateBar = (RatingBar)findViewById(R.id.ratingBar);
		
		Intent intent = getIntent();
		String id = intent.getStringExtra(MainActivity.LOCAL_ID);
		
		this.local = db.getLocal(Integer.parseInt(id));
		this.promedio = db.getCalificacion(local.getId());
		this.myCalification = db.getCalificacionByDispositivo(macAddress, local.getId());
		
		setValues(local);
		
		rateBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			public void onRatingChanged(RatingBar ratingBar, float rating,boolean fromUser) {
				
				if (isOnline()){
					//Ingresar nueva nota
					if (myCalification == -1){
						db.addNewCalificacion(macAddress, local.getId(), Math.round(rating));
						myCalification = Math.round(rating);
						new setCalificacionTask().execute(new ApiConnector());
					}
					else{
						//Actualizar nota
						if (Math.round(rating) != myCalification){
							db.updateCalificacion(macAddress, local.getId(), Math.round(rating));
							myCalification = Math.round(rating);
							new updateCalificacionTask().execute(new ApiConnector());
						}
					}
				}
				else{
					Toast.makeText(LocalActivity.this, "Debe estar conectado a internet para poder calificar", Toast.LENGTH_SHORT).show();
					ratingBar.setRating(promedio);
				}
				
			}
		});
		
		Button callingButton = (Button)findViewById(R.id.callingButton);
		callingButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:"+local.getTelefono()));
				startActivity(callIntent);
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
        this.name.setText(local.getNombre());
        this.address.setText(local.getDireccion());
        this.horarioText.setText(local.getHorario());
        this.mailText.setText(local.getMail());
        this.phone_number.setText(local.getTelefono());
        this.localDescriptionText.setText(local.getDescripcion());
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
	
	public void openMap(String id){
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(STORE_ID_EXTRA,id);
		startActivity(intent);
	}
}

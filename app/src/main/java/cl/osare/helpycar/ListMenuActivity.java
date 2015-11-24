package cl.osare.helpycar;

import java.util.ArrayList;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ListMenuActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
	
	public final static String STORE_TYPE = "cl.osare.helpycar.list.STORE_TYPE";
	private static int CURRENT_STORE_TYPE;
	
	private final Context mContext = this;
	
	private ArrayList<Local> locales;

	private ListView localListView;
	private SearchView searchView;
	private MenuItem searchMenuItem;
	private ListAdapter localListAdapter; 
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_list);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
	    
	    SQLiteHelper db = new SQLiteHelper(this);
	    
	    //Codigo para retornar tipo correcto en base a la posicion del grid view
	    Intent intent = getIntent();
	    CURRENT_STORE_TYPE = intent.getIntExtra(MenuActivity.STORE_TYPE, 0);

		locales = db.getLocalesByRubro(CURRENT_STORE_TYPE);

		localListView = (ListView) findViewById(R.id.listview);
		localListAdapter = new ListAdapter(mContext, locales);
		localListView.setAdapter(localListAdapter);
		localListView.setTextFilterEnabled(false);
		
		localListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	openLocalActivity(position);
            }
        });
	    
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.list, menu);
		
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchMenuItem = menu.findItem(R.id.search);
		searchView = (SearchView) searchMenuItem.getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		searchView.setSubmitButtonEnabled(false);
		searchView.setOnQueryTextListener(this);
	    
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		
		if (id == R.id.search){
			//doSearch();
		}
		
		else if (id == R.id.location) {
			openMap(Integer.toString(CURRENT_STORE_TYPE));
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	
	private void openLocalActivity(int position) {
		// close search view if its visible
        if (searchView.isShown()) {
            searchMenuItem.collapseActionView();
            searchView.setQuery("", false);
        }
		
		
		Intent intent = new Intent(this, LocalActivity.class);
		intent.putExtra(MainActivity.LOCAL_ID, String.valueOf(locales.get(position).getId()));
		startActivity(intent);
	}
	
	/*
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.overridePendingTransition(R.anim.stay_in, R.anim.bottom_out);
	}*/

	@Override
	public boolean onQueryTextSubmit(String query) {
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		
		localListAdapter.getFilter().filter(newText);
		
		// use to enable search view popup text
		/*if (TextUtils.isEmpty(newText)) {
			localListView.clearTextFilter();
		}
		else {
			localListView.setFilterText(newText.toString());
		}*/
		
		return true;
	}
	
	public void openMap(String id){
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(STORE_TYPE,id);
		startActivity(intent);
	}
	
}
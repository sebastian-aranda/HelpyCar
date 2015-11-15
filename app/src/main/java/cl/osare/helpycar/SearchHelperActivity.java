package cl.osare.helpycar;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class SearchHelperActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
    	super.onNewIntent(intent);
        
    	handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        
    	if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            showResults(query);
            //Toast.makeText(this, query, Toast.LENGTH_LONG).show();
        }
    }
    
    private void showResults(String query){
    	//Toast.makeText(this, query, Toast.LENGTH_LONG).show();
    }
}


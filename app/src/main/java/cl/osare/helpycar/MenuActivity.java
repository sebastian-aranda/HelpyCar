package cl.osare.helpycar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MenuActivity extends AppCompatActivity{
	public final static String STORE_TYPE = "cl.osare.helpycar.menu.STORE_TYPE";
	
	private GridView grid;

	private static int[] rubros_id;

	private final int[] rubros_id_1 = {
			1,2,3,
			4,5,6,
			7,8,9,
			10,11,12,
			13,14,15
	};

	private final int[] rubros_id_2 = {
			16,17,18,
			19,20,21,
			22,23,24,
			25,26,27,
			32,28,29,
			30
	};

	private final String[] rubros_titulo_1 = {
			"Gasolinera", "Estacionamientos", "Vulcanización",
		"Baterías", "Mecánico", "Grúas",
			"Ruedas a domicilio", "Lubricentro", "Balanceo",
			"Venta de neumáticos", "Frenos", "Mecánico a domicilio",
			"Repuestos", "Lavado", "Revisión técnica"
	};

	private final String[] rubros_titulo_2 = {
			"Desarmaduría", "Radio alarmas", "Parabrisas",
			"Pintura", "Eléctrico", "Chapas",
			"Tapizes", "Automotora", "Rent a car",
			"Motos", "Escapes", "Tunning",
			"Aire Acondicionado", "Ambulancias", "Bomberos",
			"Carabineros"
	};
	
	private final int[] rubros_icon_1 = {
			R.drawable.mdpi_gasolina, R.drawable.mdpi_estacionamiento, R.drawable.mdpi_vulca,
			R.drawable.mdpi_bateria, R.drawable.mdpi_mecanico, R.drawable.mdpi_grua,
			R.drawable.mdpi_rueda_a_domicilio, R.drawable.mdpi_lubricentro, R.drawable.mdpi_balanceo,
			R.drawable.mdpi_venta_ruedas, R.drawable.mdpi_frenos, R.drawable.mdpi_mecanico_a_domicilio,
			R.drawable.mdpi_repuestos, R.drawable.mdpi_carwash, R.drawable.mdpi_revision_tecnica
	};

	private final int[] rubros_icon_2 = {
			R.drawable.mdpi_desarmaduria, R.drawable.mdpi_radio_alarma, R.drawable.mdpi_parabrisas,
			R.drawable.mdpi_pintura, R.drawable.mdpi_electrico, R.drawable.mdpi_chapa,
			R.drawable.mdpi_tapiz, R.drawable.mdpi_automotora, R.drawable.mdpi_rent_a_car,
			R.drawable.mdpi_moto, R.drawable.mdpi_escape, R.drawable.mdpi_tuning,
			R.drawable.mdpi_aire_acondicionado, R.drawable.mdpi_ambulancia, R.drawable.mdpi_bomberos,
			R.drawable.mdpi_policia
	};
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_menu);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.logo);

		AdView mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

		Intent intent = getIntent();
		int extra = intent.getIntExtra(MainActivity.RUBRO_CLASS, 0);

		ImageAdapter adapter = null;
		if (extra == 1){
			adapter = new ImageAdapter(this, rubros_titulo_1, rubros_icon_1);
			rubros_id = rubros_id_1;

		}
		else if (extra == 2){
			adapter = new ImageAdapter(this, rubros_titulo_2, rubros_icon_2);
			rubros_id = rubros_id_2;
		}

		grid = (GridView) findViewById(R.id.gridview);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int rubro_id = rubros_id[position];
            	openList(rubro_id);
            }
        });
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}*/
	
	public void openList(int id){
		Intent intent = new Intent(this, ListMenuActivity.class);
		intent.putExtra(STORE_TYPE,id);
		startActivity(intent);
	}
	
	
	
	
}

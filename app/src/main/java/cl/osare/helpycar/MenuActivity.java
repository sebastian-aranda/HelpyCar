package cl.osare.helpycar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class MenuActivity extends ActionBarActivity{
	public final static String STORE_TYPE = "cl.osare.helpycar.menu.STORE_TYPE";
	
	private GridView grid;
	private final String[] web = {
			"Gasolinera", "Estacionamientos", "Vulcanización",
			"Baterías", "Mecánico", "Grúas",
			"Ruedas a domicilio", "Lubricentro", "Balanceo",
			"Venta de neumáticos", "Frenos", "Mecánico a domicilio",
			"Repuestos", "Lavado", "Revisión técnica",
			"Desarmaduría", "Radio alarmas", "Parabrisas",
			"Pintura", "Eléctrico", "Chapas",
			"Tapizes", "Automotora", "Rent a car",
			"Motos", "Escapes", "Tunning",
			"Aire Acondicionado", "Ambulancias", "Bomberos",
			"Carabineros"
	};
	
	private final int[] imageId = {
			R.drawable.bencina_menu, R.drawable.lavado_menu, R.drawable.carro_menu,
			R.drawable.grua_menu, R.drawable.mecanico_menu, R.drawable.mecanicodomicilio_menu,
			R.drawable.grua_menu, R.drawable.mecanico_menu, R.drawable.mecanicodomicilio_menu,
			R.drawable.grua_menu, R.drawable.mecanico_menu, R.drawable.mecanicodomicilio_menu,
			R.drawable.grua_menu, R.drawable.mecanico_menu, R.drawable.mecanicodomicilio_menu,
			R.drawable.grua_menu, R.drawable.mecanico_menu, R.drawable.mecanicodomicilio_menu,
			R.drawable.grua_menu, R.drawable.mecanico_menu, R.drawable.mecanicodomicilio_menu,
			R.drawable.grua_menu, R.drawable.mecanico_menu, R.drawable.mecanicodomicilio_menu,
			R.drawable.grua_menu, R.drawable.mecanico_menu, R.drawable.mecanicodomicilio_menu,
			R.drawable.grua_menu, R.drawable.mecanico_menu, R.drawable.mecanicodomicilio_menu,
			R.drawable.vulcanizacion_menu
	};
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_menu);

	    ImageAdapter adapter = new ImageAdapter(this, web, imageId);
	    grid = (GridView) findViewById(R.id.gridview);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	openList(Integer.toString(position));
            }
        });
	}
	
	public void openList(String id){
		Intent intent = new Intent(this, ListMenuActivity.class);
		intent.putExtra(STORE_TYPE,id);
		startActivity(intent);
	}
	
	
	
	
}

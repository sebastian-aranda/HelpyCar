package cl.osare.helpycar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

public class OfferAdapter extends BaseAdapter{

	private final Context context;
	private ArrayList<Oferta> ofertas;

	public OfferAdapter(Context context, ArrayList<Oferta> ofertas) {
		this.context = context;
		this.ofertas = ofertas;
	}
	
	@Override
	public int getCount() {
		return ofertas.size();
	}
	
	@Override
	public Oferta getItem(int i) {
		return ofertas.get(i);
	}
	
	@Override
	public long getItemId(int i) { return i; }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View listView = convertView;
		final ViewHolder holder;
		Oferta oferta = getItem(position);
		
		if (listView == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			listView = inflater.inflate(R.layout.offer_item, null);
			
			holder = new ViewHolder();
			holder.oferta = (TextView) listView.findViewById(R.id.oferta);
			holder.precio = (TextView) listView.findViewById(R.id.precio);

			listView.setTag(holder);
		}
		else{
			holder = (ViewHolder) listView.getTag();
		}
		
		holder.oferta.setText(oferta.getNombre());
		holder.precio.setText(String.valueOf(oferta.getPrecio()));
		
		return listView;
	}

    static class ViewHolder {
        TextView oferta;
        TextView precio;
    }
} 

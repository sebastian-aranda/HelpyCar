package cl.osare.helpycar;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter implements Filterable{
	
	private final Context context;
	
	private LocalFilter localFilter;
	private ArrayList<Local> originalList;
	private ArrayList<Local> filteredList;
	

	public ListAdapter(Context context, ArrayList<Local> locales) {
		this.context = context;
		this.originalList = locales;
		this.filteredList = locales;
	    
	}
	
	@Override
	public int getCount() {
		return filteredList.size();
	}
	
	@Override
	public Local getItem(int i) {
		return filteredList.get(i);
	}
	
	@Override
	public long getItemId(int i) { return i; }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View listView = convertView;
		final ViewHolder holder;
		Local local = getItem(position);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		listView = inflater.inflate(R.layout.list_item, null);
			
		holder = new ViewHolder();
		holder.localPremium = local.getPremium();
		holder.logo = (ImageView) listView.findViewById(R.id.logo);
		holder.loadingLogo = (ProgressBar) listView.findViewById(R.id.loadingLogo);
		holder.cliente = (TextView) listView.findViewById(R.id.cliente);
		holder.direccion = (TextView) listView.findViewById(R.id.direccion);
		holder.telefono = (TextView) listView.findViewById(R.id.telefono);
		holder.horario = (TextView) listView.findViewById(R.id.horario);

		listView.setTag(holder);

		new DownloadImageTask(context, holder.loadingLogo, holder.logo, 80, 80).execute(Configurations.SERVER_LOGOS+local.getLogo());
		holder.cliente.setText(local.getNombre());
		holder.direccion.setText(local.getDireccion());
		holder.telefono.setText(local.getTelefono());
		holder.horario.setText(local.getHorario());
		
		return listView;
	}

    static class ViewHolder {
		int localPremium;
		ProgressBar loadingLogo;
		ImageView logo;
        TextView cliente;
        TextView direccion;
        TextView telefono;
        TextView horario;
    }

	@Override
	public Filter getFilter() {
		if (localFilter == null) {
			localFilter = new LocalFilter();
		}

		return localFilter;
	}

	private class LocalFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint!=null && constraint.length()>0) {
                ArrayList<Local> tempList = new ArrayList<Local>();

                for (Local local : originalList) {
                    if (local.getNombre().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(local);
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            }
			else {
                filterResults.count = originalList.size();
                filterResults.values = originalList;
            }

            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<Local>) results.values;
            notifyDataSetChanged();
        }
    }
} 

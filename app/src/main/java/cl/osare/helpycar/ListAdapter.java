package cl.osare.helpycar;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter implements Filterable{
	
	private final Context context;
	
	private LocalFilter localFilter;
	private ArrayList<Local> localList;
	private ArrayList<Local> filteredList;
	

	public ListAdapter(Context context, ArrayList<Local> locales) {
		this.context = context;
		this.localList = locales;
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
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View listView = convertView;
		final ViewHolder holder;
		Local local = (Local) getItem(position);
		
		if (listView == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			listView = inflater.inflate(R.layout.list_item, null);
			
			holder = new ViewHolder();
			holder.cliente = (TextView) listView.findViewById(R.id.cliente);
			holder.direccion = (TextView) listView.findViewById(R.id.direccion);
			holder.telefono = (TextView) listView.findViewById(R.id.telefono);
			holder.horario = (TextView) listView.findViewById(R.id.horario);
			
			listView.setTag(holder);
		}
		else{
			holder = (ViewHolder) listView.getTag();
		}
		
		holder.cliente.setText(local.getNombre());
		holder.direccion.setText(local.getDireccion());
		holder.telefono.setText(local.getTelefono());
		holder.horario.setText(local.getHorario());
		
		return listView;
	}
	
	@Override
	public Filter getFilter() {
		if (localFilter == null) {
			localFilter = new LocalFilter();
		}
		
		return localFilter;
	}
	
	/**
     * Keep reference to children view to avoid unnecessary calls
     */
    static class ViewHolder {
        TextView cliente;
        TextView direccion;
        TextView telefono;
        TextView horario;
        
    }

    /**
     * Custom filter for friend list
     * Filter content in friend list according to the search text
     */
	private class LocalFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint!=null && constraint.length()>0) {
                ArrayList<Local> tempList = new ArrayList<Local>();

                // search content in friend list
                for (Local user : localList) {
                    if (user.getNombre().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(user);
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = localList.size();
                filterResults.values = localList;
            }

            return filterResults;
        }

        /**
         * Notify about filtered list to ui
         * @param constraint text
         * @param results filtered result
         */
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<Local>) results.values;
            notifyDataSetChanged();
        }
    }
} 

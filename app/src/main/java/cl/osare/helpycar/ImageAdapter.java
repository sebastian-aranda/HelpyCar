package cl.osare.helpycar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private final String[] web;
    private final int[] Imageid;
    
    public ImageAdapter(Context c ,String[] web, int[] Imageid ) {
      mContext = c;
      this.Imageid = Imageid;
      this.web = web;
    }

    public int getCount() {
        return web.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null){
        	grid = new View(mContext);
        	grid = inflater.inflate(R.layout.grid_item, null);
            
        	TextView textView = (TextView) grid.findViewById(R.id.grid_text);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
            
            textView.setText(web[position]);
            imageView.setImageResource(Imageid[position]);
        }
        else{
        	grid = (View) convertView;
        }
        
        return grid;
    }
}
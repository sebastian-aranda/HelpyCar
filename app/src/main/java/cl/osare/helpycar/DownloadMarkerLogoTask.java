package cl.osare.helpycar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.TypedValue;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.io.InputStream;

public class DownloadMarkerLogoTask extends AsyncTask<String, Void, Bitmap> {
    Context context;
    Local local;

    int height, width;

    public DownloadMarkerLogoTask(Context context, Local local, int height, int width) {
        this.context = context;

        this.local = local;

        this.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, context.getResources().getDisplayMetrics());
        this.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, context.getResources().getDisplayMetrics());
    }

    protected Bitmap doInBackground(String... urls) {

        String urldisplay = urls[0];
        Bitmap icon = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            icon = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return icon;
    }

    protected void onPostExecute(Bitmap result) {
        if (result != null)
            local.getMarker().setIcon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(result, width, height, true)));
    }
}


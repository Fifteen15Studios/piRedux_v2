package com.devcab.piredux;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by randy on 1/2/2015.
 */
public class PhotoAdapter extends SimpleAdapter {

    Context context;

    public PhotoAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to, ListView listView)
    {
        super(context, data, resource, from, to);

        this.context = context;

        for(int i = 0; i < data.size(); i++) {
            Bitmap image = (Bitmap) data.get(i).get(from);
            ImageView view = (ImageView) listView.findViewById(resource);
        }
        //this.listView = listView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        convertView = new RelativeLayout(context);
        String inflater = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.photo_list_row, parent, false);

        //MediaStore.Images.Media.getBitmap(context.getContentResolver(), arrayUri.get(i-1));

        final View parentView = super.getView(position, convertView, parent);
        final ImageView photo1 = (ImageView) parentView.findViewById(R.id.photo1);
        final ImageView photo2 = (ImageView) parentView.findViewById(R.id.photo2);

        return parentView;
    }

    protected void setImages(int position, Bitmap image1, Bitmap image2)
    {

        ArrayList<HashMap<String, ?>> item = new ArrayList<HashMap<String, ?>>();



        //ImageView view1 = (ImageView) listView.findViewById(R.id.photo1);
        //ImageView view2 = (ImageView) listView.findViewById(R.id.photo2);

        //view1.setImageBitmap(image1);
        //view2.setImageBitmap(image2);
    }
}

package com.example.carefree.carefree;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ITExpertsList extends ArrayAdapter<String>
{

    private final Activity context;
    private final String[] namelist;
    private final Float[] distance;
    private final String[] ratingvaluelist;

    public ITExpertsList(Activity context, String[] namelist,String[] ratingvaluelist, Float[] distance) {
        super(context, R.layout.oneitexpertadapter, namelist);
        this.context = context;
                this.namelist = namelist;
        this.distance=distance;
        this.ratingvaluelist=ratingvaluelist;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.oneitexpertadapter, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txtnamel);
        TextView txtdistance = (TextView) rowView.findViewById(R.id.txtdistancel);
        TextView txtrating = (TextView) rowView.findViewById(R.id.txtavgrating);

        txtTitle.setText(namelist[position]);
        txtrating.setText(ratingvaluelist[position]);
        txtdistance.setText(String.valueOf(distance[position] / 1000.00));
        return rowView;
    }
}
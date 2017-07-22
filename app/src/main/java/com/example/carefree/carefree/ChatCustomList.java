package com.example.carefree.carefree;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ChatCustomList extends ArrayAdapter<String>
{

    private final Activity context;
    private final String[] namelist;
    private final String[] ratingvaluelist;

    public ChatCustomList(Activity context, String[] namelist, String[] ratingvaluelist) {
        super(context, R.layout.simplerowchat, namelist);
        this.context = context;
                this.namelist = namelist;
        this.ratingvaluelist=ratingvaluelist;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.simplerowchat, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txtnamel);
        TextView txtdistance = (TextView) rowView.findViewById(R.id.txtdistancel);
        TextView txtrating = (TextView) rowView.findViewById(R.id.txtavgrating);

        txtTitle.setText(namelist[position]);
        txtrating.setText("");
        return rowView;
    }
}
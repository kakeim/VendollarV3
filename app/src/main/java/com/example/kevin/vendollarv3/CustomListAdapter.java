package com.example.kevin.vendollarv3;


import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<String> {

    final Activity context;
    private final ArrayList<String> itemname;
    private final ArrayList<Integer> imgid;

    public CustomListAdapter(Activity context, ArrayList<String> itemname, ArrayList<Integer> imgid) {
        super(context, R.layout.content_coupon_list, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
    }


    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.content_coupon_list, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.couponTitle);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.couponIcon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.couponDesc);

        txtTitle.setText(itemname.get(position));
        imageView.setImageResource(imgid.get(position));
        extratxt.setText("Description "+itemname.get(position));
        return rowView;

    };

}
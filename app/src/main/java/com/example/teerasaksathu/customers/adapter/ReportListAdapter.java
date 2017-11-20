package com.example.teerasaksathu.customers.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.teerasaksathu.customers.R;
import com.squareup.picasso.Picasso;

/**
 * Created by teerasaksathu on 11/16/2017 AD.
 */

public class ReportListAdapter extends BaseAdapter {

    private Context ojdContext;
    private String[] nameMarket;
    private String[] date;
    private String[] lockName;
    private String[] pictureUrl;


    public ReportListAdapter(Context ojdContext, String[] nameMarket, String[] date, String[] lockName, String[] pictureUrl) {
        this.ojdContext = ojdContext;
        this.nameMarket = nameMarket;
        this.date = date;
        this.lockName = lockName;
        this.pictureUrl = pictureUrl;


    }

    @Override
    public int getCount() {
        return lockName.length;

    }

    @Override
    public Object getItem(int i) {
        return null;

    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        LayoutInflater layoutInflater = (LayoutInflater) ojdContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View ojdView = layoutInflater.inflate(R.layout.list_item_report, viewGroup, false);


        TextView tvNameMarket = ojdView.findViewById(R.id.nememarket);
        tvNameMarket.setText(nameMarket[i]);

        TextView tvAddress = ojdView.findViewById(R.id.dateReseration);
        tvAddress.setText(date[i]);

        TextView tvAddress1 = ojdView.findViewById(R.id.lockName);
        tvAddress1.setText(lockName[i]);
//
        ImageView imageView = ojdView.findViewById(R.id.imageView);
//        Log.d("maxz", URLimage[i]);

//        Picasso.with(viewGroup.getContext())
//                    .load(R.drawable.mock_market)
//                    .into(imageView);

        try {
            Picasso.with(viewGroup.getContext())
                    .load(pictureUrl[i])
                    .placeholder(R.drawable.mock_market)
                    .into(imageView);
        } catch (IllegalArgumentException e) {
            Picasso.with(viewGroup.getContext())
                    .load(R.drawable.mock_market)
                    .into(imageView);
        }

        return ojdView;
    }
}

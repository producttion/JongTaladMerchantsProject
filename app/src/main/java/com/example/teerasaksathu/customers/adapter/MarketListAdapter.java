package com.example.teerasaksathu.customers.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.teerasaksathu.customers.R;
import com.squareup.picasso.Picasso;



/**
 * Created by teerasaksathu on 11/10/2017 AD.
 */

public class MarketListAdapter extends BaseAdapter {



    private Context ojdContext;
    private String[] nameMarket;
    private String[] URLimage = {""};
    private String[] marketAddress;
    private String[] pictureUrl;


    public MarketListAdapter(Context ojdContext, String[] nameMarket, String[] URLimage ,String[] marketAddress, String[] pictureUrl) {
        this.ojdContext = ojdContext;
        this.nameMarket = nameMarket;
        this.URLimage = URLimage;
        this.marketAddress = marketAddress;
        this.pictureUrl = pictureUrl;

    }


    @Override
    public int getCount() {
        return 1;

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
        View ojdView = layoutInflater.inflate(R.layout.list_item_market, viewGroup, false);

        TextView tvNameMarket = ojdView.findViewById(R.id.tvNameMarket);
        tvNameMarket.setText(nameMarket[i]);

        TextView tvAddress = ojdView.findViewById(R.id.tvAddress);
        tvAddress.setText(marketAddress[i]);

        ImageView imageView = ojdView.findViewById(R.id.imageView);
        Log.d("maxz", URLimage[i]);



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

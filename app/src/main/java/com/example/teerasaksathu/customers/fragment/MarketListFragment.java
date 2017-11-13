package com.example.teerasaksathu.customers.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.teerasaksathu.customers.R;
import com.example.teerasaksathu.customers.activity.LockReservationActivity;
import com.example.teerasaksathu.customers.activity.MainActivity;
import com.example.teerasaksathu.customers.adapter.MarketListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MarketListFragment extends Fragment {
    ListView marketList;
    String[] nameMarket, marketAddress;
    String[] URLimage;


    public MarketListFragment() {
        super();
    }

    public static MarketListFragment newInstance() {
        MarketListFragment fragment = new MarketListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_market_list, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        loadMarketList loadMarketList = new loadMarketList();
        loadMarketList.execute();

        marketList = rootView.findViewById(R.id.marketList);
        marketList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Intent intent = new Intent(getActivity(), LockReservationActivity.class);
                    intent.putExtra("username", MainActivity.intentUsername);
                    startActivity(intent);
                }

//                More than one market
//                if (i == 1) {
//                    Intent intent = new Intent(getActivity(), LockReservationActivity.class);
//                    intent.putExtra("username", MainActivity.intentUsername);
//                    startActivity(intent);
//                }
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    /*
     * Restore Instance State Here
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore Instance State here
        }
    }


    private class loadMarketList extends AsyncTask<Void, Void, String> {
        public static final String URL = "http://www.jongtalad.com/doc/load_market_list.php";

        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(URL)
                    .build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                } else {
                    return "Not Success - code : " + response.code();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Error - " + e.getMessage();
            }
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONArray jsonArray = new JSONArray(s);
                nameMarket = new String[jsonArray.length()];
                URLimage = new String[jsonArray.length()];
                marketAddress = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    nameMarket[i] = (jsonObject.getString("name"));
                    URLimage[i] = (jsonObject.getString("picture_url"));
                    marketAddress[i] = (jsonObject.getString("address"));
//
                    MarketListAdapter marketListAdapter = new MarketListAdapter(getActivity(), nameMarket, URLimage, marketAddress);
                    marketList.setAdapter(marketListAdapter);

                }

            } catch (JSONException e) {
                nameMarket = new String[1];
                URLimage = new String[1];
                marketAddress = new String[1];

                nameMarket[0] = "none";
                URLimage[0] = "none";
                marketAddress[0] = "none";

                e.printStackTrace();
            }
            Log.d("Uri", String.valueOf(URLimage));


        }
    }
}

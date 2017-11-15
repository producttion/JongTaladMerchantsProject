package com.example.teerasaksathu.customers.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.teerasaksathu.customers.R;
import com.example.teerasaksathu.customers.fragment.dialog.MarketRuleDialogFragment;


public class MarketRuleFragment extends Fragment {

    public MarketRuleFragment() {
        super();
    }

    public static MarketRuleFragment newInstance() {
        MarketRuleFragment fragment = new MarketRuleFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_market_rule, container, false);
        initInstances(rootView);

        MarketRuleDialogFragment marketRuleDialogFragment = new MarketRuleDialogFragment();
        marketRuleDialogFragment.show(getFragmentManager(), "marketRuleDialogFragment");
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here

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
}

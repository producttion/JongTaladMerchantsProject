package com.example.teerasaksathu.customers.fragment.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.teerasaksathu.customers.R;
import com.example.teerasaksathu.customers.fragment.LockReservationFragment;
import com.example.teerasaksathu.customers.fragment.MarketListFragment;

/**
 * Created by Naetirat on 11/15/2017.
 */

public class MarketRuleDialogFragment extends DialogFragment {

    //TODO : Change dialog layout to display rule
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_market_rule)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        getFragmentManager().beginTransaction()
                                .replace(R.id.contentContainer, LockReservationFragment.newInstance())
                                .commit();
                        dismiss();
                    }
                });
//                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // User cancelled the dialog
//                    }
//                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
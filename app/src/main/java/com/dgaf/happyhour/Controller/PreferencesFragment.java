package com.dgaf.happyhour.Controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

import com.dgaf.happyhour.R;

/**
 * Created by Sherman on 5/24/15.
 */

/* NOTE:
 * This is a place holder and is not currently used
 */
public class PreferencesFragment extends Fragment {
    public PreferencesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_preferences, container,
                false);
    }
}

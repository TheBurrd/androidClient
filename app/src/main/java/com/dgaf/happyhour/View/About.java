package com.dgaf.happyhour.View;

/**
 * Created by trentonrobison on 4/26/15.
 * Written by Sherman Cheung
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.dgaf.happyhour.R;


/* Fragment displayed when the user clicks About Us in the Navigation Drawer
 */
public class About extends Fragment implements View.OnClickListener {

    private ScrollView scrollView;
    private ImageView burrdIcon;
    private boolean displayed = false;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    //section ID acts like ID for the query search as well
    public static About newInstance(int sectionNumber) {
        About fragment = new About();
        Bundle args = new Bundle();
        //args.putInt(QUERY_DECISION, query);
        //fragment.setArguments(args);

        return fragment;
    }

    public About() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        // App icon is also a secret button
        burrdIcon = (ImageView) rootView.findViewById(R.id.burrdIcon);
        burrdIcon.setOnClickListener(this);
        //llamaButton.setAlpha(0.0f); im testing this on API < 10 uncomment for final project

        // The scroll view to set the background for
        scrollView = (ScrollView) rootView.findViewById(R.id.aboutScrollView);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.burrdIcon:
                // Show the llama when the user clicks the Burrd Icon
                if (!displayed) {
                    scrollView.setBackgroundResource(R.drawable.llama);
                    displayed = true;
                }
                // Return to default background when the user clicks the Burrd Icon
                else {
                    scrollView.setBackgroundResource(0);
                    displayed = false;
                }
        }
    }

}



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
import android.widget.Button;
import android.widget.ImageView;

import com.dgaf.happyhour.R;


/* Fragment displayed when the user clicks About Us in the Navigation Drawer
 */
public class About extends Fragment implements View.OnClickListener {

    private ImageView llamaPic;
    private boolean displayed = false;
    private boolean transparent = true;
    private Button llamaButton;

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
/*

        // Display the secret button
        llamaButton = (Button) rootView.findViewById(R.id.llamaButton);
        llamaButton.setOnClickListener(this);
        //llamaButton.setAlpha(0.0f); im testing this on API < 10 uncomment for final project

        // Display the hidden llama picture
        llamaPic = (ImageView) rootView.findViewById(R.id.llamaPic);
        llamaPic.setOnClickListener(this);
*/
        return rootView;
    }

    @Override
    public void onClick(View v) {
        /*
        switch(v.getId()) {
            case R.id.llamaButton:
                // Show the llama and the button when the user clicks the secret button
                if (!displayed) {
                    llamaPic.setVisibility(View.VISIBLE);
                    displayed = true;
                    llamaButton.setAlpha(0.2f);
                    transparent = false;
                }
                // Hide the llama again when the user clicks the secret button
                else {
                    llamaPic.setVisibility(View.INVISIBLE);
                    displayed = false;
                }
        }
        */
    }

}



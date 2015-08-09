package com.dgaf.happyhour.Controller;

/**
 * Created by trentonrobison on 4/26/15.
 * Written by Sherman Cheung
 */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dgaf.happyhour.R;


/* Fragment displayed when the user clicks AboutFragment Us in the Navigation Drawer
 */
public class AboutFragment extends Fragment implements View.OnClickListener {

    private ScrollView scrollView;
    private ImageView burrdIcon;
    private TextView aboutText;
    private boolean displayed = false;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    //section ID acts like ID for the query search as well
    public static AboutFragment newInstance(int sectionNumber) {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        //args.putInt(QUERY_DECISION, query);
        //fragment.setArguments(args);

        return fragment;
    }

    public AboutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.about, container, false);


        // App icon is also a secret button
        burrdIcon = (ImageView) rootView.findViewById(R.id.burrdIcon);
        burrdIcon.setOnClickListener(this);
        //llamaButton.setAlpha(0.0f); im testing this on API < 10 uncomment for final project

        // The scroll view to set the background for
        scrollView = (ScrollView) rootView.findViewById(R.id.aboutScrollView);

        // AboutFragment Text is the secret button
        aboutText = (TextView) rootView.findViewById(R.id.aboutText);
        aboutText.setOnClickListener(this);


        return rootView;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.burrdIcon:
                Uri uri = Uri.parse("https://youtu.be/9Gc4QTqslN4?t=1s");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;

            case R.id.aboutText:
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
                break;

        }
        
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    //disable the search icon
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (menu != null) {
            menu.findItem(R.id.action_settings).setVisible(false);
        }
    }

}



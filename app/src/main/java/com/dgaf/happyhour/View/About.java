package com.dgaf.happyhour.View;

/**
 * Created by trentonrobison on 4/26/15.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.dgaf.happyhour.R;

/*This is the fragment that our page view loads*/
public class About extends Fragment implements View.OnClickListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */

    ImageView llamaPic;
    boolean displayed = false;
    boolean transparent = true;
    Button llamaButton;

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

        llamaButton = (Button) rootView.findViewById(R.id.llamaButton);
        llamaButton.setOnClickListener(this);
        llamaButton.setAlpha(0.0f);

        llamaPic = (ImageView) rootView.findViewById(R.id.llamaPic);
        llamaPic.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.llamaButton:
                if (!displayed) {
                    llamaPic.setVisibility(View.VISIBLE);
                    displayed = true;
                    llamaButton.setAlpha(0.2f);
                    transparent = false;
                }
                else {
                    llamaPic.setVisibility(View.INVISIBLE);
                    displayed = false;
                }
        }
    }
}

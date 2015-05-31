package com.dgaf.happyhour.Model;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dgaf.happyhour.R;

import java.util.ArrayList;

/**
 * Created by Adam on 5/30/2015.
 */ /* An adapter used for the drawer list view.  Contains the actual rows for the
 * Navigation Drawer in terms of an ArrayList of NavItems
 */
public class DrawerListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<NavItem> mNavItems;
    private static final int NORMAL = 0;
    private static final int DAYS = 1;
    private static final int SEEK = 2;
    private static final int DIVIDER = 3;
    private static int seekProgress = 3;//for seekbar
    private QueryParameters queryParameters;

    public DrawerListAdapter(Context context, ArrayList<NavItem> navItems) {
        mContext = context;
        mNavItems = navItems;
        queryParameters = QueryParameters.getInstance();
    }

    @Override
    public int getCount() {
        return mNavItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mNavItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    /** Return the view that should be inflated */
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        View v = convertView;
        LayoutInflater inflater;

        switch (type) {
            // Ordinary row
            case NORMAL:
                    inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = inflater.inflate(R.layout.drawer_item, null);

                    TextView text = (TextView) v.findViewById(R.id.title);
                    ImageView icon =  (ImageView) v.findViewById(R.id.icon);

                    // Update the row with the correct title and image corresponding to the item
                    text.setText(mNavItems.get(position).mTitle);
                    icon.setImageResource(mNavItems.get(position).mIcon);
                break;

            // Row containing the days of the week
            case DAYS:
                    inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = inflater.inflate(R.layout.weekday_item, null);

                    Button monday =  (Button) v.findViewById(R.id.monday);
                    Button tuesday = (Button) v.findViewById(R.id.tuesday);
                    Button wednesday = (Button) v.findViewById(R.id.wednesday);
                    Button thursday = (Button) v.findViewById(R.id.thursday);
                    Button friday = (Button) v.findViewById(R.id.friday);
                    Button saturday = (Button) v.findViewById(R.id.saturday);
                    Button sunday =  (Button) v.findViewById(R.id.sunday);

                    //TODO Respond to when the buttons are clicked
                    monday.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(mContext, "monday was clicked", Toast.LENGTH_SHORT).show();

                        }
                    });

                    tuesday.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(mContext, "tuesday was clicked", Toast.LENGTH_SHORT).show();
                        }
                    });

                    wednesday.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(mContext, "wednesday was clicked", Toast.LENGTH_SHORT).show();
                        }
                    });

                    thursday.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(mContext, "thursday was clicked", Toast.LENGTH_SHORT).show();
                        }
                    });

                    friday.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(mContext, "friday was clicked", Toast.LENGTH_SHORT).show();
                        }
                    });

                    saturday.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(mContext, "saturday was clicked", Toast.LENGTH_SHORT).show();
                        }
                    });

                    sunday.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(mContext, "sunday was clicked", Toast.LENGTH_SHORT).show();
                        }
                    });

                break;

            // Black line that divides the navdrawer
            case DIVIDER:
                inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.line_item, null);
               break;

            // Seek bar for proximity
            case SEEK:
                inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.seekbar_item, null);

                // Set a listener for the view to disable navdrawer movements
                // when scrubbing the seekbar
                v.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    // Disallow user access to navigation drawer when they are
                    // touching the seekbar row
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        switch (action) {
                            case MotionEvent.ACTION_DOWN:
                                // Disallow ScrollView to intercept touch events.
                                v.getParent().requestDisallowInterceptTouchEvent(true);
                                break;

                            case MotionEvent.ACTION_UP:
                                // Allow ScrollView to intercept touch events.
                                v.getParent().requestDisallowInterceptTouchEvent(false);
                                break;
                        }

                        // Handle ListView touch events.
                        v.onTouchEvent(event);
                        return true;
                    }
                });

                SeekBar seekBar = (SeekBar) v.findViewById(R.id.seek_bar);
                seekBar.setProgress(seekProgress);
                final TextView seekBarText = (TextView) v.findViewById(R.id.seek_bar_text);

                // TODO what should happen when the seek bar is changed
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }


                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        seekProgress = progress;//save current seekbar value
                        seekBarText.setText(progress+" mi");
                    }

                });
                break;
        }
        return v;
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    /** Given a position of a row on the navigation drawer, return the type
     * of view that should be inflated.
     */
    public int getItemViewType(int position) {
        int type = 0;

        if (position == 0 || position == 2 || position == 3 || position == 5 || position == 8 || position == 10) {
            type = NORMAL;
        }else if (position == 1 || position == 7 || position == 9) {
            type = DIVIDER;
        }else if( position == 4 ){
            type = SEEK;
        }else if( position == 6){
            type = DAYS;
        }
        return type;
    }
}

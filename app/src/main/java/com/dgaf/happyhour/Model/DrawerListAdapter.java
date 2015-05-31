package com.dgaf.happyhour.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dgaf.happyhour.R;

import java.util.ArrayList;

// TODO Scroll bar padding
// TODO Highlight selecting either sort by rating or sort by proximity
// TODO Add our image view as the secret llama button

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

    private static boolean monSelected = false;
    private static boolean tueSelected = false;
    private static boolean wedSelected = false;
    private static boolean thuSelected = false;
    private static boolean friSelected = false;
    private static boolean satSelected = false;
    private static boolean sunSelected = false;

    private ImageView monday;
    private ImageView tuesday;
    private ImageView wednesday;
    private ImageView thursday;
    private ImageView friday;
    private ImageView saturday;
    private ImageView sunday;

    public DrawerListAdapter(Context context, ArrayList<NavItem> navItems) {
        mContext = context;
        mNavItems = navItems;
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

                monday =  (ImageView) v.findViewById(R.id.monday);
                tuesday = (ImageView) v.findViewById(R.id.tuesday);
                wednesday = (ImageView) v.findViewById(R.id.wednesday);
                thursday = (ImageView) v.findViewById(R.id.thursday);
                friday = (ImageView) v.findViewById(R.id.friday);
                saturday = (ImageView) v.findViewById(R.id.saturday);
                sunday =  (ImageView) v.findViewById(R.id.sunday);

                // Remember what button was selected from the last time
                // the nav drawer was opened.
                if (monSelected) {
                    monday.setImageResource(R.drawable.ic_monday_active);
                }
                else if (tueSelected) {
                    tuesday.setImageResource(R.drawable.ic_tuesday_active);
                }
                else if (wedSelected) {
                    wednesday.setImageResource(R.drawable.ic_wednesday_active);
                }
                else if (thuSelected) {
                    thursday.setImageResource(R.drawable.ic_thursday_active);
                }
                else if (friSelected) {
                    friday.setImageResource(R.drawable.ic_friday_active);
                }
                else if (satSelected) {
                    saturday.setImageResource(R.drawable.ic_saturday_active);
                }
                else if (sunSelected) {
                    sunday.setImageResource(R.drawable.ic_sunday_active);
                }

                monday.setOnClickListener(weekDayListener);
                tuesday.setOnClickListener(weekDayListener);
                wednesday.setOnClickListener(weekDayListener);
                thursday.setOnClickListener(weekDayListener);
                friday.setOnClickListener(weekDayListener);
                saturday.setOnClickListener(weekDayListener);
                sunday.setOnClickListener(weekDayListener);

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

                SeekBar seekBar = (SeekBar) v.findViewById(R.id.seek_bar);
                seekBar.setProgress(seekProgress);
                final TextView seekBarText = (TextView) v.findViewById(R.id.seek_bar_text);

                seekBarText.setOnTouchListener(navLockListener);
                seekBar.setOnTouchListener(navLockListener);

                // TODO what should happen when the seek bar is changed
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        //close drawer
                        //sort list
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }


                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        seekProgress = progress;//save current seekbar value
                        seekBarText.setText(progress + " mi");
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

    /** Helper method to unselect all the buttons */
    private void unselectButtons() {
        monday.setImageResource(R.drawable.ic_monday);
        tuesday.setImageResource(R.drawable.ic_tuesday);
        wednesday.setImageResource(R.drawable.ic_wednesday);
        thursday.setImageResource(R.drawable.ic_thursday);
        friday.setImageResource(R.drawable.ic_friday);
        saturday.setImageResource(R.drawable.ic_saturday);
        sunday.setImageResource(R.drawable.ic_sunday);

        monSelected = false;
        tueSelected = false;
        wedSelected = false;
        thuSelected = false;
        friSelected = false;
        satSelected = false;
        sunSelected = false;
    }

    /* Lock the navigation drawer from closing when the user is
     * changing the seekbar
     */
    private ListView.OnTouchListener navLockListener= new ListView.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    // Disallow Drawer to intercept touch events.
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    break;

                case MotionEvent.ACTION_UP:
                    // Allow Drawer to intercept touch events.
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    break;
            }

            // Handle seekbar touch events.
            v.onTouchEvent(event);
            return true;
        }
    };

    /* Listener for the weekday buttons.  Only one button can be selected at any
     * time.  The selected button will be highlighted.  When another button is
     * selected, the original button should be unhighlighted.  When a selected
     * button is clicked again, it should no longer be selected*/
    private View.OnClickListener weekDayListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch(id) {
                case R.id.monday:
                    Toast.makeText(mContext, "monday was clicked", Toast.LENGTH_SHORT).show();
                    if (!monSelected) {
                        unselectButtons();
                        ((ImageView) v).setImageResource(R.drawable.ic_monday_active);
                        monSelected = true;
                    }
                    else {
                        ((ImageView) v).setImageResource(R.drawable.ic_monday);
                        monSelected = false;
                    }
                    break;

                case R.id.tuesday:
                    Toast.makeText(mContext, "tuesday was clicked", Toast.LENGTH_SHORT).show();
                    if (!tueSelected) {
                        unselectButtons();
                        ((ImageView) v).setImageResource(R.drawable.ic_tuesday_active);
                        tueSelected = true;
                    }
                    else {
                        ((ImageView) v).setImageResource(R.drawable.ic_tuesday);
                        tueSelected = false;
                    }
                    break;

                case R.id.wednesday:
                    Toast.makeText(mContext, "wednesday was clicked", Toast.LENGTH_SHORT).show();
                    if (!wedSelected) {
                        unselectButtons();
                        ((ImageView) v).setImageResource(R.drawable.ic_wednesday_active);
                        wedSelected = true;
                    }
                    else {
                        ((ImageView) v).setImageResource(R.drawable.ic_wednesday);
                        wedSelected = false;
                    }
                    break;

                case R.id.thursday:
                    Toast.makeText(mContext, "thursday was clicked", Toast.LENGTH_SHORT).show();
                    if (!thuSelected) {
                        unselectButtons();
                        ((ImageView) v).setImageResource(R.drawable.ic_thursday_active);
                        thuSelected = true;
                    }
                    else {
                        ((ImageView) v).setImageResource(R.drawable.ic_thursday);
                        thuSelected = false;
                    }
                    break;

                case R.id.friday:
                    Toast.makeText(mContext, "friday was clicked", Toast.LENGTH_SHORT).show();
                    if (!friSelected) {
                        unselectButtons();
                        ((ImageView) v).setImageResource(R.drawable.ic_friday_active);
                        friSelected = true;
                    }
                    else {
                        ((ImageView) v).setImageResource(R.drawable.ic_friday);
                        friSelected = false;
                    }
                    break;

                case R.id.saturday:
                    Toast.makeText(mContext, "saturday was clicked", Toast.LENGTH_SHORT).show();
                    if (!satSelected) {
                        unselectButtons();
                        ((ImageView) v).setImageResource(R.drawable.ic_saturday_active);
                        satSelected = true;
                    }
                    else {
                        ((ImageView) v).setImageResource(R.drawable.ic_saturday);
                        satSelected = false;
                    }
                    break;

                case R.id.sunday:
                    Toast.makeText(mContext, "sunday was clicked", Toast.LENGTH_SHORT).show();
                    if (!sunSelected) {
                        unselectButtons();
                        ((ImageView) v).setImageResource(R.drawable.ic_sunday_active);
                        sunSelected = true;
                    }
                    else {
                        ((ImageView) v).setImageResource(R.drawable.ic_sunday);
                        sunSelected = false;
                    }
                    break;

            }
        }
    };
}



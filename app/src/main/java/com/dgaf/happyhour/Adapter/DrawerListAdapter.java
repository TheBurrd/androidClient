package com.dgaf.happyhour.Adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dgaf.happyhour.Model.QueryParameters;
import com.dgaf.happyhour.R;

import java.util.ArrayList;

// TODO Scroll bar padding
// TODO Highlight selecting either sort by rating or sort by proximity
// TODO Add our image view as the secret llama button

/**
 * Created by Adam on 5/30/2015.
 * Written by Sherman and Trent
 */
/**An adapter used for the drawer list view.  Contains the actual rows for the
 * Navigation Drawer in terms of an ArrayList of NavItems
 */
public class DrawerListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<NavItem> mNavItems;
    private static final int NORMAL = 0;
    private static final int DAYS = 1;
    private static final int SEEK = 2;
    private QueryParameters queryParameters;

    private ImageView monday;
    private ImageView tuesday;
    private ImageView wednesday;
    private ImageView thursday;
    private ImageView friday;
    private ImageView saturday;
    private ImageView sunday;
    private DrawerLayout mDrawerLayout;

    public DrawerListAdapter(Context context, ArrayList<NavItem> navItems, DrawerLayout drawerLayout) {
        mContext = context;
        mNavItems = navItems;
        mDrawerLayout = drawerLayout;
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


                monday =  (ImageView) v.findViewById(R.id.monday);
                tuesday = (ImageView) v.findViewById(R.id.tuesday);
                wednesday = (ImageView) v.findViewById(R.id.wednesday);
                thursday = (ImageView) v.findViewById(R.id.thursday);
                friday = (ImageView) v.findViewById(R.id.friday);
                saturday = (ImageView) v.findViewById(R.id.saturday);
                sunday =  (ImageView) v.findViewById(R.id.sunday);

                // Remember what button was selected from the last time
                // the nav drawer was opened.

                byte dayOfWeekMask = queryParameters.getDayOfWeekMask();

                if ((dayOfWeekMask & QueryParameters.MONDAY) != 0) {
                    monday.setImageResource(R.drawable.ic_monday_active);
                }
                if ((dayOfWeekMask & QueryParameters.TUESDAY) != 0) {
                    tuesday.setImageResource(R.drawable.ic_tuesday_active);
                }
                if ((dayOfWeekMask & QueryParameters.WEDNESDAY) != 0) {
                    wednesday.setImageResource(R.drawable.ic_wednesday_active);
                }
                if ((dayOfWeekMask & QueryParameters.THURSDAY) != 0) {
                    thursday.setImageResource(R.drawable.ic_thursday_active);
                }
                if ((dayOfWeekMask & QueryParameters.FRIDAY) != 0) {
                    friday.setImageResource(R.drawable.ic_friday_active);
                }
                if ((dayOfWeekMask & QueryParameters.SATURDAY) != 0) {
                    saturday.setImageResource(R.drawable.ic_saturday_active);
                }
                if ((dayOfWeekMask & QueryParameters.SUNDAY) != 0) {
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

            // Seek bar for proximity
            case SEEK:
                inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.seekbar, null);

                SeekBar seekBar = (SeekBar) v.findViewById(R.id.seek_bar);
                seekBar.setProgress(queryParameters.getRadiusMi()-1);
                final TextView seekBarText = (TextView) v.findViewById(R.id.seek_bar_text);
                seekBarText.setText(queryParameters.getRadiusMi()+" mi");
                seekBarText.setOnTouchListener(navLockListener);
                seekBar.setOnTouchListener(navLockListener);

                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        queryParameters.setRadiusMi(seekBar.getProgress() + 1);
                        queryParameters.notifyAllListeners();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }


                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        seekBarText.setText((progress+1) + " mi");
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

        if (position == 0 || position == 1 || position == 4 ) {
            type = NORMAL;
        }else if( position == 2 ){
            type = SEEK;
        }else if( position == 3){
            type = DAYS;
        }
        return type;
    }

    /** Helper method to unselect all the buttons */
    private void unselectButtons() {
        // Set the images to the unactive versions
        monday.setImageResource(R.drawable.ic_monday);
        tuesday.setImageResource(R.drawable.ic_tuesday);
        wednesday.setImageResource(R.drawable.ic_wednesday);
        thursday.setImageResource(R.drawable.ic_thursday);
        friday.setImageResource(R.drawable.ic_friday);
        saturday.setImageResource(R.drawable.ic_saturday);
        sunday.setImageResource(R.drawable.ic_sunday);

        // Unselect all buttons
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
    // TODO Remove string literals and add to strings.xml
    private View.OnClickListener weekDayListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            byte dayOfWeekMask = queryParameters.getDayOfWeekMask();
            switch(id) {
                case R.id.monday:
                    if ((dayOfWeekMask & QueryParameters.MONDAY) == 0) {
                        ((ImageView) v).setImageResource(R.drawable.ic_monday_active);
                        queryParameters.setDayOfWeekMask((byte) (dayOfWeekMask | QueryParameters.MONDAY));
                        ((AppCompatActivity) mContext).setTitle("Monday's Deals");
                    }
                    else {
                        ((ImageView) v).setImageResource(R.drawable.ic_monday);
                        queryParameters.setDayOfWeekMask((byte) (dayOfWeekMask & ~QueryParameters.MONDAY));
                        ((AppCompatActivity) mContext).setTitle("Today's Deals");
                    }
                    break;

                case R.id.tuesday:
                    if ((dayOfWeekMask & QueryParameters.TUESDAY) == 0) {
                        ((ImageView) v).setImageResource(R.drawable.ic_tuesday_active);
                        queryParameters.setDayOfWeekMask((byte) (dayOfWeekMask | QueryParameters.TUESDAY));
                        ((AppCompatActivity) mContext).setTitle("Tuesday's Deals");
                    }
                    else {
                        ((ImageView) v).setImageResource(R.drawable.ic_tuesday);
                        queryParameters.setDayOfWeekMask((byte) (dayOfWeekMask & ~QueryParameters.TUESDAY));
                        ((AppCompatActivity) mContext).setTitle("Today's Deals");
                    }
                    break;

                case R.id.wednesday:
                    if ((dayOfWeekMask & QueryParameters.WEDNESDAY) == 0) {
                        ((ImageView) v).setImageResource(R.drawable.ic_wednesday_active);
                        queryParameters.setDayOfWeekMask((byte) (dayOfWeekMask | QueryParameters.WEDNESDAY));
                        ((AppCompatActivity) mContext).setTitle("Wednesday's Deals");
                    }
                    else {
                        ((ImageView) v).setImageResource(R.drawable.ic_wednesday);
                        queryParameters.setDayOfWeekMask((byte) (dayOfWeekMask & ~QueryParameters.WEDNESDAY));
                        ((AppCompatActivity) mContext).setTitle("Today's Deals");
                    }
                    break;

                case R.id.thursday:
                    if ((dayOfWeekMask & QueryParameters.THURSDAY) == 0) {
                        ((ImageView) v).setImageResource(R.drawable.ic_thursday_active);
                        queryParameters.setDayOfWeekMask((byte) (dayOfWeekMask | QueryParameters.THURSDAY));
                        ((AppCompatActivity) mContext).setTitle("Thursday's Deals");
                    }
                    else {
                        ((ImageView) v).setImageResource(R.drawable.ic_thursday);
                        queryParameters.setDayOfWeekMask((byte) (dayOfWeekMask & ~QueryParameters.THURSDAY));
                        ((AppCompatActivity) mContext).setTitle("Today's Deals");
                    }
                    break;

                case R.id.friday:
                    if ((dayOfWeekMask & QueryParameters.FRIDAY) == 0) {
                        ((ImageView) v).setImageResource(R.drawable.ic_friday_active);
                        queryParameters.setDayOfWeekMask((byte) (dayOfWeekMask | QueryParameters.FRIDAY));
                        ((AppCompatActivity) mContext).setTitle("Friday's Deals");
                    }
                    else {
                        ((ImageView) v).setImageResource(R.drawable.ic_friday);
                        queryParameters.setDayOfWeekMask((byte) (dayOfWeekMask & ~QueryParameters.FRIDAY));
                        ((AppCompatActivity) mContext).setTitle("Today's Deals");
                    }
                    break;

                case R.id.saturday:
                    if ((dayOfWeekMask & QueryParameters.SATURDAY) == 0) {
                        ((ImageView) v).setImageResource(R.drawable.ic_saturday_active);
                        queryParameters.setDayOfWeekMask((byte) (dayOfWeekMask | QueryParameters.SATURDAY));
                        ((AppCompatActivity) mContext).setTitle("Saturday's Deals");
                    }
                    else {
                        ((ImageView) v).setImageResource(R.drawable.ic_saturday);
                        queryParameters.setDayOfWeekMask((byte) (dayOfWeekMask & ~QueryParameters.SATURDAY));
                        ((AppCompatActivity) mContext).setTitle("Today's Deals");
                    }
                    break;

                case R.id.sunday:
                    if ((dayOfWeekMask & QueryParameters.SUNDAY) == 0) {
                        ((ImageView) v).setImageResource(R.drawable.ic_sunday_active);
                        queryParameters.setDayOfWeekMask((byte) (dayOfWeekMask | QueryParameters.SUNDAY));
                        ((AppCompatActivity) mContext).setTitle("Monday's Deals");
                    }
                    else {
                        ((ImageView) v).setImageResource(R.drawable.ic_sunday);
                        queryParameters.setDayOfWeekMask((byte) (dayOfWeekMask & ~QueryParameters.SUNDAY));
                        ((AppCompatActivity) mContext).setTitle("Today's Deals");
                    }
                    break;
            }
            queryParameters.notifyAllListeners();
            mDrawerLayout.closeDrawers();
        }
    };
}



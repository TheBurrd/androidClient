package com.dgaf.happyhour.View;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dgaf.happyhour.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by trentonrobison on 4/26/15.
 */

/*This is the fragment that our page view loads*/
public class Restaurant extends Fragment {
    private GoogleMap map;
    static final LatLng UCSD = new LatLng(32.881122,-117.237631);
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */


    public static Restaurant newInstance(int sectionNumber) {
        Restaurant fragment = new Restaurant();
        Bundle args = new Bundle();
        return fragment;
    }

    public Restaurant() {
    }

    private void moveToCurrentLocation(LatLng currentLocation)
    {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.restaurant_fragment, container, false);

        //get support map fragment from xml
        map = ((SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map))
                .getMap();

        //create a marker
        if(map != null) {
            Marker UCSDmarker = map.addMarker(new MarkerOptions().position(UCSD)
                    .title("Here"));
            moveToCurrentLocation(UCSD);
        }

        return rootView;
    }
}

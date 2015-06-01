package com.dgaf.happyhour.Model;

/**
 * Created by Adam on 5/30/2015.
 */ /* NavItem class that defines a single row in the Navigation Drawer
 * It consists of an icon for the element and a string for the element itself
 */
public class NavItem{
    String mTitle;
    int mIcon;

    public NavItem(String title, int icon) {
        mTitle = title;
        mIcon = icon;
    }
}

package com.dgaf.happyhour.Model;

import android.widget.ImageView;

import com.dgaf.happyhour.R;

/**
 * Created by Adam on 8/1/2015.
 */
public class DealIcon {

    public static void setImageToDealCategory(ImageView imageView, DealModel deal) {
        switch (deal.getPrimaryTag().toLowerCase()) {
            case "asian":
                imageView.setImageResource(R.drawable.ic_category_asian_test);
                break;
            case "beer":
                imageView.setImageResource(R.drawable.ic_category_beer_test);
                break;
            case "burger":
                imageView.setImageResource(R.drawable.ic_category_burger_test);
                break;
            case "cocktail":
                imageView.setImageResource(R.drawable.ic_category_cocktail_test);
                break;
            case "pizza":
                imageView.setImageResource(R.drawable.ic_category_pizza_test);
                break;
            case "shot":
                imageView.setImageResource(R.drawable.ic_category_shot_test);
                break;
            case "taco":
                imageView.setImageResource(R.drawable.ic_category_taco_test);
                break;
            case "wine":
                imageView.setImageResource(R.drawable.ic_category_wine_test);
                break;
            case "wings":
                imageView.setImageResource(R.drawable.ic_category_wings_test);
                break;
            default:
                imageView.setImageResource(R.drawable.ic_category_food_test);
                break;
        }
    }
}
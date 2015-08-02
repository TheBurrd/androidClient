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
                imageView.setImageResource(R.drawable.ic_category_asian);
                break;
            case "beer":
                imageView.setImageResource(R.drawable.ic_category_beer);
                break;
            case "burger":
                imageView.setImageResource(R.drawable.ic_category_burger);
                break;
            case "cocktail":
                imageView.setImageResource(R.drawable.ic_category_cocktail);
                break;
            case "pizza":
                imageView.setImageResource(R.drawable.ic_category_pizza);
                break;
            case "shot":
                imageView.setImageResource(R.drawable.ic_category_shot);
                break;
            case "taco":
                imageView.setImageResource(R.drawable.ic_category_taco);
                break;
            case "wine":
                imageView.setImageResource(R.drawable.ic_category_wine);
                break;
            case "wings":
                imageView.setImageResource(R.drawable.ic_category_wings);
                break;
            default:
                imageView.setImageResource(R.drawable.ic_category_food);
                break;
        }
    }
}

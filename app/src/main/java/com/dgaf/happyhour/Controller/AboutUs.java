package com.dgaf.happyhour.Controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dgaf.happyhour.R;

public class AboutUs extends AppCompatActivity implements View.OnClickListener{

    private ScrollView scrollView;
    private ImageView burrdIcon;
    private TextView aboutText;
    private boolean displayed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        // App icon is also a secret button
        burrdIcon = (ImageView) findViewById(R.id.burrdIcon);
        burrdIcon.setOnClickListener(this);
        //llamaButton.setAlpha(0.0f); im testing this on API < 10 uncomment for final project

        // The scroll view to set the background for
        scrollView = (ScrollView) findViewById(R.id.aboutScrollView);

        // AboutFragment Text is the secret button
        aboutText = (TextView) findViewById(R.id.aboutText);
        aboutText.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about_us, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
}

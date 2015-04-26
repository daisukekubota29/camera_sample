package com.gmail.kubota.daisuke.camerasample;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.ImageView;

/**
 *
 * Created by daisuke on 15/04/26.
 */
public class PictureActivity extends ActionBarActivity {

    public static final String EXTRA_URI = "EXTRA_URI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_activity_picture);
        }

        Uri uri = getIntent().getParcelableExtra(EXTRA_URI);
        if (uri == null) {
            finish();
        }

        setContentView(R.layout.activity_picture);
        ImageView imageView = (ImageView) findViewById(R.id.image_view);
        imageView.setImageURI(uri);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                // ActionBarの戻るボタン
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}

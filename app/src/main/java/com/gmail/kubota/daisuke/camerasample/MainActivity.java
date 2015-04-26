package com.gmail.kubota.daisuke.camerasample;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    private static final int REQUEST_CODE_CAMERA_INTENT = 7777;
    private static final String SAVED_LAST_PICTURE_URI = "SAVED_LAST_PICTURE_URI";
    private Uri mLastPictureUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button)findViewById(R.id.start_camera_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCameraIntent();
            }
        });
        if (savedInstanceState != null) {
            mLastPictureUri = savedInstanceState.getParcelable(SAVED_LAST_PICTURE_URI);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_LAST_PICTURE_URI, mLastPictureUri);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAMERA_INTENT) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = mLastPictureUri;
                if (data != null && data.getExtras() != null) {
                    Uri dataUri = data.getExtras().getParcelable(MediaStore.EXTRA_OUTPUT);
                    if (dataUri != null) {
                        uri = dataUri;
                    }
                }
                Log.d("onActivityResult", "uri: " + uri);
                mLastPictureUri = null;
                if (uri != null) {
                    // 写真を表示するActivityを開く
                    Intent intent = new Intent(MainActivity.this, PictureActivity.class);
                    intent.putExtra(PictureActivity.EXTRA_URI, uri);
                    startActivity(intent);
                }
            } else {
                if (mLastPictureUri != null) {
                    // 削除する
                    getContentResolver().delete(mLastPictureUri, null, null);
                }
            }
        }
    }

    /**
     * カメラアプリをIntentで開く
     */
    private void startCameraIntent() {
        // 保存先
        String filename = System.currentTimeMillis() + ".jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, filename);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        mLastPictureUri =
                getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        values);

        // インストールされているカメラアプリを開く
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mLastPictureUri);

        Log.d("startCameraIntent", "uri = " + mLastPictureUri);

        startActivityForResult(intent, REQUEST_CODE_CAMERA_INTENT);
    }

}

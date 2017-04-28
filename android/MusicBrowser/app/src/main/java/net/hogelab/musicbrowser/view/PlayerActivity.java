package net.hogelab.musicbrowser.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by kobayasi on 2017/04/25.
 */

public class PlayerActivity extends BaseActivity {
    private static final String TAG = PlayerActivity.class.getSimpleName();

    public static final int REQUEST_CODE = 1;


    public static Intent newIntent(Context context) {
        return new Intent(context, PlayerActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}

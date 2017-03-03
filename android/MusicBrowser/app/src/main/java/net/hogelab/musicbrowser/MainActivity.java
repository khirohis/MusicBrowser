package net.hogelab.musicbrowser;

import net.hogelab.musicbrowser.view.AlbumListActivity;
import net.hogelab.musicbrowser.view.ArtistListActivity;
import net.hogelab.musicbrowser.view.TrackListActivity;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static final String TAG = MainActivity.class.getSimpleName();

    static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 1;

    static boolean isInitialized;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isInitialized) {
            Realm.init(getApplicationContext());

            // delete
            RealmConfiguration realmConfig = new RealmConfiguration.Builder().build();
            Realm.deleteRealm(realmConfig);

            isInitialized = true;
        }

        setContentView(net.hogelab.musicbrowser.R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(net.hogelab.musicbrowser.R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(net.hogelab.musicbrowser.R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, net.hogelab.musicbrowser.R.string.navigation_drawer_open, net.hogelab.musicbrowser.R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(net.hogelab.musicbrowser.R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        Sandbox.doMain();

        checkPermission();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(net.hogelab.musicbrowser.R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean granted = false;

        if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                granted = true;
            }
        }

        if (!granted) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
                // パーミッションをリクエストする合理的な理由あり
                // 繰り返しお願いする
                requestPermission();
            } else {
                // 「今後は確認しない」がチェックされた模様
                // なので諦める
                MyAlertDialog dialog = MyAlertDialog.newInstance("設定で許可してください", "わかった");
                dialog.show(getSupportFragmentManager(), null);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(net.hogelab.musicbrowser.R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == net.hogelab.musicbrowser.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_artists) {
            startActivity(ArtistListActivity.newIntent(this));
        } else if (id == R.id.nav_albums) {
            startActivity(AlbumListActivity.newIntent(this));
        } else if (id == R.id.nav_tracks) {
            startActivity(TrackListActivity.newIntent(this));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(net.hogelab.musicbrowser.R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


    private boolean checkPermission() {
        // パーミッション判定
        if (ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // このタイミングで shouldShowRequestPermissionRationale 判定すると再インストールしても
            // false が返り続ける
            // おそらく requestPermissions の結果が戻ってくる際にリセットされる　※要出展
            // なので判定は onRequestPermissionsResult へ
            Log.d(TAG, "shouldShowRequestPermissionRationale: " +
                    ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE));

            requestPermission();

            return false;
        }

        return true;
    }

    private void requestPermission() {
        // パーミッションをリクエスト
        String[] permissions = new String[] { READ_EXTERNAL_STORAGE };
        ActivityCompat.requestPermissions(this, permissions, READ_EXTERNAL_STORAGE_REQUEST_CODE);
    }


    public static class MyAlertDialog extends DialogFragment {

        public static MyAlertDialog newInstance(String message, String positiveButton) {
            Bundle args = new Bundle();
            args.putString("message", message);
            args.putString("positiveButton", positiveButton);

            MyAlertDialog dialog = new MyAlertDialog();
            dialog.setArguments(args);

            return dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String message = getArguments().getString("message");
            String positiveButton = getArguments().getString("positiveButton");

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Alert!");
            builder.setMessage(message);
            builder.setPositiveButton(positiveButton, null);

            return builder.create();
        }
    }
}

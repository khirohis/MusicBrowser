package net.hogelab.musicbrowser;

import net.hogelab.musicbrowser.databinding.ActivityMainBinding;
import net.hogelab.musicbrowser.view.AlbumListActivity;
import net.hogelab.musicbrowser.view.ArtistListActivity;
import net.hogelab.musicbrowser.view.GenericDialogFragment;
import net.hogelab.musicbrowser.view.TrackListActivity;
import net.hogelab.musicbrowser.viewmodel.MainRootViewModel;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static final String TAG = MainActivity.class.getSimpleName();

    static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 100;
    static final int PROMPT_SETTING_CHANGE_DIALOG_REQUEST_CODE = 200;

    static boolean isInitialized;

    private ActivityMainBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Realm DB を毎回削除してまっさらに
        if (!isInitialized) {
            Realm.init(getApplicationContext());

            // delete
            RealmConfiguration realmConfig = new RealmConfiguration.Builder().build();
            Realm.deleteRealm(realmConfig);

            isInitialized = true;
        }

        // inflate で View と Data Binding オブジェクトを生成
        // ViewModel オブジェクトを Data Binding の変数に設定
        // Root View を setContentView
        // Data Binding が各 View の参照を持っているので必要なら適宜アトリビュート設定

        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        mBinding.setViewModel(new MainRootViewModel());
        setContentView(mBinding.getRoot());

        setSupportActionBar(mBinding.appBarMain.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mBinding.drawerLayout, mBinding.appBarMain.toolbar, net.hogelab.musicbrowser.R.string.navigation_drawer_open, net.hogelab.musicbrowser.R.string.navigation_drawer_close);
        mBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mBinding.navView.setNavigationItemSelectedListener(this);

//        Sandbox.doMain();

        checkPermission();
    }

    @Override
    public void onBackPressed() {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PROMPT_SETTING_CHANGE_DIALOG_REQUEST_CODE) {
            ActivityCompat.finishAffinity(this);
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
                GenericDialogFragment.Builder builder = new GenericDialogFragment.Builder(
                        PROMPT_SETTING_CHANGE_DIALOG_REQUEST_CODE)
                        .setTitle("Alert")
                        .setMessage("設定で許可してください")
                        .setPositiveButton("わかった");
                builder.show(getSupportFragmentManager());
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

        mBinding.drawerLayout.closeDrawer(GravityCompat.START);

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
}

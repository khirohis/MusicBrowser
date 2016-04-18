package net.hogelab.musicbrowser;

import android.content.Intent;

import net.hogelab.musicbrowser.view.AlbumListActivity;
import net.hogelab.musicbrowser.view.ArtistListActivity;
import net.hogelab.musicbrowser.view.TrackListActivity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}

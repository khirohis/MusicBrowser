package net.hogelab.musicbrowser.view;

import net.hogelab.musicbrowser.R;
import net.hogelab.musicbrowser.databinding.ActivityArtistListBinding;
import net.hogelab.musicbrowser.event.EventBus;
import net.hogelab.musicbrowser.event.OpenArtistEvent;
import net.hogelab.musicbrowser.viewmodel.ArtistListRootViewModel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.squareup.otto.Subscribe;

import io.realm.Realm;

/**
 * Created by kobayasi on 2016/04/01.
 */
public class ArtistListActivity extends AppCompatActivity {
    private static final String TAG = ArtistListActivity.class.getSimpleName();


    private Realm mRealm;
    private ActivityArtistListBinding mBinding;


    public static Intent newIntent(Context context) {
        return new Intent(context, ArtistListActivity.class);
    }


    public Realm getRealm() {
        return mRealm;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRealm = Realm.getDefaultInstance();

        mBinding = ActivityArtistListBinding.inflate(getLayoutInflater());
        mBinding.setViewModel(new ArtistListRootViewModel());
        setContentView(mBinding.getRoot());

        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_container, ArtistListFragment.newInstance())
                    .commit();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        EventBus.getBus().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getBus().unregister(this);

        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (mRealm != null) {
            mRealm.close();
            mRealm = null;
        }

        super.onDestroy();
    }


    @Subscribe
    public void openArtist(OpenArtistEvent event) {
        startActivity(AlbumListActivity.newIntent(this, event.artistId));
    }
}

package net.hogelab.musicbrowser.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.hogelab.musicbrowser.R;
import net.hogelab.musicbrowser.databinding.ActivityAlbumListBinding;
import net.hogelab.musicbrowser.viewmodel.AlbumListRootViewModel;

/**
 * Created by kobayasi on 2016/04/11.
 */
public class AlbumListActivity extends AppCompatActivity {
    private static final String TAG = AlbumListActivity.class.getSimpleName();


    private ActivityAlbumListBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityAlbumListBinding.inflate(getLayoutInflater());
        mBinding.setViewModel(new AlbumListRootViewModel());
        setContentView(mBinding.getRoot());

        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (savedInstanceState == null) {
            AlbumListFragment fragment =  new AlbumListFragment();

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                Bundle args = new Bundle();
                args.putLong("artistId", extras.getLong("artistId"));
                fragment.setArguments(args);
            }

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_container, fragment)
                    .commit();
        }
    }
}

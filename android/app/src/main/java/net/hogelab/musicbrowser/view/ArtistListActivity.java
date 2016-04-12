package net.hogelab.musicbrowser.view;

import net.hogelab.musicbrowser.R;
import net.hogelab.musicbrowser.databinding.ActivityArtistListBinding;
import net.hogelab.musicbrowser.viewmodel.ArtistListRootViewModel;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by kobayasi on 2016/04/01.
 */
public class ArtistListActivity extends AppCompatActivity {
    private static final String TAG = ArtistListActivity.class.getSimpleName();


    private ActivityArtistListBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                    .add(R.id.content_container, new ArtistListFragment())
                    .commit();
        }
    }
}

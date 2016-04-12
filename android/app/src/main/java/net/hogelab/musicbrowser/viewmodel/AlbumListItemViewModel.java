package net.hogelab.musicbrowser.viewmodel;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import net.hogelab.musicbrowser.R;

/**
 * Created by kobayasi on 2016/04/11.
 */
public class AlbumListItemViewModel {
    private static final String TAG = AlbumListItemViewModel.class.getSimpleName();

    private Context context;

    private String title;
    private String subtitle;
    private String thumbnailUrl;

    public AlbumListItemViewModel(Context context, Cursor cursor) {
        this.context = context;

        title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM));
        subtitle = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST));
        thumbnailUrl = "file://" + cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
    }


    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public int getDefaultDrawable() {
        return R.drawable.media_music;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public View.OnClickListener onClickListItem() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + title);
            }
        };
    }
}

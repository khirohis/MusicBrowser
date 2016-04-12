package net.hogelab.musicbrowser.viewmodel;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import net.hogelab.musicbrowser.event.EventBus;
import net.hogelab.musicbrowser.event.OpenArtistEvent;


/**
 * Created by kobayasi on 2016/04/08.
 */
public class ArtistListItemViewModel {
    private static final String TAG = ArtistListItemViewModel.class.getSimpleName();

    private Context context;

    private long artistId;
    private String artist;
    private String numberOfAlbums;
    private String numberOfTracks;

    public ArtistListItemViewModel(Context context, Cursor cursor) {
        this.context = context;

        artistId = Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID)));
        artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST));
        numberOfAlbums = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS));
        numberOfTracks = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS));
    }


    public String getArtist() {
        return artist;
    }

    public String getNumberOfAlbums() {
        return numberOfAlbums;
    }

    public String getNumberOfTracks() {
        return numberOfTracks;
    }

    public int getDefaultDrawable() {
        return 0;
    }

    public View.OnClickListener onClickListItem() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + artist);

                EventBus.postMainLooper(new OpenArtistEvent(artistId));
            }
        };
    }
}

package net.hogelab.musicbrowser.viewmodel;

import android.content.Context;
import android.util.Log;
import android.view.View;

import net.hogelab.musicbrowser.event.EventBus;
import net.hogelab.musicbrowser.event.OpenArtistEvent;
import net.hogelab.musicbrowser.model.entity.ArtistEntity;


/**
 * Created by kobayasi on 2016/04/08.
 */
public class ArtistListItemViewModel {
    private static final String TAG = ArtistListItemViewModel.class.getSimpleName();

    private Context context;

    private String id;
    private String artist;
    private int numberOfAlbums;
    private int numberOfTracks;


    public ArtistListItemViewModel(Context context, ArtistEntity listItem) {
        this.context = context;

        id = listItem.getId();
        artist = listItem.getArtist();
        numberOfAlbums = listItem.getNumberOfAlbums();
        numberOfTracks = listItem.getNumberOfTracks();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getNumberOfAlbums() {
        return numberOfAlbums;
    }

    public void setNumberOfAlbums(int numberOfAlbums) {
        this.numberOfAlbums = numberOfAlbums;
    }

    public int getNumberOfTracks() {
        return numberOfTracks;
    }

    public void setNumberOfTracks(int numberOfTracks) {
        this.numberOfTracks = numberOfTracks;
    }

    public int getDefaultDrawable() {
        return 0;
    }

    public View.OnClickListener onClickListItem() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + artist);

                EventBus.postMainLooper(new OpenArtistEvent(id));
            }
        };
    }
}

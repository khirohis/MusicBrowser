package net.hogelab.musicbrowser.viewmodel;

import android.content.Context;
import android.database.Cursor;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.provider.MediaStore;

import net.hogelab.musicbrowser.BR;

/**
 * Created by kobayasi on 2016/04/11.
 */
public class AlbumListRootViewModel extends BaseObservable {
    private static final String TAG = AlbumListRootViewModel.class.getSimpleName();

    private Context context;

    private long artistId;
    private String artist;
    private String numberOfAlbums;
    private String numberOfTracks;


    public AlbumListRootViewModel(Context context, Cursor cursor) {
        this.context = context;

        setData(cursor);
    }


    public void setData(Cursor cursor) {
        if (cursor != null) {
            setArtistId(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID))));
            setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST)));
            setNumberOfAlbums(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS)));
            setNumberOfTracks(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS)));
        }
    }


    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }

    @Bindable
    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
        notifyPropertyChanged(BR.artist);
    }

    @Bindable
    public String getNumberOfAlbums() {
        return numberOfAlbums;
    }

    public void setNumberOfAlbums(String numberOfAlbums) {
        this.numberOfAlbums = numberOfAlbums;
        notifyPropertyChanged(BR.numberOfAlbums);
    }

    @Bindable
    public String getNumberOfTracks() {
        return numberOfTracks;
    }

    public void setNumberOfTracks(String numberOfTracks) {
        this.numberOfTracks = numberOfTracks;
        notifyPropertyChanged(BR.numberOfTracks);
    }
}

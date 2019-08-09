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

    private String id;
    private String artist;
    private int numberOfAlbums;
    private int numberOfTracks;


    public AlbumListRootViewModel(Context context, Cursor cursor) {
        this.context = context;

        setupFromCursor(cursor);
    }


    public void setupFromCursor(Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            setId(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID)));
            setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST)));
            setNumberOfAlbums(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS))));
            setNumberOfTracks(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS))));
        }
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    public int getNumberOfAlbums() {
        return numberOfAlbums;
    }

    public void setNumberOfAlbums(int numberOfAlbums) {
        this.numberOfAlbums = numberOfAlbums;
        notifyPropertyChanged(BR.numberOfAlbums);
    }

    @Bindable
    public int getNumberOfTracks() {
        return numberOfTracks;
    }

    public void setNumberOfTracks(int numberOfTracks) {
        this.numberOfTracks = numberOfTracks;
        notifyPropertyChanged(BR.numberOfTracks);
    }
}

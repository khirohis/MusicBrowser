package net.hogelab.musicbrowser.viewmodel;

import android.content.Context;
import android.database.Cursor;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.provider.MediaStore;

import net.hogelab.musicbrowser.BR;
import net.hogelab.musicbrowser.R;

/**
 * Created by kobayasi on 2016/04/18.
 */
public class TrackListRootViewModel extends BaseObservable {
    private static final String TAG = AlbumListRootViewModel.class.getSimpleName();

    private Context context;

    private long id;
    private String album;
    private String artist;
    private String albumArt;


    public TrackListRootViewModel(Context context, Cursor cursor) {
        this.context = context;

        setupFromCursor(cursor);
    }


    public void setupFromCursor(Cursor cursor) {
        if (cursor != null) {
            setId(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID))));
            setAlbum(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM)));
            setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST)));
            setAlbumArt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)));
        }
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Bindable
    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
        notifyPropertyChanged(BR.album);
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
    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
        notifyPropertyChanged(BR.albumArt);
        notifyPropertyChanged(BR.thumbnailUrl);
    }

    @Bindable
    public String getThumbnailUrl() {
        return "file://" + albumArt;
    }

    public int getDefaultDrawable() {
        return R.drawable.media_music;
    }
}

package net.hogelab.musicbrowser.viewmodel;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import net.hogelab.musicbrowser.R;
import net.hogelab.musicbrowser.event.EventBus;
import net.hogelab.musicbrowser.event.OpenAlbumEvent;

/**
 * Created by kobayasi on 2016/04/11.
 */
public class AlbumListItemViewModel {

    private static final String TAG = AlbumListItemViewModel.class.getSimpleName();


    private Context context;

    private String id;
    private String album;
    private String artist;
    private String albumArt;


    public AlbumListItemViewModel(Context context, Cursor cursor) {
        this.context = context;

        setupFromCursor(cursor);
    }


    public void setupFromCursor(Cursor cursor) {
        if (cursor != null) {
            setId(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID)));
            setAlbum(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM)));
            setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST)));
            setAlbumArt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)));
        }
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    public String getThumbnailUrl() {
        return "file://" + albumArt;
    }

    public int getDefaultDrawable() {
        return R.drawable.media_music;
    }


    public View.OnClickListener onClickListItem() {
        return (view) -> {
            Log.d(TAG, "onClick: " + album);
            EventBus.postMainLooper(new OpenAlbumEvent(id));
        };
    }
}

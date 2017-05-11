package net.hogelab.musicbrowser.viewmodel;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import net.hogelab.musicbrowser.R;
import net.hogelab.musicbrowser.event.EventBus;
import net.hogelab.musicbrowser.event.PlayTrackEvent;

/**
 * Created by kobayasi on 2016/04/18.
 */
public class TrackListItemViewModel {

    private static final String TAG = TrackListItemViewModel.class.getSimpleName();


    private Context context;

    private String id;
    private String data;
    private String title;
    private long duration;

    private String artistId;
    private String artist;
    private String composer;
    private String albumId;
    private String album;
    private int track;


    public TrackListItemViewModel(Context context, Cursor cursor) {
        this.context = context;

        setupFromCursor(cursor);
    }


    public void setupFromCursor(Cursor cursor) {
        if (cursor != null) {
            setId(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
            setData(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
            setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
            setDuration(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))));
            
            setArtistId(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID)));
            setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
            setComposer(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.COMPOSER)));
            setAlbumId(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)));
            setAlbum(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));
            setTrack(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TRACK))));
        }
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }


    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getTrack() {
        return track;
    }

    public void setTrack(int track) {
        this.track = track;
    }

    public int getDefaultDrawable() {
        return R.drawable.media_music;
    }


    public View.OnClickListener onClickListItem() {
        return (view) -> {
            Log.d(TAG, "onClick: " + title);
            EventBus.postMainLooper(new PlayTrackEvent(id));
        };
    }
}

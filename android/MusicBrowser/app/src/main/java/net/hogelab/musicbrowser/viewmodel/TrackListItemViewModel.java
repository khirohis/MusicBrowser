package net.hogelab.musicbrowser.viewmodel;

import android.content.Context;
import android.util.Log;
import android.view.View;

import net.hogelab.musicbrowser.R;
import net.hogelab.musicbrowser.model.entity.TrackEntity;

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


    public TrackListItemViewModel(Context context, TrackEntity listItem) {
        this.context = context;

        setId(listItem.getId());
        setData(listItem.getData());
        setTitle(listItem.getTitle());
        setDuration(listItem.getDuration());

        setArtistId(listItem.getArtistId());
        setArtist(listItem.getArtist());
        setComposer(listItem.getComposer());
        setAlbumId(listItem.getAlbumId());
        setAlbum(listItem.getAlbum());
        setTrack(listItem.getTrack());
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
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + title);
            }
        };
    }
}

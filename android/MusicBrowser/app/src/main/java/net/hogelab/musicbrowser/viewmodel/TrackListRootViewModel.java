package net.hogelab.musicbrowser.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import net.hogelab.musicbrowser.BR;
import net.hogelab.musicbrowser.R;
import net.hogelab.musicbrowser.model.entity.AlbumEntity;

/**
 * Created by kobayasi on 2016/04/18.
 */
public class TrackListRootViewModel extends BaseObservable {
    private static final String TAG = AlbumListRootViewModel.class.getSimpleName();

    private Context context;

    private String id;
    private String album;
    private String artist;
    private String albumArt;


    public TrackListRootViewModel(Context context, AlbumEntity album) {
        this.context = context;

        setupFromAlbum(album);
    }


    public void setupFromAlbum(AlbumEntity album) {
        if (album != null) {
            setId(album.getId());
            setAlbum(album.getAlbum());
            setArtist(album.getArtist());
            setAlbumArt(album.getAlbumArt());
        }
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        notifyPropertyChanged(BR.albumArtUrl);
    }

    @Bindable
    public String getAlbumArtUrl() {
        return "file://" + albumArt;
    }

    public int getDefaultDrawable() {
        return R.drawable.media_music;
    }
}

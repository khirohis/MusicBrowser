package net.hogelab.musicbrowser.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import net.hogelab.musicbrowser.BR;
import net.hogelab.musicbrowser.model.entity.ArtistEntity;

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


    public AlbumListRootViewModel(Context context, ArtistEntity artist) {
        this.context = context;

        setupFromArtist(artist);
    }


    public void setupFromArtist(ArtistEntity artist) {
        if (artist != null) {
            setId(artist.getId());
            setArtist(artist.getArtist());
            setNumberOfAlbums(artist.getNumberOfAlbums());
            setNumberOfTracks(artist.getNumberOfTracks());
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

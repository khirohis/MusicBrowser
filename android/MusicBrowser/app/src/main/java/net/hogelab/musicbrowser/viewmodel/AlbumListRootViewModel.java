package net.hogelab.musicbrowser.viewmodel;

import android.content.Context;
import android.database.Cursor;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.provider.MediaStore;

import net.hogelab.musicbrowser.BR;
import net.hogelab.musicbrowser.model.entity.Artist;

import io.realm.Realm;

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


    public AlbumListRootViewModel(Context context, String artistId) {
        this.context = context;

        setupFromArtistId(artistId);
    }


    public void setupFromArtistId(String artistId) {
        if (artistId != null) {
            Realm realm = Realm.getDefaultInstance();
            Artist artist = realm.where(Artist.class).equalTo("id", artistId).findFirst();
            if (artist != null) {
                setId(artist.getId());
                setArtist(artist.getArtist());
                setNumberOfAlbums(artist.getNumberOfAlbums());
                setNumberOfTracks(artist.getNumberOfTracks());
            }
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

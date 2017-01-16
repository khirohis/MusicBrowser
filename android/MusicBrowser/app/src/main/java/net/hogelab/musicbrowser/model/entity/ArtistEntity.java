package net.hogelab.musicbrowser.model.entity;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.annotations.PrimaryKey;

/**
 * Created by kobayasi on 2017/01/16.
 */

public class ArtistEntity extends RealmObject {

    @PrimaryKey
    private String id;

    private String artist;
    private int numberOfAlbums;
    private int numberOfTracks;

    private RealmList<EntityHolder> holders;


    // getter and setter
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

    public RealmList<EntityHolder> getHolders() {
        return holders;
    }

    public void setHolders(RealmList<EntityHolder> holders) {
        this.holders = holders;
    }


    // RealmObject factory methods
    public static ArtistEntity create(Realm realm, String id) {
        return realm.createObject(ArtistEntity.class, id);
    }

    public static ArtistEntity createOrFetch(Realm realm, String id) {
        ArtistEntity entity = findById(realm, id).findFirst();
        if (entity == null) {
            entity = realm.createObject(ArtistEntity.class, id);
        }

        return entity;
    }

    public static ArtistEntity createOrUpdate(Realm realm, String id, String artist, int numberOfAlbums, int numberOfTracks) {
        ArtistEntity entity = createOrFetch(realm, id);
        entity.setArtist(artist);
        entity.setNumberOfAlbums(numberOfAlbums);
        entity.setNumberOfTracks(numberOfTracks);

        return entity;
    }

    // RealmQuery factory methods
    public static RealmQuery<ArtistEntity> findById(Realm realm, String id) {
        return realm.where(ArtistEntity.class).equalTo("id", id);
    }
}

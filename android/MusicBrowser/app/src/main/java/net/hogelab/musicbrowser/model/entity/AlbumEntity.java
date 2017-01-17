package net.hogelab.musicbrowser.model.entity;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.annotations.PrimaryKey;

/**
 * Created by kobayasi on 2017/01/17.
 */

public class AlbumEntity extends RealmObject {

    @PrimaryKey
    private String id;

    private String album;
    private String artist;
    private String albumArt;

    private RealmList<EntityHolder> holders;


    // getter and setter
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

    public RealmList<EntityHolder> getHolders() {
        return holders;
    }

    public void setHolders(RealmList<EntityHolder> holders) {
        this.holders = holders;
    }


    // RealmObject factory methods
    public static AlbumEntity create(Realm realm, String id) {
        return realm.createObject(AlbumEntity.class, id);
    }

    public static AlbumEntity createOrFetch(Realm realm, String id) {
        AlbumEntity entity = queryById(realm, id).findFirst();
        if (entity == null) {
            entity = realm.createObject(AlbumEntity.class, id);
        }

        return entity;
    }

    public static AlbumEntity createOrUpdate(Realm realm, String id, String album, String artist, String albumArt) {
        AlbumEntity entity = createOrFetch(realm, id);
        entity.setAlbum(album);
        entity.setArtist(artist);
        entity.setAlbumArt(albumArt);

        return entity;
    }

    // RealmQuery factory methods
    public static RealmQuery<AlbumEntity> queryById(Realm realm, String id) {
        return realm.where(AlbumEntity.class).equalTo("id", id);
    }
}

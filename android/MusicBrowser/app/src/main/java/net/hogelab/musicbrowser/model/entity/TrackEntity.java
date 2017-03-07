package net.hogelab.musicbrowser.model.entity;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.annotations.PrimaryKey;

/**
 * Created by kobayasi on 2017/03/06.
 */

public class TrackEntity extends RealmObject {

    @PrimaryKey
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

    private RealmList<EntityHolder> holders;


    // getter and setter
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

    public RealmList<EntityHolder> getHolders() {
        return holders;
    }

    public void setHolders(RealmList<EntityHolder> holders) {
        this.holders = holders;
    }


    // RealmObject factory methods
    public static TrackEntity create(Realm realm, String id) {
        return realm.createObject(TrackEntity.class, id);
    }

    public static TrackEntity createOrFetch(Realm realm, String id) {
        TrackEntity entity = queryById(realm, id).findFirst();
        if (entity == null) {
            entity = create(realm, id);
        }

        return entity;
    }

    public static TrackEntity createOrUpdate(Realm realm, String id, String data, String title, long duration,
                                             String artistId, String artist, String composer,
                                             String albumId, String album, int track) {
        TrackEntity entity = createOrFetch(realm, id);
        entity.setData(data);
        entity.setTitle(title);
        entity.setDuration(duration);

        entity.setArtistId(artistId);
        entity.setArtist(artist);
        entity.setComposer(composer);
        entity.setAlbumId(albumId);
        entity.setAlbum(album);
        entity.setTrack(track);

        return entity;
    }

    // RealmQuery factory methods
    public static RealmQuery<TrackEntity> queryById(Realm realm, String id) {
        return realm.where(TrackEntity.class).equalTo("id", id);
    }
}

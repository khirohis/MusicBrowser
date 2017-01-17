package net.hogelab.musicbrowser.model.entity;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by kobayasi on 2017/01/16.
 */

public class EntityHolder extends RealmObject {

    public enum EntityKind {
        ARTIST, ALBUM, TRACK,
    }


    @PrimaryKey
    private String id;

    private String kind;
    private ArtistEntity artist;
    private AlbumEntity album;
//    private TrackEntity track;


    // getter and setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public ArtistEntity getArtist() {
        return artist;
    }

    public void setArtist(ArtistEntity artist) {
        this.artist = artist;
    }

    public AlbumEntity getAlbum() {
        return album;
    }

    public void setAlbum(AlbumEntity album) {
        this.album = album;
    }


    // RealmObject factory methods
    public static EntityHolder createWithArtist(Realm realm, ArtistEntity entity) {
        String id = UUID.randomUUID().toString();
        EntityHolder holder = realm.createObject(EntityHolder.class, id);
        holder.setKind(EntityKind.ARTIST.name());
        holder.setArtist(entity);

        entity.getHolders().add(holder);

        return holder;
    }

    public static EntityHolder createWithAlbum(Realm realm, AlbumEntity entity) {
        String id = UUID.randomUUID().toString();
        EntityHolder holder = realm.createObject(EntityHolder.class, id);
        holder.setKind(EntityKind.ALBUM.name());
        holder.setAlbum(entity);

        entity.getHolders().add(holder);

        return holder;
    }

    public static void cascadeDelete(EntityHolder holder) {
        switch (EntityKind.valueOf(holder.kind)) {

            case ARTIST:
                holder.artist.getHolders().remove(holder);
                if (holder.artist.getHolders().size() <= 0) {
                    holder.artist.deleteFromRealm();
                }
                break;

            case ALBUM:
                holder.album.getHolders().remove(holder);
                if (holder.album.getHolders().size() <= 0) {
                    holder.album.deleteFromRealm();
                }
                break;

            case TRACK:
                // constructing!!!
                break;

            default:
                // エラーログ
                break;
        }

        holder.deleteFromRealm();
    }


    // accessor methods
    public Object getEntity() {
        switch (EntityKind.valueOf(kind)) {

            case ARTIST:
                return artist;

            case ALBUM:
                return album;

            case TRACK:
                // constructing!!!

            default:
                // エラーログ
                break;
        }

        return null;
    }

    public boolean isKind(EntityKind kind) {
        return this.kind.equals(kind.name());
    }
}

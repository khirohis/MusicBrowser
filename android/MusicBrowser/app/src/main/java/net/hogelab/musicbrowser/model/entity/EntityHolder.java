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
        ARTIST(1), ALBUM(2), TRACK(3), ;

        private final int id;

        private EntityKind(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public EntityKind getKind(int id) {
            for (EntityKind kind : EntityKind.values()) {
                if (kind.getId() == id) {
                    return kind;
                }
            }

            return null;
        }
    }


    @PrimaryKey
    private String id;

    private int kind;
    private ArtistEntity artist;
//    private AlbumEntity album;
//    private TrackEntity track;


    // getter and setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public ArtistEntity getArtist() {
        return artist;
    }

    public void setArtist(ArtistEntity artist) {
        this.artist = artist;
    }


    // RealmObject factory methods
    public static EntityHolder createWithArtist(Realm realm, ArtistEntity artist) {
        String id = UUID.randomUUID().toString();
        EntityHolder holder = realm.createObject(EntityHolder.class, id);
        holder.setKind(EntityKind.ARTIST.getId());
        holder.setArtist(artist);

        artist.getHolders().add(holder);

        return holder;
    }


    // utility methods
    public boolean isKind(EntityKind kind) {
        return this.kind == kind.getId();
    }
}

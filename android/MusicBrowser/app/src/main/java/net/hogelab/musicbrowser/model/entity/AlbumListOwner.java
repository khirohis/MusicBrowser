package net.hogelab.musicbrowser.model.entity;

import android.support.annotation.Nullable;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.annotations.PrimaryKey;

/**
 * Created by kobayasi on 2017/01/17.
 */

public class AlbumListOwner extends RealmObject {

    @PrimaryKey
    private String id;

    private String artistId;
    private RealmList<EntityList> lists;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public RealmList<EntityList> getLists() {
        return lists;
    }

    public void setLists(RealmList<EntityList> lists) {
        this.lists = lists;
    }


    // RealmObject factory methods
    public static AlbumListOwner createOrFetch(Realm realm, @Nullable String artistId) {
        AlbumListOwner listOwner = queryById(realm, artistId).findFirst();
        if (listOwner == null) {
            String id = UUID.randomUUID().toString();
            listOwner = realm.createObject(AlbumListOwner.class, id);
            listOwner.artistId = artistId;
        }

        return listOwner;
    }


    // RealmQuery factory methods
    public static RealmQuery<AlbumListOwner> queryById(Realm realm, @Nullable String artistId) {
        return realm.where(AlbumListOwner.class).equalTo("artistId", artistId);
    }


    // accessor methods
    public EntityList getFirstAlbumList() {
        return lists.first();
    }

    public void insertAlbumList(EntityList list) {
        lists.add(0, list);
    }
}

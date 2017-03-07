package net.hogelab.musicbrowser.model.entity;

import android.support.annotation.Nullable;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.annotations.PrimaryKey;

/**
 * Created by kobayasi on 2017/03/06.
 */

public class TrackListOwner extends RealmObject {

    @PrimaryKey
    private String id;

    private String albumId;
    private RealmList<EntityList> lists;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public RealmList<EntityList> getLists() {
        return lists;
    }

    public void setLists(RealmList<EntityList> lists) {
        this.lists = lists;
    }


    // RealmObject factory methods
    public static TrackListOwner createOrFetch(Realm realm, @Nullable String albumId) {
        TrackListOwner listOwner = queryById(realm, albumId).findFirst();
        if (listOwner == null) {
            String id = UUID.randomUUID().toString();
            listOwner = realm.createObject(TrackListOwner.class, id);
            listOwner.albumId = albumId;
        }

        return listOwner;
    }


    // RealmQuery factory methods
    public static RealmQuery<TrackListOwner> queryById(Realm realm, @Nullable String albumId) {
        return realm.where(TrackListOwner.class).equalTo("albumId", albumId);
    }


    // accessor methods
    public EntityList getFirstTrackList() {
        return lists.first();
    }

    public void insertTrackList(EntityList list) {
        lists.add(0, list);
    }
}

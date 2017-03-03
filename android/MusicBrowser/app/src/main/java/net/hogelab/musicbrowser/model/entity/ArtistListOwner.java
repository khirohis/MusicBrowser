package net.hogelab.musicbrowser.model.entity;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.annotations.PrimaryKey;

/**
 * Created by kobayasi on 2017/01/16.
 */

public class ArtistListOwner extends RealmObject {

    @PrimaryKey
    private String id;

    private RealmList<EntityList> lists;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RealmList<EntityList> getLists() {
        return lists;
    }

    public void setLists(RealmList<EntityList> lists) {
        this.lists = lists;
    }


    // RealmObject factory methods
    public static ArtistListOwner createOrFetch(Realm realm) {
        ArtistListOwner listOwner = query(realm).findFirst();
        if (listOwner == null) {
            String id = UUID.randomUUID().toString();
            listOwner = realm.createObject(ArtistListOwner.class, id);
        }

        return listOwner;
    }


    // RealmQuery factory methods
    public static RealmQuery<ArtistListOwner> query(Realm realm) {
        return realm.where(ArtistListOwner.class);
    }


    // accessor methods
    public EntityList getFirstArtistList() {
        return lists.first();
    }

    public void insertArtistList(EntityList list) {
        lists.add(0, list);
    }
}

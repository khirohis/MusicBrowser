package net.hogelab.musicbrowser.model.entity;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by kobayasi on 2017/01/16.
 */

public class EntityList extends RealmObject {

    @PrimaryKey
    private String id;

    private RealmList<EntityHolder> holders;


    // getter and setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RealmList<EntityHolder> getHolders() {
        return holders;
    }

    public void setHolders(RealmList<EntityHolder> entities) {
        this.holders = entities;
    }


    // RealmObject factory methods
    public static EntityList create(Realm realm) {
        String id = UUID.randomUUID().toString();
        EntityList list = realm.createObject(EntityList.class, id);

        return list;
    }

    public static void cascadeDelete(EntityList list) {
        for (EntityHolder holder : list.holders) {
            EntityHolder.cascadeDelete(holder);
        }

        list.deleteFromRealm();
    }


    // accessor methods
    public int size() {
        return holders.size();
    }

    public EntityHolder getHolder(int location) {
        return holders.get(location);
    }

    public void addHolder(EntityHolder holder) {
        holders.add(holder);
    }

    public void insertHolder(int location, EntityHolder holder) {
        holders.add(location, holder);
    }
}

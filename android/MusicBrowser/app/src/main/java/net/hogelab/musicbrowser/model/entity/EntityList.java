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

    private RealmList<EntityHolder> entities;


    // getter and setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RealmList<EntityHolder> getEntities() {
        return entities;
    }

    public void setEntities(RealmList<EntityHolder> entities) {
        this.entities = entities;
    }


    // RealmObject factory methods
    public static EntityList create(Realm realm) {
        String id = UUID.randomUUID().toString();
        EntityList list = realm.createObject(EntityList.class, id);

        return list;
    }


    // accessor methods
    public void addHolder(EntityHolder holder) {
        entities.add(holder);
    }

    public void addHolder(int location, EntityHolder holder) {
        entities.add(location, holder);
    }
}

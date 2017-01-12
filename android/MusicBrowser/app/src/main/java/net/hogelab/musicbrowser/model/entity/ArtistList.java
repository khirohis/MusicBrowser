package net.hogelab.musicbrowser.model.entity;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by kobayasi on 2017/01/11.
 */

public class ArtistList extends RealmObject {

    @PrimaryKey
    private String id;

    private Date creationDate;
    private RealmList<Artist> entities;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public RealmList<Artist> getEntities() {
        return entities;
    }

    public void setEntities(RealmList<Artist> entities) {
        this.entities = entities;
    }
}

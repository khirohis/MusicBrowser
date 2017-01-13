package net.hogelab.musicbrowser.model.entity;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by kobayasi on 2017/01/12.
 */

public class TrackList extends RealmObject {

    @PrimaryKey
    private String id;

    private String parentId;
    private Date creationDate;
    private RealmList<Track> entities;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public RealmList<Track> getEntities() {
        return entities;
    }

    public void setEntities(RealmList<Track> entities) {
        this.entities = entities;
    }
}

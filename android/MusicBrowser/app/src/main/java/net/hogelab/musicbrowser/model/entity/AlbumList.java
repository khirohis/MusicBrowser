package net.hogelab.musicbrowser.model.entity;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by kobayasi on 2017/01/12.
 */

public class AlbumList extends RealmObject {

    @PrimaryKey
    private String id;

    private String artistId;
    private Date creationDate;
    private RealmList<Album> entities;

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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public RealmList<Album> getEntities() {
        return entities;
    }

    public void setEntities(RealmList<Album> entities) {
        this.entities = entities;
    }
}

package net.hogelab.musicbrowser.model.entity.wrapper;

import net.hogelab.musicbrowser.model.entity.Artist;
import net.hogelab.musicbrowser.model.entity.ArtistList;

import java.util.Date;

/**
 * Created by kobayasi on 2017/01/12.
 */

public class ArtistListWrapper extends ListWrapper<ArtistList, Artist> {

    public ArtistListWrapper(ArtistList listObject) {
        super(listObject);
    }


    @Override
    public String getId() {
        return listObject.getId();
    }

    @Override
    public Date getCreationDate() {
        return listObject.getCreationDate();
    }

    @Override
    public int size() {
        return listObject.getEntities().size();
    }

    @Override
    public Artist get(int location) {
        return listObject.getEntities().get(location);
    }
}

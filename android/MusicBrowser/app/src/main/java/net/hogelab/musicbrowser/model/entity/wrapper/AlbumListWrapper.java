package net.hogelab.musicbrowser.model.entity.wrapper;

import net.hogelab.musicbrowser.model.entity.Album;
import net.hogelab.musicbrowser.model.entity.AlbumList;

import java.util.Date;

/**
 * Created by kobayasi on 2017/01/12.
 */

public class AlbumListWrapper extends ListWrapper<AlbumList, Album> {

    public AlbumListWrapper(AlbumList listObject) {
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
    public Album get(int location) {
        return listObject.getEntities().get(location);
    }
}

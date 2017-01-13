package net.hogelab.musicbrowser.model.entity.wrapper;

import net.hogelab.musicbrowser.model.entity.Track;
import net.hogelab.musicbrowser.model.entity.TrackList;

import java.util.Date;

/**
 * Created by kobayasi on 2017/01/12.
 */

public class TrackListWrapper extends ListWrapper<TrackList, Track> {

    public TrackListWrapper(TrackList listObject) {
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
    public Track get(int location) {
        return listObject.getEntities().get(location);
    }
}

package net.hogelab.musicbrowser.model.entity.wrapper;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by kobayasi on 2017/01/12.
 */

public abstract class ListWrapper<ListObject, EntityObject> {

    protected ListObject listObject;


    public ListWrapper(ListObject listObject) {
        this.listObject = listObject;
    }


    public ListObject getListObject() {
        return listObject;
    }

    public void setListObject(ListObject listObject) {
        this.listObject = listObject;
    }


    public abstract String getId();

    public abstract Date getCreationDate();

    public abstract int size();

    public abstract EntityObject get(int location);
}

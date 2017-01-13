package net.hogelab.musicbrowser.model;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import net.hogelab.musicbrowser.model.entity.Artist;
import net.hogelab.musicbrowser.model.entity.ArtistList;

import java.util.Date;
import java.util.UUID;

import io.realm.Realm;

/**
 * Created by kobayasi on 2017/01/11.
 */

public class ArtistListLoader extends MediaStoreLoader {
    private static final String TAG = ArtistListLoader.class.getSimpleName();


    public ArtistListLoader(Context context) {
        super(context);
    }


    @Override
    protected String loadData() {
        Cursor cursor = getContext().getContentResolver().query(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                MediaStoreFieldsProjection.ARTIST_FIELDS_PROJECTION,
                null,
                null,
                MediaStore.Audio.Artists.DEFAULT_SORT_ORDER);

        if (cursor != null) {
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();

            String listId = UUID.randomUUID().toString();
            ArtistList list = realm.createObject(ArtistList.class, listId);
            list.setCreationDate(new Date());

            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID));
                Artist entity = realm.where(Artist.class).equalTo("id", id).findFirst();
                if (entity == null) {
                    entity = realm.createObject(Artist.class, id);
                }

                entity.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST)));
                entity.setNumberOfAlbums(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS))));
                entity.setNumberOfTracks(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS))));

                list.getEntities().add(entity);
            }

            realm.commitTransaction();

            cursor.close();

            return listId;
        }

        return null;
    }
}

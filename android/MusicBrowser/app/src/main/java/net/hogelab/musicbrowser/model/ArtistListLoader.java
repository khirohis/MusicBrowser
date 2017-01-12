package net.hogelab.musicbrowser.model;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import net.hogelab.musicbrowser.model.entity.Artist;
import net.hogelab.musicbrowser.model.entity.ArtistList;

import java.util.Date;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmConfiguration;

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
                MediaStoreFieldsProjection.ARTISTS_FIELDS_PROJECTION,
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
                Artist artist = realm.where(Artist.class).equalTo("id", id).findFirst();
                if (artist == null) {
                    artist = realm.createObject(Artist.class, id);
                }

                artist.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST)));
                artist.setNumberOfAlbums(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS))));
                artist.setNumberOfTracks(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS))));

                list.getEntities().add(artist);
            }

            realm.commitTransaction();

            cursor.close();

            return listId;
        }

        return null;
    }
}

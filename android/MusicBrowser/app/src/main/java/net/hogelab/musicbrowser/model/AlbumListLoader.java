package net.hogelab.musicbrowser.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import net.hogelab.musicbrowser.model.entity.Album;
import net.hogelab.musicbrowser.model.entity.AlbumList;

import java.util.Date;
import java.util.UUID;

import io.realm.Realm;

/**
 * Created by kobayasi on 2017/01/12.
 */

public class AlbumListLoader extends MediaStoreToRealmLoader {
    private static final String TAG = AlbumListLoader.class.getSimpleName();

    private final String mArtistId;


    public AlbumListLoader(Context context, String artistId) {
        super(context);

        mArtistId = artistId;
    }


    @Override
    protected String loadData(Realm realm) {
        Uri uri = null;
        if (mArtistId != null) {
            uri = MediaStore.Audio.Artists.Albums.getContentUri("external", Long.parseLong(mArtistId));
        } else {
            uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        }

        Cursor cursor = getContext().getContentResolver().query(
                uri,
                MediaStoreFieldsProjection.ALBUM_FIELDS_PROJECTION,
                null,
                null,
                MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);

        if (cursor != null) {
            String listId = UUID.randomUUID().toString();
            AlbumList list = realm.createObject(AlbumList.class, listId);
            list.setCreationDate(new Date());
            list.setParentId(mArtistId);

            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID));
                Album entity = realm.where(Album.class).equalTo("id", id).findFirst();
                if (entity == null) {
                    entity = realm.createObject(Album.class, id);
                }

                entity.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST)));
                entity.setAlbum(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM)));
                entity.setAlbumArt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)));

                list.getEntities().add(entity);
            }

            cursor.close();

            return listId;
        }

        return null;
    }
}

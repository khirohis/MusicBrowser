package net.hogelab.musicbrowser.model;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import net.hogelab.musicbrowser.model.entity.AlbumEntity;

import io.realm.Realm;

/**
 * Created by kobayasi on 2017/01/13.
 */

public class AlbumLoader extends MediaStoreToRealmLoader {
    private String albumId;

    public AlbumLoader(Context context, String albumId) {
        super(context);

        this.albumId = albumId;
    }


    @Override
    protected String loadData(Realm realm) {
        String selection = MediaStore.Audio.Albums._ID + "=?";
        String[] selectionArgs = new String[] { albumId };

        Cursor cursor = getContext().getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                MediaStoreFieldsProjection.ALBUM_FIELDS_PROJECTION,
                selection,
                selectionArgs,
                MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);

        if (cursor != null) {
            if (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID));
                String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST));
                String albumArt = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART));
                AlbumEntity.createOrUpdate(realm, id, album, artist, albumArt);
            }

            cursor.close();

            return albumId;
        }

        return null;
    }
}

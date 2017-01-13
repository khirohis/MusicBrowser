package net.hogelab.musicbrowser.model;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import net.hogelab.musicbrowser.model.entity.Album;

import io.realm.Realm;

/**
 * Created by kobayasi on 2017/01/13.
 */

public class AlbumLoader extends MediaStoreLoader {
    private String albumId;

    public AlbumLoader(Context context, String albumId) {
        super(context);

        this.albumId = albumId;
    }


    @Override
    protected String loadData() {
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
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();

                String id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID));
                Album entity = realm.where(Album.class).equalTo("id", id).findFirst();
                if (entity == null) {
                    entity = realm.createObject(Album.class, id);
                }

                entity.setAlbum(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM)));
                entity.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST)));
                entity.setAlbumArt(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART)));

                realm.commitTransaction();
            }

            cursor.close();

            return albumId;
        }

        return null;
    }
}

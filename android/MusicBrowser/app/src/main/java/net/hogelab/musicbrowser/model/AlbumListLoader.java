package net.hogelab.musicbrowser.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import net.hogelab.musicbrowser.model.entity.AlbumEntity;
import net.hogelab.musicbrowser.model.entity.AlbumListOwner;
import net.hogelab.musicbrowser.model.entity.EntityHolder;
import net.hogelab.musicbrowser.model.entity.EntityList;

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
            EntityList entityList = EntityList.create(realm);

            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID));
                String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST));
                String albumArt = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART));
                AlbumEntity entity = AlbumEntity.createOrUpdate(realm, id, album, artist, albumArt);

                EntityHolder holder = EntityHolder.createWithAlbum(realm, entity);
                entityList.addHolder(holder);
            }

            cursor.close();

            AlbumListOwner listOwner = AlbumListOwner.createOrFetch(realm, mArtistId);
            listOwner.insertAlbumList(entityList);

            return listOwner.getId();
        }

        return null;
    }
}

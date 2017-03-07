package net.hogelab.musicbrowser.model;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import net.hogelab.musicbrowser.model.entity.EntityHolder;
import net.hogelab.musicbrowser.model.entity.EntityList;
import net.hogelab.musicbrowser.model.entity.TrackEntity;
import net.hogelab.musicbrowser.model.entity.TrackListOwner;

import io.realm.Realm;

/**
 * Created by kobayasi on 2017/01/13.
 */

public class TrackListLoader extends MediaStoreToRealmLoader {
    private static final String TAG = TrackListLoader.class.getSimpleName();

    private final String mAlbumId;


    public TrackListLoader(Context context, String albumId) {
        super(context);

        mAlbumId = albumId;
    }


    @Override
    protected String loadData(Realm realm) {
        String selection = null;
        String[] selectionArgs = null;

        if (mAlbumId != null) {
            selection = MediaStore.Audio.Media.ALBUM_ID + "=?";
            selectionArgs = new String[] { mAlbumId };
        } else {
            selection = MediaStore.Audio.Media.IS_MUSIC + "=1";
        }

        Cursor cursor = getContext().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                MediaStoreFieldsProjection.TRACK_FIELDS_PROJECTION,
                selection,
                selectionArgs,
                MediaStore.Audio.Media.TRACK);

        if (cursor != null) {
            EntityList entityList = EntityList.create(realm);
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID));
                String data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                long duration = Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));

                String artistId = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID));
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                String composer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.COMPOSER));
                String albumId = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                int track = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TRACK)));

                TrackEntity entity = TrackEntity.createOrUpdate(realm, id, data, title, duration,
                        artistId, artist, composer, albumId, album, track);

                EntityHolder holder = EntityHolder.createWithTrack(realm, entity);
                entityList.addHolder(holder);
            }

            cursor.close();

            TrackListOwner listOwner = TrackListOwner.createOrFetch(realm, mAlbumId);
            listOwner.insertTrackList(entityList);

            return listOwner.getId();
        }

        return null;
    }
}

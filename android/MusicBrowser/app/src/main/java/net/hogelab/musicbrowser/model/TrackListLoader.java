package net.hogelab.musicbrowser.model;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import net.hogelab.musicbrowser.model.entity.Track;
import net.hogelab.musicbrowser.model.entity.TrackList;

import java.util.Date;
import java.util.UUID;

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
            String listId = UUID.randomUUID().toString();
            TrackList list = realm.createObject(TrackList.class, listId);
            list.setCreationDate(new Date());
            list.setParentId(mAlbumId);

            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                Track entity = realm.where(Track.class).equalTo("id", id).findFirst();
                if (entity == null) {
                    entity = realm.createObject(Track.class, id);
                }

                entity.setData(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                entity.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
                entity.setDuration(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))));

                entity.setArtistId(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID)));
                entity.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                entity.setComposer(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.COMPOSER)));
                entity.setAlbumId(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)));
                entity.setAlbum(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                entity.setTrack(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TRACK))));

                list.getEntities().add(entity);
            }

            cursor.close();

            return listId;
        }

        return null;
    }
}

package net.hogelab.musicbrowser.model;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import net.hogelab.musicbrowser.model.entity.ArtistEntity;

import io.realm.Realm;

/**
 * Created by kobayasi on 2017/01/11.
 */

public class ArtistLoader extends MediaStoreToRealmLoader {
    private String artistId;

    public ArtistLoader(Context context, String artistId) {
        super(context);

        this.artistId = artistId;
    }


    @Override
    protected String loadData(Realm realm) {
        String selection = MediaStore.Audio.Artists._ID + "=?";
        String[] selectionArgs = new String[] { artistId };

        Cursor cursor = getContext().getContentResolver().query(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                MediaStoreFieldsProjection.ARTIST_FIELDS_PROJECTION,
                selection,
                selectionArgs,
                MediaStore.Audio.Artists.DEFAULT_SORT_ORDER);

        if (cursor != null) {
            if (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID));
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST));
                int numberOfAlbums = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS)));
                int numberOfTracks = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS)));
                ArtistEntity.createOrUpdate(realm, id, artist, numberOfAlbums, numberOfTracks);
            }

            cursor.close();

            return artistId;
        }

        return null;
    }
}

package net.hogelab.musicbrowser.model;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import net.hogelab.musicbrowser.model.entity.Artist;

import io.realm.Realm;

/**
 * Created by kobayasi on 2017/01/11.
 */

public class ArtistLoader extends MediaStoreLoader {
    private String artistId;

    public ArtistLoader(Context context, String artistId) {
        super(context);

        this.artistId = artistId;
    }


    @Override
    protected String loadData() {
        String selection = MediaStore.Audio.Artists._ID + "=?";
        String[] selectionArgs = new String[] { artistId };

        Cursor cursor = getContext().getContentResolver().query(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                MediaStoreFieldsProjection.ARTISTS_FIELDS_PROJECTION,
                selection,
                selectionArgs,
                MediaStore.Audio.Artists.DEFAULT_SORT_ORDER);

        if (cursor != null) {
            if (cursor.moveToNext()) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();

                String id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID));
                Artist artist = realm.where(Artist.class).equalTo("id", id).findFirst();
                if (artist == null) {
                    artist = realm.createObject(Artist.class, id);
                }

                artist.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST)));
                artist.setNumberOfAlbums(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS))));
                artist.setNumberOfTracks(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS))));

                realm.commitTransaction();
            }

            cursor.close();

            return artistId;
        }

        return null;
    }
}

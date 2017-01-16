package net.hogelab.musicbrowser.model;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import net.hogelab.musicbrowser.model.entity.ArtistEntity;
import net.hogelab.musicbrowser.model.entity.ArtistListOwner;
import net.hogelab.musicbrowser.model.entity.EntityHolder;
import net.hogelab.musicbrowser.model.entity.EntityList;

import io.realm.Realm;

/**
 * Created by kobayasi on 2017/01/11.
 */

public class ArtistListLoader extends MediaStoreToRealmLoader {
    private static final String TAG = ArtistListLoader.class.getSimpleName();


    public ArtistListLoader(Context context) {
        super(context);
    }


    @Override
    protected String loadData(Realm realm) {
        Cursor cursor = getContext().getContentResolver().query(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                MediaStoreFieldsProjection.ARTIST_FIELDS_PROJECTION,
                null,
                null,
                MediaStore.Audio.Artists.DEFAULT_SORT_ORDER);

        if (cursor != null) {
            EntityList entityList = EntityList.create(realm);

            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID));
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST));
                int numberOfAlbums = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS)));
                int numberOfTracks = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS)));
                ArtistEntity entity = ArtistEntity.createOrUpdate(realm, id, artist, numberOfAlbums, numberOfTracks);

                EntityHolder holder = EntityHolder.createWithArtist(realm, entity);
                entityList.addHolder(holder);
            }

            cursor.close();

            ArtistListOwner listOwner = ArtistListOwner.createOrFetch(realm);
            listOwner.insertArtistList(entityList);

            return listOwner.getId();
        }

        return null;
    }
}

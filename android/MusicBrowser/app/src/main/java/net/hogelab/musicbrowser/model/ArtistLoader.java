package net.hogelab.musicbrowser.model;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

/**
 * Created by kobayasi on 2017/01/11.
 */

public class ArtistLoader extends MediaStoreLoader {
    private long artistId;

    public ArtistLoader(Context context, long artistId) {
        super(context);

        this.artistId = artistId;
    }


    @Override
    protected String loadData() {
        String selection = MediaStore.Audio.Artists._ID + "=?";
        String[] selectionArgs = new String[] { Long.toString(artistId) };

        Cursor cursor = getContext().getContentResolver().query(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                MediaStoreFieldsProjection.ARTISTS_FIELDS_PROJECTION,
                selection,
                selectionArgs,
                MediaStore.Audio.Artists.DEFAULT_SORT_ORDER);

        if (cursor != null) {
            if (cursor.moveToNext()) {
                // create artist data
            }

            cursor.close();
        }

        return null;
    }
}

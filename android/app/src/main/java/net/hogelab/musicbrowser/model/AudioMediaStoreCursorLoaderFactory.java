package net.hogelab.musicbrowser.model;

import android.content.Context;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

/**
 * Created by kobayasi on 2016/04/12.
 */
public final class AudioMediaStoreCursorLoaderFactory {

    public static final String[] ARTISTS_FIELDS_PROJECTION = {
            MediaStore.Audio.Artists._ID,
            MediaStore.Audio.Artists.ARTIST,
            MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
            MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
    };

    public static final String[] ALBUM_FIELDS_PROJECTION = {
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ALBUM_ART,
            MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.FIRST_YEAR,
            MediaStore.Audio.Albums.LAST_YEAR,
            MediaStore.Audio.Albums.NUMBER_OF_SONGS,
    };


    public static final CursorLoader createArtistsCursorLoader(Context context) {
        return new CursorLoader(context,
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                ARTISTS_FIELDS_PROJECTION,
                null,
                null,
                MediaStore.Audio.Artists.DEFAULT_SORT_ORDER);
    }

    public static final CursorLoader createAlbumsCursorLoader(Context context) {
        return new CursorLoader(context,
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                ALBUM_FIELDS_PROJECTION,
                null,
                null,
                MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
    }


    public static final CursorLoader createAlbumsCursorLoader(Context context, long byArtistId) {
        return new CursorLoader(context,
                MediaStore.Audio.Artists.Albums.getContentUri("external", byArtistId),
                ALBUM_FIELDS_PROJECTION,
                null,
                null,
                MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
    }
}

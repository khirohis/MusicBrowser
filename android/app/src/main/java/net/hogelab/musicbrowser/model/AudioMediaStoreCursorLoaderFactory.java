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

    public static final String[] TRACK_FIELDS_PROJECTION = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DURATION,

            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.COMPOSER,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.TRACK,
            MediaStore.Audio.Media.YEAR,
    };


    public static final CursorLoader createArtistCursorLoader(Context context, long artistId) {
        String selection = MediaStore.Audio.Artists._ID + "=?";
        String[] selectionArgs = new String[] { Long.toString(artistId) };

        return new CursorLoader(context,
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                ARTISTS_FIELDS_PROJECTION,
                selection,
                selectionArgs,
                MediaStore.Audio.Artists.DEFAULT_SORT_ORDER);
    }

    public static final CursorLoader createArtistListCursorLoader(Context context) {
        return new CursorLoader(context,
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                ARTISTS_FIELDS_PROJECTION,
                null,
                null,
                MediaStore.Audio.Artists.DEFAULT_SORT_ORDER);
    }


    public static final CursorLoader createAlbumCursorLoader(Context context, long albumId) {
        String selection = MediaStore.Audio.Albums._ID + "=?";
        String[] selectionArgs = new String[] { Long.toString(albumId) };

        return new CursorLoader(context,
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                ALBUM_FIELDS_PROJECTION,
                selection,
                selectionArgs,
                MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
    }

    public static final CursorLoader createAlbumListCursorLoader(Context context) {
        return new CursorLoader(context,
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                ALBUM_FIELDS_PROJECTION,
                null,
                null,
                MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
    }

    public static final CursorLoader createAlbumListCursorLoader(Context context, long artistId) {
        return new CursorLoader(context,
                MediaStore.Audio.Artists.Albums.getContentUri("external", artistId),
                ALBUM_FIELDS_PROJECTION,
                null,
                null,
                MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
    }


    public static final CursorLoader createTrackCursorLoader(Context context, long trackId) {
        String selection = MediaStore.Audio.Media._ID + "=?";
        String[] selectionArgs = new String[] { Long.toString(trackId) };

        return new CursorLoader(context,
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                TRACK_FIELDS_PROJECTION,
                selection,
                selectionArgs,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
    }

    public static final CursorLoader createTrackListCursorLoader(Context context) {
        String selection = MediaStore.Audio.Media.IS_MUSIC + "=1";

        return new CursorLoader(context,
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                TRACK_FIELDS_PROJECTION,
                selection,
                null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
    }

    public static final CursorLoader createTrackListCursorLoader(Context context, long albumId) {
        String selection = MediaStore.Audio.Media.ALBUM_ID + "=?";
        String[] selectionArgs = new String[] { Long.toString(albumId) };

        return new CursorLoader(context,
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                TRACK_FIELDS_PROJECTION,
                selection,
                selectionArgs,
                MediaStore.Audio.Media.TRACK);
    }
}

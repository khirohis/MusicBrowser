package net.hogelab.musicbrowser.model;

import android.content.Context;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

/**
 * Created by kobayasi on 2016/04/12.
 */
public final class AudioMediaStoreCursorLoaderFactory {

    public static final CursorLoader createArtistCursorLoader(Context context, long artistId) {
        String selection = MediaStore.Audio.Artists._ID + "=?";
        String[] selectionArgs = new String[] { Long.toString(artistId) };

        return new CursorLoader(context,
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                MediaStoreFieldsProjection.ARTISTS_FIELDS_PROJECTION,
                selection,
                selectionArgs,
                MediaStore.Audio.Artists.DEFAULT_SORT_ORDER);
    }

    public static final CursorLoader createArtistListCursorLoader(Context context) {
        return new CursorLoader(context,
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                MediaStoreFieldsProjection.ARTISTS_FIELDS_PROJECTION,
                null,
                null,
                MediaStore.Audio.Artists.DEFAULT_SORT_ORDER);
    }


    public static final CursorLoader createAlbumCursorLoader(Context context, long albumId) {
        String selection = MediaStore.Audio.Albums._ID + "=?";
        String[] selectionArgs = new String[] { Long.toString(albumId) };

        return new CursorLoader(context,
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                MediaStoreFieldsProjection.ALBUM_FIELDS_PROJECTION,
                selection,
                selectionArgs,
                MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
    }

    public static final CursorLoader createAlbumListCursorLoader(Context context) {
        return new CursorLoader(context,
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                MediaStoreFieldsProjection.ALBUM_FIELDS_PROJECTION,
                null,
                null,
                MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
    }

    public static final CursorLoader createAlbumListCursorLoader(Context context, long artistId) {
        return new CursorLoader(context,
                MediaStore.Audio.Artists.Albums.getContentUri("external", artistId),
                MediaStoreFieldsProjection.ALBUM_FIELDS_PROJECTION,
                null,
                null,
                MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
    }


    public static final CursorLoader createTrackCursorLoader(Context context, long trackId) {
        String selection = MediaStore.Audio.Media._ID + "=?";
        String[] selectionArgs = new String[] { Long.toString(trackId) };

        return new CursorLoader(context,
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                MediaStoreFieldsProjection.TRACK_FIELDS_PROJECTION,
                selection,
                selectionArgs,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
    }

    public static final CursorLoader createTrackListCursorLoader(Context context) {
        String selection = MediaStore.Audio.Media.IS_MUSIC + "=1";

        return new CursorLoader(context,
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                MediaStoreFieldsProjection.TRACK_FIELDS_PROJECTION,
                selection,
                null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
    }

    public static final CursorLoader createTrackListCursorLoader(Context context, long albumId) {
        String selection = MediaStore.Audio.Media.ALBUM_ID + "=?";
        String[] selectionArgs = new String[] { Long.toString(albumId) };

        return new CursorLoader(context,
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                MediaStoreFieldsProjection.TRACK_FIELDS_PROJECTION,
                selection,
                selectionArgs,
                MediaStore.Audio.Media.TRACK);
    }
}

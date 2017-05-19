package net.hogelab.musicbrowser.model;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

/**
 * Created by kobayasi on 2016/04/12.
 */
public final class AudioMediaStoreCursorFactory {

    public static CursorLoader createArtistCursorLoader(Context context, String artistId) {
        String selection = MediaStore.Audio.Artists._ID + "=?";
        String[] selectionArgs = new String[] { artistId };

        return new CursorLoader(context,
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                MediaStoreFieldsProjection.ARTIST_FIELDS_PROJECTION,
                selection,
                selectionArgs,
                MediaStore.Audio.Artists.DEFAULT_SORT_ORDER);
    }

    public static CursorLoader createAllArtistListCursorLoader(Context context) {
        return new CursorLoader(context,
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                MediaStoreFieldsProjection.ARTIST_FIELDS_PROJECTION,
                null,
                null,
                MediaStore.Audio.Artists.DEFAULT_SORT_ORDER);
    }


    public static CursorLoader createAlbumCursorLoader(Context context, String albumId) {
        String selection = MediaStore.Audio.Albums._ID + "=?";
        String[] selectionArgs = new String[] { albumId };

        return new CursorLoader(context,
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                MediaStoreFieldsProjection.ALBUM_FIELDS_PROJECTION,
                selection,
                selectionArgs,
                MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
    }

    public static CursorLoader createAllAlbumListCursorLoader(Context context) {
        return new CursorLoader(context,
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                MediaStoreFieldsProjection.ALBUM_FIELDS_PROJECTION,
                null,
                null,
                MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
    }

    public static CursorLoader createArtistAlbumListCursorLoader(Context context, String artistId) {
        return new CursorLoader(context,
                MediaStore.Audio.Artists.Albums.getContentUri("external", Long.parseLong(artistId)),
                MediaStoreFieldsProjection.ALBUM_FIELDS_PROJECTION,
                null,
                null,
                MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
    }


    public static CursorLoader createTrackCursorLoader(Context context, String trackId) {
        String selection = MediaStore.Audio.Media._ID + "=?";
        String[] selectionArgs = new String[] { trackId };

        return new CursorLoader(context,
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                MediaStoreFieldsProjection.TRACK_FIELDS_PROJECTION,
                selection,
                selectionArgs,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
    }

    public static Cursor queryTrack(Context context, String trackId) {
        String selection = MediaStore.Audio.Media._ID + "=?";
        String[] selectionArgs = new String[] { trackId };

        return context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                MediaStoreFieldsProjection.TRACK_FIELDS_PROJECTION,
                selection,
                selectionArgs,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
    }

    public static CursorLoader createAllTrackListCursorLoader(Context context) {
        String selection = MediaStore.Audio.Media.IS_MUSIC + "=1";

        return new CursorLoader(context,
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                MediaStoreFieldsProjection.TRACK_FIELDS_PROJECTION,
                selection,
                null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
    }

    public static Cursor queryAllTrackList(Context context) {
        String selection = MediaStore.Audio.Media.IS_MUSIC + "=1";

        return context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                MediaStoreFieldsProjection.TRACK_FIELDS_PROJECTION,
                selection,
                null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
    }

    public static CursorLoader createAlbumTrackListCursorLoader(Context context, String albumId) {
        String selection = MediaStore.Audio.Media.ALBUM_ID + "=?";
        String[] selectionArgs = new String[] { albumId };

        return new CursorLoader(context,
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                MediaStoreFieldsProjection.TRACK_FIELDS_PROJECTION,
                selection,
                selectionArgs,
                MediaStore.Audio.Media.TRACK);
    }

    public static Cursor queryTrackList(Context context, String albumId) {
        String selection = MediaStore.Audio.Media.ALBUM_ID + "=?";
        String[] selectionArgs = new String[] { albumId };

        return context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                MediaStoreFieldsProjection.TRACK_FIELDS_PROJECTION,
                selection,
                selectionArgs,
                MediaStore.Audio.Media.TRACK);
    }
}

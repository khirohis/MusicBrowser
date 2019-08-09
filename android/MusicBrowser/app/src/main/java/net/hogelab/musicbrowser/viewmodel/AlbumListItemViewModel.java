package net.hogelab.musicbrowser.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import net.hogelab.musicbrowser.R;
import net.hogelab.musicbrowser.event.OpenAlbumEvent;
import net.hogelab.musicbrowser.view.TrackListActivity;

/**
 * Created by kobayasi on 2016/04/11.
 */
public class AlbumListItemViewModel {

    private static final String TAG = AlbumListItemViewModel.class.getSimpleName();


    private Context context;

    private String id;
    private String album;
    private String albumId;
    private String artist;
    private String albumArt;


    public AlbumListItemViewModel(Context context, Cursor cursor) {
        this.context = context;

        setupFromCursor(cursor);
    }


    public void setupFromCursor(Cursor cursor) {
        if (cursor != null) {
            setId(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID)));
            setAlbum(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM)));
            setAlbumId(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ID)));
            setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST)));

            // Android Q にて ALBUM_ART が取得できないケースへの対応
            String albumArt = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
            if (albumArt != null) {
                setAlbumArt(albumArt);
            } else {
                // を試そうとしたけど難しいのでやめた
            }
        }
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    public String getThumbnailUrl() {
        if (!TextUtils.isEmpty(albumArt)) {
            return "file://" + albumArt;
        }

        return null;
    }

    public String getThumbnailAlbumId() {
        if (TextUtils.isEmpty(albumArt)) {
            return albumId;
        }

        return null;
    }

    public int getDefaultDrawable() {
        return R.drawable.media_music;
    }

    public View.OnClickListener onClickListItem() {
        return (view) -> {
            Log.d(TAG, "onClick: " + album + "(" + id + ", " + albumId + ")");

            // TODO: 本来はViewに通知を送りViewが遷移する設計
            Intent intent = TrackListActivity.newIntent(view.getContext(), albumId);
            view.getContext().startActivity(intent);
        };
    }
}

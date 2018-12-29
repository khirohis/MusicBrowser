package net.hogelab.musicbrowser.player;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaSessionCompat;

import net.hogelab.musicbrowser.model.AudioMediaStoreCursorFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by kobayasi on 2017/05/12.
 */

public class AudioMediaStoreQueueFactory extends BasePlayerQueueFactory {

    @Override
    public List<MediaSessionCompat.QueueItem> createQueueFromMediaId(Context context, String mediaId, Bundle extras) {
        List<MediaSessionCompat.QueueItem> queue = new LinkedList<>();

        Cursor cursor;
//        String artistId = ;

        cursor = createTrackCursorFromMediaId(context, mediaId);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                queue.add(newQueueItem(mediaId, cursor));
            }

            cursor.close();
        }

        return queue;
    }


    protected Cursor createTrackCursorFromMediaId(Context context, String mediaId) {
        return AudioMediaStoreCursorFactory.queryTrack(context, mediaId);
    }


    protected MediaSessionCompat.QueueItem newQueueItem(String mediaId, Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
        String data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
        Uri uri = Uri.parse(data);
        MediaDescriptionCompat description = new MediaDescriptionCompat.Builder()
                .setMediaId(mediaId)
                .setTitle(title)
                .setMediaUri(uri)
                .build();
        return new MediaSessionCompat.QueueItem(description, id);
    }
}

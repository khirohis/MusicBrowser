package net.hogelab.musicbrowser.event;

/**
 * Created by kobayasi on 2016/04/11.
 */
public class OpenAlbumEvent {
    public final long albumId;

    public OpenAlbumEvent(long albumId) {
        this.albumId = albumId;
    }
}

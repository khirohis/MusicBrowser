package net.hogelab.musicbrowser.event;

/**
 * Created by kobayasi on 2016/04/11.
 */
public class OpenAlbumEvent {
    public final String albumId;

    public OpenAlbumEvent(String albumId) {
        this.albumId = albumId;
    }
}

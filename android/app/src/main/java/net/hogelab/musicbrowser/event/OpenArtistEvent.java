package net.hogelab.musicbrowser.event;

/**
 * Created by kobayasi on 2016/04/11.
 */
public class OpenArtistEvent {
    public final long artistId;

    public OpenArtistEvent(long artistId) {
        this.artistId = artistId;
    }
}

package net.hogelab.musicbrowser.event;

/**
 * Created by kobayasi on 2016/04/11.
 */
public class OpenArtistEvent {
    public final String artistId;

    public OpenArtistEvent(String artistId) {
        this.artistId = artistId;
    }
}

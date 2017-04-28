package net.hogelab.musicbrowser.event;

/**
 * Created by kobayasi on 2016/04/14.
 */
public class PlayTrackEvent {
    public final String trackId;

    public PlayTrackEvent(String trackId) {
        this.trackId = trackId;
    }
}

package net.hogelab.musicbrowser.event;

/**
 * Created by kobayasi on 2016/04/14.
 */
public class OpenTrackEvent {
    public final long trackId;

    public OpenTrackEvent(long trackId) {
        this.trackId = trackId;
    }
}

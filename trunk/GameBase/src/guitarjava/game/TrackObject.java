package guitarjava.game;

import java.awt.Color;

/**
 * Represents an object that belongs to the track.
 * @author lucasjadami
 */
public abstract class TrackObject extends GameObject
{
    public static final int TRACK_SPACEMENT = 70;
    public static final int OBJECT_SIZE = 60;

    private int track;

    /**
     * @param track The track.
     * @param y Position y.
     * @param z Position z.
     */
    public TrackObject(int track, int y, int z)
    {
        this(track, 0f, y, z);
    }

    /**
     * @param track The track.
     * @param spacement Adjustable spacement for objects with width smaller than the track object size.
     * @param y Position y.
     * @param z Position z.
     */
    public TrackObject(int track, float spacement, int y, int z)
    {
        super(TRACK_SPACEMENT * (track + 1) + spacement, y, 1, TrackObject.getColorByTrack(track));

        this.track = track;
    }

    /**
     * @param track The track.
     * @param spacement Adjustable spacement for objects with width smaller than the track object size.
     * @param y Position y.
     * @param z Position z.
     */
    public TrackObject(int track, float spacement, int y, int z, Color color)
    {
        super(TRACK_SPACEMENT * (track + 1) + spacement, y, 1, color);

        this.track = track;
    }

    public abstract void think(double deltaTime);

    /**
     * @return The track.
     */
    public int getTrack()
    {
        return track;
    }

    /**
     * @param track The track.
     * @return A different color for each track.
     */
    private static Color getColorByTrack(int track)
    {
        switch (track)
        {
            case 0:
                return Color.BLUE;
            case 1:
                return Color.GREEN;
            case 2:
                return Color.ORANGE;
            case 3:
                return Color.RED;
            case 4:
                return Color.YELLOW;
            default:
                return Color.BLACK;
        }
    }
}

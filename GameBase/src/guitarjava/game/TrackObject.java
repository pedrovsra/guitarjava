package guitarjava.game;

import java.awt.Color;

/**
 * Represents an object that belongs to the track.
 * @author lucasjadami
 */
public abstract class TrackObject extends GameObject
{
    public static final int TRACK_SPACEMENT = 70;
    public static final int DEFAULT_OBJECT_SIZE = 60;
    public static final int BURNING_POSITION_Y = 490;
    private static final float START_X = (Constant.WINDOW_WIDTH - TRACK_SPACEMENT * 6) / 2;
    public static final float TRACK_DEFAULT_SPEED = .4f;

    protected int track;

    /**
     * @param track The track.
     * @param horizontalSize Adjustable spacement for objects with width smaller than the track object size.
     * @param y Position y.
     * @param z Position z.
     */
    public TrackObject(int track, float y, float z, float width, float height)
    {
        this(track, y, z, width, height, getColorByTrack(track));
    }

    /**
     * @param track The track.
     * @param horizontalSize Adjustable spacement for objects with width smaller than the track object size.
     * @param y Position y.
     * @param z Position z.
     */
    public TrackObject(int track, float y, float z, float width, float height, Color color)
    {
        super(START_X + TRACK_SPACEMENT * (track + 1), y, z, width, height, color);

        this.track = track;
    }

    public abstract void think(float deltaTime);

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
    protected static Color getColorByTrack(int track)
    {
        switch (track)
        {
            case 0:
                return Color.GREEN;
            case 1:
                return Color.RED;
            case 2:
                return Color.YELLOW;
            case 3:
                return Color.BLUE;
            case 4:
                return Color.MAGENTA;
            default:
                return Color.BLACK;
        }
    }
}

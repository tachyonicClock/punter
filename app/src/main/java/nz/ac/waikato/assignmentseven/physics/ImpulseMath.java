package nz.ac.waikato.assignmentseven.physics;

public class ImpulseMath {

    public static final float BIAS_RELATIVE = 0.95f;
    public static final float BIAS_ABSOLUTE = 0.01f;

    public static float clamp( float min, float max, float a )
    {
        return (a < min ? min : (Math.min(a, max)));
    }

    public static boolean gt( float a, float b )
    {
        return a >= b * BIAS_RELATIVE + a * BIAS_ABSOLUTE;
    }
}

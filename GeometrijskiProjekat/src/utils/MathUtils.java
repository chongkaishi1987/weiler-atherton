package utils;

public class MathUtils {
	public static double DOUBLE_EQUALITY_TOLERANCE = 1e-6;

	public static boolean Equal(double x, double y) {
		if (Math.abs(x - y) < DOUBLE_EQUALITY_TOLERANCE)
			return true;
		return false;
	}
}

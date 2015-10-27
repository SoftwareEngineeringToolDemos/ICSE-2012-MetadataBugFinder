package util;

public class UtilLog {
	public final static int	__OFF		= 0;
	public final static int	__ALL		= 1;
	static int					_level	= 1;

	/**
	 * @METHOD
	 */
	public static void set(int level) {
		_level = level;
	}

	/**
	 * @METHOD
	 */
	public static void w(String msg) {
		switch (_level) {
		case __OFF:
			break;
		case __ALL:
			System.out.println("[LOG] " + msg);
		}
	}
}

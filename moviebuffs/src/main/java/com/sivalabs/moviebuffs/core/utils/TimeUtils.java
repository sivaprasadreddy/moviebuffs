package com.sivalabs.moviebuffs.core.utils;

public class TimeUtils {

	public final static long ONE_SECOND = 1000;

	public final static long SECONDS = 60;

	public final static long ONE_MINUTE = ONE_SECOND * 60;

	public final static long MINUTES = 60;

	public final static long ONE_HOUR = ONE_MINUTE * 60;

	public final static long HOURS = 24;

	public final static long ONE_DAY = ONE_HOUR * 24;

	private TimeUtils() {
	}

	/**
	 * converts time (in milliseconds) to human-readable format "<w> days, <x> hours, <y>
	 * minutes and (z) seconds"
	 */
	public static String millisToLongDHMS(long duration) {
		StringBuffer res = new StringBuffer();
		long temp = 0;
		if (duration >= ONE_SECOND) {
			temp = duration / ONE_DAY;
			if (temp > 0) {
				duration -= temp * ONE_DAY;
				res.append(temp).append(" day").append(temp > 1 ? "s" : "").append(duration >= ONE_MINUTE ? ", " : "");
			}

			temp = duration / ONE_HOUR;
			if (temp > 0) {
				duration -= temp * ONE_HOUR;
				res.append(temp).append(" hour").append(temp > 1 ? "s" : "").append(duration >= ONE_MINUTE ? ", " : "");
			}

			temp = duration / ONE_MINUTE;
			if (temp > 0) {
				duration -= temp * ONE_MINUTE;
				res.append(temp).append(" minute").append(temp > 1 ? "s" : "");
			}

			temp = duration / ONE_SECOND;
			long millis = duration % ONE_SECOND;

			if (!res.toString().equals("") && duration >= ONE_SECOND) {
				if (millis < 1)
					res.append(" and ");
				else
					res.append(", ");
			}

			if (temp > 0) {
				res.append(temp).append(" second").append(temp > 1 ? "s" : "");
			}
			if (millis > 0) {
				res.append(" and " + millis + " millis");
			}
			return res.toString();
		}
		else {
			return duration + " millis";
		}
	}

	public static void main(String args[]) {
		System.out.println(((5 * ONE_SECOND) + 123) % 1000);
		System.out.println(millisToLongDHMS(123));
		System.out.println(millisToLongDHMS((5 * ONE_SECOND) + 123));
		System.out.println(millisToLongDHMS(ONE_DAY + ONE_HOUR));
		System.out.println(millisToLongDHMS(ONE_DAY + 2 * ONE_SECOND));
		System.out.println(millisToLongDHMS(ONE_DAY + ONE_HOUR + (2 * ONE_MINUTE)));
		System.out.println(millisToLongDHMS((4 * ONE_DAY) + (3 * ONE_HOUR) + (2 * ONE_MINUTE) + ONE_SECOND));
		System.out.println(millisToLongDHMS((5 * ONE_DAY) + (4 * ONE_HOUR) + ONE_MINUTE + (23 * ONE_SECOND) + 123));
		System.out.println(millisToLongDHMS(42 * ONE_DAY));
	}

}

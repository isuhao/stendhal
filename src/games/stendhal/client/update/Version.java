/* $Id$
 */
package games.stendhal.client.update;

/**
 * maintains the current version
 *
 * @author hendrik
 */
public class Version {

	public static final String VERSION = "0.58.5";

	/**
	 * Extract the specified number of parts from a version-string.
	 *
	 * @param version version-string
	 * @param parts number of parts to extract
	 * @return parts of the version-string
	 */
	public static String cut(String version, int parts) {
		int pos = 0;
		for (int i = 0; i < parts; i++) {
			int temp = version.indexOf(".", pos + 1);
			if (temp < 0) {
				pos = version.length();
				break;
			}
			pos = temp;
		}
		return version.substring(0, pos);
	}

	/**
	 * compares to versions
	 *
	 * @param v1 1st version string
	 * @param v2 2nd version string
	 * @return see compare
	 */
	public static int compare(String v1, String v2) {
		String version1 = v1;
		String version2 = v2;
		while (!version1.equals("") || !version2.equals("")) {
			// split version string at the first dot into the current
			// component and the rest of the version
			String component1;
			int pos1 = version1.indexOf(".");
			if (pos1 > -1) {
				component1 = version1.substring(0, pos1);
				version1 = version1.substring(pos1 + 1);
			} else {
				component1 = version1;
				version1 = "";
			}
			if (component1.equals("")) {
				component1 = "0";
			}

			String component2;
			int pos2 = version2.indexOf(".");
			if (pos2 > -1) {
				component2 = version2.substring(0, pos2);
				version2 = version2.substring(pos2 + 1);
			} else {
				component2 = version2;
				version2 = "";
			}
			if (component2.equals("")) {
				component2 = "0";
			}

			// if the current component of both version is equal,
			// we have to have a look at the next one. Otherwise
			// we return the result of this comparison.
			int res = 0;
			try {
				// try an integer comparison so that 2 < 13
				int componentInt1 = Integer.parseInt(component1.trim());
				int componentInt2 = Integer.parseInt(component2.trim());
				res = componentInt1 - componentInt2;
			} catch (NumberFormatException e) {
				// integer comparison failed because one component is not a
				// number. Do a string comparison.
				res = component1.compareTo(component2);
			}
			if (res != 0) {
				return res;
			}
		}
		return 0;
	}

	public static boolean checkCompatibility(String v1, String v2) {
		String ev1 = cut(v1, 2);
		String ev2 = cut(v2, 2);
		boolean res = ev1.equals(ev2);
		return res;
	}

	private Version() {
		// hide constructor; this is a static class
	}

}

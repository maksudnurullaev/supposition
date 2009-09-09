
public class Class4Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String line = "111111111111 =";
		int found = line.indexOf("=");
		if (found != -1) {
			String curKey = line.substring(0, found - 1);
			String curValue = line.substring(found + 1, line.length());

			curValue = null;
			
			System.out.println(curKey);
			System.out.println(curValue);
		}
	}

}

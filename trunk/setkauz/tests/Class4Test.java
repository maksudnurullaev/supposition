import org.supposition.utils.CryptoManager;



public class Class4Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String salt1 = CryptoManager.encryptPassword("inPassword");
		System.out.println(salt1);
		String salt2 = CryptoManager.encryptPassword("inPassword");
		System.out.println(salt2);
		
		System.out.println(CryptoManager.checkPassword("inPassword", salt1));
		System.out.println(CryptoManager.checkPassword("inPassword", salt2));
	}

}

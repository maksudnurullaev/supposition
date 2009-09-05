import org.supposition.db.proxy.UserProxy;

public class Class4Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		UserProxy users = new UserProxy();
		
		System.out.println("Users count" + users.getAll().size());

	}

}

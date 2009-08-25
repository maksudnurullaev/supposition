import org.apache.cayenne.access.DataContext;
import org.supposition.db.User;
import org.supposition.utils.DBUtils;



public class Class4Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DataContext _context = DBUtils.getInstance().getDBContext();
		
		User user = null;
		for (int i = 0; i < 100; i++) {
			user = _context.newObject(User.class);
			user.setMail(String.format("test_user%d@admin.com", i));
			user.setAdditionals("For Test Pro");
			user.setPassword("test");	
			user.postValidationSave();
		}
		
		_context.commitChanges();
	}

}

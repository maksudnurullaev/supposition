import org.apache.cayenne.access.DataContext;
import org.supposition.db.Role;
import org.supposition.utils.DBUtils;


public class Class4Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DataContext _context = DBUtils.getInstance().getDBContext();
		Role  role = (Role) _context.newObject(Role.class);
		role.setName("test333");
		_context.commitChanges();		
	}

}

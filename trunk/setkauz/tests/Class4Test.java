import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.validation.ValidationResult;
import org.supposition.db.Cgroup;
import org.supposition.utils.DBUtils;



public class Class4Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DataContext _context = DBUtils.getInstance().getDBContext();

		Cgroup cgroup1 = (Cgroup) _context.newObject(Cgroup.class);
		
		cgroup1.setName("First Element");
		
		ValidationResult validationResult = cgroup1.getValidationResult();
		
		System.out.println(" validationResult has " + validationResult.getFailures().size() + " fails");
				
		// ###

		Cgroup cgroup2 = (Cgroup) _context.newObject(Cgroup.class);
		
		cgroup2.setName("Second Element");
		
		validationResult = cgroup2.getValidationResult();
		
		System.out.println(" validationResult has " + validationResult.getFailures().size() + " fails");
		
		_context.commitChanges();
		
		cgroup1.addToChilds(cgroup2);
		
		_context.commitChanges();
		
		System.out.println("Changes commited");				
	}

}

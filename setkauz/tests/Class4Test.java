import org.apache.cayenne.validation.ValidationFailure;
import org.apache.cayenne.validation.ValidationResult;
import org.supposition.db.Default;
import org.supposition.db.proxy.Defaults;
import org.supposition.utils.Constants;



public class Class4Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Defaults defaults = new Defaults();
		Default keyValue = null;
		
		try {
			keyValue = defaults.createNew();
		} catch (Exception e) {
			System.out.println("errors.could.not.create.dbobject");
		}
		
		keyValue.setKey("test");
		keyValue.setValue("testvalue");
		
		ValidationResult validationResult = new ValidationResult(); 		
		keyValue.validateForSave(validationResult);
		
		if(validationResult.hasFailures()){
			System.out.println("### Validation Failed ###");
			for(ValidationFailure fail: validationResult.getFailures()){
				System.out.println("Fails: " + fail.getDescription());
			}			
			System.out.println(Constants._web_error_result_prefix);
		}		
		
		defaults.commitChanges();
		
		System.out.println(Constants._web_ok_result_prefix);	
	}

}

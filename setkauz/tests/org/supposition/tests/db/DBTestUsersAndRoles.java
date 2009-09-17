package org.supposition.tests.db;

import java.util.List;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.validation.ValidationFailure;
import org.apache.cayenne.validation.ValidationResult;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.supposition.db.Role;
import org.supposition.db.User;
import org.supposition.db.proxy.UserProxy;
import org.supposition.utils.DBUtils;
import org.supposition.utils.MessagesManager;
import org.supposition.utils.Utils;

public class DBTestUsersAndRoles {
	@BeforeClass
	public static void start() {
		delete_test_objects();		
	}
	
	@AfterClass
	public static void finish(){
		//delete_test_objects();
	}	

	@Test
	public void test_0_simple() {
		DataContext _context = DBUtils.getInstance().getDBContext();
		User user = (User) _context.newObject(User.class); 
		Assert.assertNotNull(user);
		Assert.assertTrue(user instanceof User);
	}	
	
	@Test
	public void test_1_simple_objects() {
		DataContext _context = DBUtils.getInstance().getDBContext();
		User user = (User) _context.newObject(User.class);
		user.check4Kaptcha(false);
		
		user.setMail("test_user_1@admin.com");
		user.setPassword("test_password");	
		
		ValidationResult validationResult = user.getValidationResult();
		Assert.assertNotNull(validationResult);
		Assert.assertFalse(validationResult.hasFailures());
		user.postValidationSave();
		
		Assert.assertNotNull(user.getSalt());
		Assert.assertNotNull(user.getCreated());
		Assert.assertNotNull(user.getStatus());
		
		Assert.assertTrue(Utils.isValidString(user.getSalt()));
		Assert.assertTrue(Utils.isValidString(user.getCreated()));
		Assert.assertTrue(Utils.isValidString(user.getStatus()));
		
		_context.commitChanges();
	}	
	

	@Test
	public void test_password_setting() {
		// New password
		String password = "Strong password";
		String password2 = "Strong password2";
		// User-Admin
		
		DataContext _context = DBUtils.getInstance().getDBContext();
		User user = (User) _context.newObject(User.class); 
		user.check4Kaptcha(false);
		
		user.setMail("test_user_password@test.com");
		user.setPassword(password);
		
		ValidationResult validationResult = user.getValidationResult();
		
		Assert.assertFalse(validationResult.hasFailures());
		user.postValidationSave();
		
		_context.commitChanges();
		
		Assert.assertTrue(user.getPassword() == "");
		Assert.assertTrue(user.getStatus().equalsIgnoreCase(MessagesManager.getDefault("password.salted")));
		Assert.assertTrue(user.checkPassword(password));
		
		// Change password
		user.setPassword(password2);

		validationResult = user.getValidationResult();		
		Assert.assertFalse(validationResult.hasFailures());

		user.postValidationSave();
		_context.commitChanges();
		
		Assert.assertTrue(user.getPassword() == "");
		Assert.assertTrue(user.getStatus().equalsIgnoreCase(MessagesManager.getDefault("password.salted")));
		Assert.assertTrue(user.checkPassword(password2));
	}
	
	@Test
	public void test_add_users_and_roles() {
		ValidationResult validationResult = new  ValidationResult();
		
		DataContext _context = DBUtils.getInstance().getDBContext();
		User userAdmin = (User) _context.newObject(User.class); 
		User userClient = (User) _context.newObject(User.class);
		
		userClient.check4Kaptcha(false);
		userAdmin.check4Kaptcha(false);
		
		userAdmin.setMail("test_user_role_admin@user.com");
		userAdmin.setPassword("test_admin_password");
		
		validationResult = userAdmin.getValidationResult();
		Assert.assertFalse(validationResult.hasFailures());

		// User-Client
		userClient.setMail("test_user_client@client.com");
		userClient.setPassword("test_client_password");

		userClient.getValidationResult();
		Assert.assertFalse(validationResult.hasFailures());
		
		// Role-Admin
		Role roleAdmin = (Role) _context.newObject(Role.class);
		roleAdmin.setName("test_role_admin");
		validationResult = roleAdmin.getValidationResult();
		Assert.assertFalse(validationResult.hasFailures());

		// Role-Agent
		Role roleAgent = (Role) _context.newObject(Role.class);
		roleAgent.setName("test_role_agent");
		validationResult = roleAgent.getValidationResult();
		Assert.assertFalse(validationResult.hasFailures());

		// Role-Client
		Role roleClient = (Role) _context.newObject(Role.class);
		roleClient.setName("test_role_client");
		validationResult = roleClient.getValidationResult();
		Assert.assertFalse(validationResult.hasFailures());
		
		_context.commitChanges();

		// ### Bind Users & Roles
		roleAdmin.addToUsers(userAdmin);
		_context.commitChanges();

		userClient.addToRoles(roleClient);
		roleAgent.addToUsers(userClient);
		_context.commitChanges();

		// ### DELETE USERS & ROLES
		_context.deleteObject(roleAdmin);

		_context.deleteObject(roleAgent);

		_context.deleteObject(roleClient);

		_context.deleteObject(userAdmin);

		_context.deleteObject(userClient);

		_context.commitChanges();
	}	
	
	// Test exceptions
	@Test
	public void test_empty_fields_and_kaptcha() throws Exception{
		DataContext _context = DBUtils.getInstance().getDBContext();
		User user = (User) _context.newObject(User.class); 
		user.check4Kaptcha(false);
		
		user.setMail("");
		user.setPassword("");
		
		ValidationResult validationResult = user.getValidationResult();
		Assert.assertTrue(validationResult.hasFailures());
		
		// 1. Test invalid mail
		user.setMail("test_user_empty_fields#test.com");
		user.setPassword("test_invalid_mail");
		
		validationResult = user.getValidationResult();		
				
		Assert.assertTrue(validationResult.hasFailures());
		Assert.assertTrue(validationResult.getFailures().size() == 1);
		
		Assert.assertTrue(((ValidationFailure)validationResult.getFailures().get(0)).getDescription().equals("errors.invalid.mail"));
		
		// 2. Test invalid kaptcha
		user.setMail("test_invalid_mail@test.com");
		user.setPassword("test_invalid_mail");
		user.setKaptcha(null);
		user.check4Kaptcha(true);
		
		validationResult = user.getValidationResult();		
				
		Assert.assertTrue(validationResult.hasFailures());
		Assert.assertTrue(validationResult.getFailures().size() == 1);
		
		Assert.assertTrue(((ValidationFailure)validationResult.getFailures().get(0)).getDescription().equals("errors.null.object"));
		
		// 3. Test invalid/empty password
		user.setMail("test_invalid_mail@test.com");
		user.setPassword("");
		user.check4Kaptcha(false);
		
		validationResult = user.getValidationResult();		
				
		Assert.assertTrue(validationResult.hasFailures());
		Assert.assertTrue(validationResult.getFailures().size() == 1);
		
		Assert.assertTrue(((ValidationFailure)validationResult.getFailures().get(0)).getDescription().equals("errors.passwords.is.empty"));
		
		// 4. Test invalid/short password
		user.setMail("test_invalid_mail@test.com");
		user.setPassword("123");
		user.check4Kaptcha(false);
		
		validationResult = user.getValidationResult();		
				
		Assert.assertTrue(validationResult.hasFailures());
		Assert.assertTrue(validationResult.getFailures().size() == 1);
		
		Assert.assertTrue(((ValidationFailure)validationResult.getFailures().get(0)).getDescription().equals("errors.invalid.password.length"));		
		
		// 5. Test when valid everythings
		user.setMail("est_user_valid_mail@test.com");
		user.setPassword("test_password");
		
		validationResult = user.getValidationResult();		
		
		Assert.assertFalse(validationResult.hasFailures());
	}		
	
	private static void delete_test_objects() {
		UserProxy users_proxy = new UserProxy();
		
		users_proxy.setPageSize(0);
		users_proxy.addExpression(ExpressionFactory.likeIgnoreCaseExp("mail", "test_user_%"));
		
		List<User> users = users_proxy.getAll();
		
		if (users.size() > 0) {
			users_proxy.deleteObjects(users);
			users_proxy.commitChanges();
		}
	}	
	
	
}

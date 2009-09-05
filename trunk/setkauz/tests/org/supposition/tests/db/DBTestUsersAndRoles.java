package org.supposition.tests.db;

import java.util.List;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.validation.ValidationResult;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.supposition.db.Role;
import org.supposition.db.User;
import org.supposition.db.proxy.UserProxy;
import org.supposition.utils.Constants;
import org.supposition.utils.DBUtils;

public class DBTestUsersAndRoles {
	static int generate_test_objects_count = 1;
	static DataContext _context = DBUtils.getInstance().getDBContext();
	
	private static String oldPassword = "password";
	

	@BeforeClass
	public static void start() {
		delete_test_objects();		
		create_test_objects(generate_test_objects_count);
	}
	
	@AfterClass
	public static void finish(){
		delete_test_objects();
	}	

	public static void create_test_objects(int inCount) {
		User user = null;
		for (int i = 0; i < inCount; i++) {
			user = _context.newObject(User.class);
			user.setMail(String.format("test_user%d@admin.com", i));
			user.setPassword(oldPassword);	
		}
		// batch update
		_context.commitChanges();
	}	
	
	private static void delete_test_objects() {
		UserProxy users_proxy = new UserProxy();
		
		users_proxy.setPageSize(0);
		users_proxy.addExpression(ExpressionFactory.likeIgnoreCaseDbExp("Mail",
				"test%"));
		List<User> users = users_proxy.getAll();
		if (users.size() > 0) {
			users_proxy.deleteObjects(users);
			users_proxy.commitChanges();
		}
	}	
	
	@Test
	public void test_simple() {
		User user = new User();
		Assert.assertNotNull(user);
		Assert.assertTrue(user instanceof User);
	}

	@Test
	public void test_password_setting() {
		
		// New password
		String password = "Strong password";
		String password2 = "Strong password2";
		// User-Admin
		
		User user = (User) _context.newObject(User.class);
		user.setMail("testme@test.com");
		user.setPassword(password);
		user.setKaptcha(Constants._testing_string);
		
		ValidationResult validationResult = user.getValidationResult();
		
		Assert.assertFalse(validationResult.hasFailures());
		
		user.postValidationSave();
		_context.commitChanges();
		
		Assert.assertTrue(user.getPassword() == "");
		Assert.assertTrue(user.getStatus() == Constants._password_salted);
		Assert.assertTrue(user.checkPassword(password));
		
		// Change password
		user.setPassword(password2);

		validationResult = user.getValidationResult();		
		Assert.assertFalse(validationResult.hasFailures());

		user.postValidationSave();
		_context.commitChanges();
		
		Assert.assertTrue(user.getPassword() == "");
		Assert.assertTrue(user.getStatus() == Constants._password_salted);
		Assert.assertTrue(user.checkPassword(password2));

	}

	@Test
	public void test_add_users_and_roles() {
		// ### CREATE USERS & ROLES
		// User-Admin
		User userAdmin = _context.newObject(User.class);
		userAdmin.setMail("test@user.com");
		userAdmin.setPassword("test_admin_password");

		// User-Client
		User userClient = (User) _context.newObject(User.class);
		userClient.setMail("test_client@client.com");
		userClient.setPassword("test_client_password");
		// Role-Admin
		Role roleAdmin = (Role) _context.newObject(Role.class);
		roleAdmin.setName("Test_Role_Admin");
		// Role-Agent
		Role roleAgent = (Role) _context.newObject(Role.class);
		roleAgent.setName("Test_Role_Agent");
		// Role-Client
		Role roleClient = (Role) _context.newObject(Role.class);
		roleClient.setName("Test_Role_Client");

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
	public void test_empty_fields() throws Exception{
		UserProxy users = new UserProxy();
		
		User user = users.createNew();
		user.setMail("");
		user.setPassword("");
		user.setKaptcha(Constants._testing_string);
		
		ValidationResult validationResult = user.getValidationResult();
		Assert.assertTrue(validationResult.hasFailures());
		Assert.assertTrue(validationResult.getFailures().size() == 2);
	}
	
	@Test
	public void test_invalid_user_mail() {
		DataContext _context = DBUtils.getInstance().getDBContext();		
		// User-Admin
		User user = (User) _context.newObject(User.class);
		user.setMail("test_invalid_mail#test.com");
		user.setPassword("test_invalid_mail");
		user.setKaptcha(Constants._testing_string);
		
		ValidationResult validationResult = user.getValidationResult();		
		
		Assert.assertTrue(validationResult.hasFailures());
		Assert.assertTrue(validationResult.getFailures().size() == 1);
		Assert.assertTrue(validationResult.getFailures().get(0).getDescription() == "errors.invalid.mail");		
	}	
}

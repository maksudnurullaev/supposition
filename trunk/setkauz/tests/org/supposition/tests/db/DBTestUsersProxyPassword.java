package org.supposition.tests.db;

import java.util.List;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.validation.ValidationResult;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.supposition.db.User;
import org.supposition.db.proxy.Users;
import org.supposition.utils.DBUtils;

/**
 * To use this test we should create 1000 test users
 * 
 * @see Create100TestUsers
 * @author M.Nurullayev
 */
public class DBTestUsersProxyPassword {

	static DataContext _context = DBUtils.getInstance().getDBContext();
	static int generate_test_dbobjects_count = 1;
	private static String oldPassword = "password";
	
	private static String newPassword = "newPassword";
	private static String newPassword2 = "newPassword2";
	
	
	@BeforeClass
	public static void start() {
		delete_test_users();		
		create_test_users(generate_test_dbobjects_count);
	}

	@AfterClass
	public static void end(){
		delete_test_users();
	}
	
	@Test
	public void testSimple() throws Exception {
		Users users = new Users();
		User user = users.createNew();
		Assert.assertTrue(user instanceof User);
	}

	@Test
	public void testOldPassword() {
		Users users = new Users();
		List<User> usersList = users.getAll();
		
		// Check oldPassword
		User user = usersList.get(0);
		Assert.assertTrue(user.checkPassword(oldPassword));		
	}

	@Test
	public void testNewPassword() {
		Users users = new Users();
		List<User> usersList = users.getAll();
		
		User user = usersList.get(0);
		
		ValidationResult validationResult = new ValidationResult();
		
		// Set newPassword	
		user.setPassword(newPassword);
		
		user.validateForUpdate(validationResult);		
		if(!validationResult.hasFailures())
			_context.commitChanges();
		
		// Check newPassword
		Assert.assertTrue(user.checkPassword(newPassword));		

		// Set newPassword2
		validationResult = new ValidationResult();
		user.setPassword(newPassword2);
		user.validateForUpdate(validationResult);		
		if(!validationResult.hasFailures())
			_context.commitChanges();
		
		// Check newPassword2
		Assert.assertTrue(user.checkPassword(newPassword2));		
	}

	public static void create_test_users(int inCount) {
		User user = null;
		for (int i = 0; i < inCount; i++) {
			user = _context.newObject(User.class);
			user.setMail(String.format("test_user%d@admin.com", i));
			user.setPassword(oldPassword);	
		}
		// batch update
		_context.commitChanges();
	}

	private static void delete_test_users() {
		Users users_proxy = new Users();
		
		users_proxy.setPageSize(0);
		users_proxy.addExpression(ExpressionFactory.likeIgnoreCaseDbExp("Mail",
				"test%"));
		List<User> users = users_proxy.getAll();
		System.out.println("Count = " + users.size());
		if (users.size() > 0) {
			users_proxy.deleteObjects(users);
			users_proxy.commitChanges();
		}
	}
}

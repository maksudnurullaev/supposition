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
import org.supposition.db.proxy.UserProxy;
import org.supposition.utils.DBUtils;
import org.supposition.utils.MessagesManager;

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
		UserProxy users = new UserProxy();
		User user = users.createNew();
		Assert.assertTrue(user instanceof User);
	}

	@Test
	public void testOldPassword() {
		UserProxy users = new UserProxy();
		
		users.addExpression(ExpressionFactory.likeIgnoreCaseExp("Mail","test%"));
		List<User> usersList = users.getAll();
		
		// Check oldPassword
		User user = usersList.get(0);
		Assert.assertTrue(user.checkPassword(oldPassword));		
	}

	@Test
	public void testNewPassword() {
		UserProxy users = new UserProxy();
		
		users.addExpression(ExpressionFactory.likeIgnoreCaseExp("Mail","test%"));

		List<User> usersList = users.getAll();
		
		User user = usersList.get(0);	
		
		// Set newPassword	
		user.setKaptcha(MessagesManager.getDefault("testing.string"));
		user.setPassword(newPassword);
		
		ValidationResult validationResult = user.getValidationResult(true);		
		if(!validationResult.hasFailures()){
			user.postValidationSave();
			_context.commitChanges();
		}
		
		// Check newPassword
		Assert.assertTrue(user.checkPassword(newPassword));		

		// Set newPassword2
		user.setPassword(newPassword2);
		validationResult = user.getValidationResult(true);		
		if(!validationResult.hasFailures()){
			user.postValidationSave();
			_context.commitChanges();
		}
		
		// Check newPassword2
		Assert.assertTrue(user.checkPassword(newPassword2));		
	}

	public static void create_test_users(int inCount) {
		User user = null;
		for (int i = 0; i < inCount; i++) {
			user = (User) _context.newObject(User.class);
			user.setMail(String.format("test_user%d@admin.com", i));
			user.setPassword(oldPassword);	
			user.postValidationSave();
		}
		_context.commitChanges();
	}

	private static void delete_test_users() {
		UserProxy users_proxy = new UserProxy();
		
		users_proxy.setPageSize(0);
		users_proxy.addExpression(ExpressionFactory.likeIgnoreCaseExp("Mail","test%"));
		List<User> users = users_proxy.getAll();
		if (users.size() > 0) {
			users_proxy.deleteObjects(users);
			users_proxy.commitChanges();
		}
	}
}

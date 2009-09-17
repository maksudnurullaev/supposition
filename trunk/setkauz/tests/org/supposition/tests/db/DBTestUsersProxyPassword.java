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

/**
 * To use this test we should create 1000 test users
 * 
 * @see Create100TestUsers
 * @author M.Nurullayev
 */
public class DBTestUsersProxyPassword {	
	
	@BeforeClass
	public static void start() {
		delete_test_users();		
	}

	@AfterClass
	public static void end(){
		delete_test_users();
	}
	
	@Test
	public void test_1_old_password() {
		DataContext _context = DBUtils.getInstance().getDBContext();
		User user = (User) _context.newObject(User.class); 
		user.check4Kaptcha(false);
		
		user.setMail("test_user_password@test.com");
		user.setPassword("1234");		
				
		ValidationResult validationResult = user.getValidationResult();
		Assert.assertFalse(validationResult.hasFailures());
		user.postValidationSave();
		_context.commitChanges();		
		
		// 1. Check old password
		UserProxy users = new UserProxy();
		users.addExpression(ExpressionFactory.matchDbExp("mail","test_user_password@test.com"));
		List<User> usersList = users.getAll();
		
		Assert.assertNotNull(usersList);
		Assert.assertTrue(usersList.size() == 1);
		Assert.assertEquals(user.getMail(), usersList.get(0).getMail());
		Assert.assertTrue(user.checkPassword("1234"));
		Assert.assertFalse(user.checkPassword("123_4"));

		// 2. Change password
		user.setPassword("12345");
		
		validationResult = user.getValidationResult();
		Assert.assertFalse(validationResult.hasFailures());
		user.postValidationSave();
		_context.commitChanges();
		
		// 3. Check for new password
		Assert.assertTrue(user.checkPassword("12345"));		
	}

	private static void delete_test_users() {
		UserProxy users_proxy = new UserProxy();
		
		users_proxy.setPageSize(0);
		users_proxy.addExpression(ExpressionFactory.likeIgnoreCaseExp("mail","test_user_%"));
		List<User> users = users_proxy.getAll();
		if (users != null && users.size() > 0) {
			users_proxy.deleteObjects(users);
			users_proxy.commitChanges();
		}
	}
}

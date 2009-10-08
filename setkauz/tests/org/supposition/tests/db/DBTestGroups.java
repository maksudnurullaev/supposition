package org.supposition.tests.db;

import java.util.Date;
import java.util.List;

import org.apache.cayenne.CayenneDataObject;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.validation.ValidationResult;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.supposition.db.Cgroup;
import org.supposition.db.Company;
import org.supposition.db.Group;
import org.supposition.db.User;
import org.supposition.db.proxy.SimpleProxy;
import org.supposition.utils.DBUtils;
import org.supposition.utils.SessionManager;

/**
 * To use this test we should create 1000 test users
 * 
 * @author M.Nurullayev
 */
public class DBTestGroups {	
	@BeforeClass
	public static void start() {
		delete_test_objects();		
	}
	
	@AfterClass
	public static void end(){
		delete_test_objects();
	}
	
	@Test
	public void test_1_common() {
		DataContext _context = DBUtils.getInstance().getDBContext();
		
		// 1. Create user 4 test
		User user = getTestUser(_context);	
		
		// 2. Create cgroup 4 test
		Cgroup cgroup = getTestCgroup(_context);
		
		// 3. Create company 4 test
		Company company = getTestCompany(_context, user, cgroup);
				
		// 4. Create group 4 test 
		Group group = (Group)_context.newObject(Group.class);
		group.setName("test_group_name");
		
		group.setCgroup(cgroup);
		group.addToCompanies(company);
		
		ValidationResult validationResult = group.getValidationResult();
		Assert.assertFalse(validationResult.hasFailures());
		
		_context.commitChanges();
	}

	private Company getTestCompany(DataContext _context, User user, Cgroup cgroup) {
		ValidationResult validationResult;
		Company company = (Company) _context.newObject(Company.class);
		
		company.setName("test_company_element");
		company.setCity("10%");		
		company.setUpdated(new Date());
		company.setUuuid(user.getUuid());
		company.setGuuid(cgroup.getUuid());
		
		company.setCgroup(cgroup);
		company.setUser(user);
		cgroup.addToCompanies(company);		
				
		validationResult = company.getValidationResult();	
		if(validationResult.hasFailures()){
			for(Object fail:validationResult.getFailures()){
				System.out.println(fail);
			}
		} else _context.commitChanges();				
		
		return company;
	}

	private Cgroup getTestCgroup(DataContext _context) {
		ValidationResult validationResult;
		Cgroup cgroup = (Cgroup) _context.newObject(Cgroup.class);		
		String cgroupName = "test_cgroup_element";
		cgroup.setName(cgroupName);
				
		validationResult = cgroup.getValidationResult();		
		Assert.assertFalse(validationResult.hasFailures());
		
		_context.commitChanges();
		
		return cgroup;
	}

	private User getTestUser(DataContext _context) {
		User user = (User) _context.newObject(User.class); 
		
		user.setMail("test_user_4group@test.com");
		user.setPassword("1234");
		user.check4Kaptcha(false);
		
		ValidationResult validationResult = user.getValidationResult();
		Assert.assertFalse(validationResult.hasFailures());
		user.postValidationSave();
		
		_context.commitChanges();
		
		// Login User
		SessionManager.loginUser(user);
		
		return user;
	}

	private static <E extends CayenneDataObject> void deleteWhere(SimpleProxy<E> inProxy, String pathSpec, String inFilter){
		inProxy.addExpression(ExpressionFactory.likeIgnoreCaseExp(pathSpec, inFilter));
		List<E> objectsList = inProxy.getAll();
		if(objectsList != null && objectsList.size() != 0){
			inProxy.deleteObjects(objectsList);
			inProxy.commitChanges();
		}
	}
	
	private static void delete_test_objects() {
		deleteWhere(new SimpleProxy<User>(User.class),"mail","test_user_%");
		deleteWhere(new SimpleProxy<Cgroup>(Cgroup.class),"name","test_cgroup_%");
		deleteWhere(new SimpleProxy<Company>(Company.class),"name","test_company_%");
		deleteWhere(new SimpleProxy<Group>(Group.class),"name","test_group_%");
	}
}

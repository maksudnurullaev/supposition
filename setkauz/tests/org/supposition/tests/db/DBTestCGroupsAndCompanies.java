package org.supposition.tests.db;

import java.util.Date;
import java.util.List;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.validation.ValidationResult;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.supposition.db.Cgroup;
import org.supposition.db.Company;
import org.supposition.db.User;
import org.supposition.db.proxy.CgroupProxy;
import org.supposition.db.proxy.CompanyProxy;
import org.supposition.db.proxy.UserProxy;
import org.supposition.utils.DBUtils;

/**
 * To use this test we should create 1000 test users
 * 
 * @author M.Nurullayev
 */
public class DBTestCGroupsAndCompanies {	
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
		
		// Create test user
		User user = (User) _context.newObject(User.class); 
		user.check4Kaptcha(false);
		
		user.setMail("test_user_4company@test.com");
		user.setPassword("1234");
		
		ValidationResult validationResult = user.getValidationResult();
		Assert.assertFalse(validationResult.hasFailures());
		user.postValidationSave();
		_context.commitChanges();	
		
		// Create test groups
		String group1 = "test_cgroup_element1";
		String group11 = "test_cgroup_element11";
		

		Cgroup gelement1 = (Cgroup) _context.newObject(Cgroup.class);
		Cgroup gelement11 = (Cgroup) _context.newObject(Cgroup.class);
		
		gelement1.setName(group1);
		gelement11.setName(group11);
				
		validationResult = gelement1.getValidationResult();		
		Assert.assertFalse(validationResult.hasFailures());
		validationResult = gelement11.getValidationResult();
		Assert.assertFalse(validationResult.hasFailures());
		
		_context.commitChanges();		
		
		// Create test companies
		String ename1 = "test_company_element1";
		String ename11 = "test_company_element11";
		
		Company celement1 = (Company) _context.newObject(Company.class);
		Company celement11 = (Company) _context.newObject(Company.class);
				
		celement1.setName(ename1);
		celement1.setUser(user);
		celement1.setUuuid(user.getUuid());
		celement1.setCity("test");
		celement1.setGuuid(gelement1.getUuid());
		celement1.setUpdated(new Date());
		celement1.setCgroup(gelement1);
		
		celement11.setName(ename11);
		celement11.setUser(user);
		celement11.setUuuid(user.getUuid());
		celement11.setCity("test");
		celement11.setGuuid(gelement11.getUuid());
		celement11.setUpdated(new Date());
		celement11.setCgroup(gelement1);
		
		validationResult = celement1.getValidationResult();		
		
		if(validationResult.hasFailures()){
			for(Object obj:validationResult.getFailures()){
				System.out.println(obj);
			}
		}
			
		Assert.assertFalse(validationResult.hasFailures());
		validationResult = celement11.getValidationResult();
		Assert.assertFalse(validationResult.hasFailures());
				
		Assert.assertNotNull(celement1);
		Assert.assertNotNull(celement11);
		
		Assert.assertNotNull(celement1.getUuid());
		Assert.assertNotNull(celement11.getUuid());
		
		_context.commitChanges();		
		
		gelement1.addToCompanies(celement1);
		gelement11.addToCompanies(celement11);
		
		_context.commitChanges();				
	}

	private static void delete_test_objects() {
		// Delete test user
		UserProxy users = new UserProxy();
		users.addExpression(ExpressionFactory.likeIgnoreCaseExp("mail","test_user_%"));
		List<User> usersList = users.getAll();
		if (usersList != null && usersList.size() > 0) {
			users.deleteObjects(usersList);
			users.commitChanges();
		}		
		
		// Delete test Groups
		CgroupProxy cgroups = new CgroupProxy();
				
		cgroups.setPageSize(0);
		cgroups.addExpression(ExpressionFactory.likeIgnoreCaseExp("name","test_cgroup_%"));
		List<Cgroup> cgroups_list = cgroups.getAll();
		if (cgroups_list != null && cgroups_list.size() > 0) {
			cgroups.deleteObjects(cgroups_list);
			cgroups.commitChanges();
		}
		// Delete test Companies
		CompanyProxy companies = new CompanyProxy();
		
		companies.setPageSize(0);
		companies.addExpression(ExpressionFactory.likeIgnoreCaseExp("name","test_company_%"));
		List<Company> companies_list = companies.getAll();
		if (companies_list != null && companies_list.size() > 0) {
			companies.deleteObjects(companies_list);
			companies.commitChanges();
		}		
	}
}

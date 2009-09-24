package org.supposition.tests.db;

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
import org.supposition.db.proxy.CgroupProxy;
import org.supposition.db.proxy.CompanyProxy;
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
		// Create test groups
		String group1 = "test_cgroup_element1";
		String group11 = "test_cgroup_element11";
		String group12 = "test_cgroup_element12";
		String group121 = "test_cgroup_element121";
		
		DataContext _context = DBUtils.getInstance().getDBContext();

		Cgroup gelement1 = (Cgroup) _context.newObject(Cgroup.class);
		Cgroup gelement11 = (Cgroup) _context.newObject(Cgroup.class);
		Cgroup gelement12 = (Cgroup) _context.newObject(Cgroup.class);
		Cgroup gelement121 = (Cgroup) _context.newObject(Cgroup.class);
		
		gelement1.setName(group1);
		gelement11.setName(group11);
		gelement12.setName(group12);
		gelement121.setName(group121);
				
		ValidationResult validationResult = gelement1.getValidationResult();		
		Assert.assertFalse(validationResult.hasFailures());
		validationResult = gelement11.getValidationResult();
		Assert.assertFalse(validationResult.hasFailures());
		validationResult = gelement12.getValidationResult();
		Assert.assertFalse(validationResult.hasFailures());
		validationResult = gelement121.getValidationResult();
		Assert.assertFalse(validationResult.hasFailures());

		_context.commitChanges();
		
		gelement1.addToChilds(gelement11);
		gelement1.addToChilds(gelement12);
		gelement12.addToChilds(gelement121);		
		
		_context.commitChanges();		
		
		// Create test companies
		String ename1 = "test_company_element1";
		String ename11 = "test_company_element11";
		String ename12 = "test_company_element12";
		String ename121 = "test_company_element121";
		
		Company celement1 = (Company) _context.newObject(Company.class);
		Company celement11 = (Company) _context.newObject(Company.class);
		Company celement12 = (Company) _context.newObject(Company.class);
		Company celement121 = (Company) _context.newObject(Company.class);
		
		celement1.setName(ename1);
		celement11.setName(ename11);
		celement12.setName(ename12);
		celement121.setName(ename121);
				
		validationResult = celement1.getValidationResult();		
		Assert.assertFalse(validationResult.hasFailures());
		validationResult = celement11.getValidationResult();
		Assert.assertFalse(validationResult.hasFailures());
		validationResult = celement12.getValidationResult();
		Assert.assertFalse(validationResult.hasFailures());
		validationResult = celement121.getValidationResult();
		Assert.assertFalse(validationResult.hasFailures());
				
		Assert.assertNotNull(celement1);
		Assert.assertNotNull(celement11);
		Assert.assertNotNull(celement12);
		Assert.assertNotNull(celement121);		
		
		Assert.assertNotNull(celement1.getUuid());
		Assert.assertNotNull(celement11.getUuid());
		Assert.assertNotNull(celement12.getUuid());
		Assert.assertNotNull(celement121.getUuid());
		
		_context.commitChanges();		
		
		gelement1.addToCompanies(celement1);
		gelement11.addToCompanies(celement11);
		gelement12.addToCompanies(celement12);
		gelement121.addToCompanies(celement121);
		
		_context.commitChanges();		
		
		Assert.assertTrue(celement1.getCgoups().size() == 1);
		Assert.assertTrue(celement11.getCgoups().size() == 1);
		Assert.assertTrue(celement12.getCgoups().size() == 1);
		Assert.assertTrue(celement121.getCgoups().size() == 1);
		
		
	}

	private static void delete_test_objects() {
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

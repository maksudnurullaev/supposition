package org.supposition.tests.db;

import java.util.List;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.validation.ValidationResult;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.supposition.db.Company;
import org.supposition.utils.DBUtils;

/**
 * To use this test we should create 1000 test users
 * 
 * @see Create100TestUsers
 * @author M.Nurullayev
 */
public class DBTestCompanies {	
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
		String ename1 = "test_company_element1";
		String ename11 = "test_company_element11";
		String ename12 = "test_company_element12";
		String ename121 = "test_company_element121";
		
		DataContext _context = DBUtils.getInstance().getDBContext();

		Company element1 = (Company) _context.newObject(Company.class);
		Company element11 = (Company) _context.newObject(Company.class);
		Company element12 = (Company) _context.newObject(Company.class);
		Company element121 = (Company) _context.newObject(Company.class);
		
		element1.setName(ename1);
		element11.setName(ename11);
		element12.setName(ename12);
		element121.setName(ename121);
				
		ValidationResult validationResult = element1.getValidationResult();		
		Assert.assertFalse(validationResult.hasFailures());
		validationResult = element11.getValidationResult();
		Assert.assertFalse(validationResult.hasFailures());
		validationResult = element12.getValidationResult();
		Assert.assertFalse(validationResult.hasFailures());
		validationResult = element121.getValidationResult();
		Assert.assertFalse(validationResult.hasFailures());
				
		Assert.assertNotNull(element1);
		Assert.assertNotNull(element11);
		Assert.assertNotNull(element12);
		Assert.assertNotNull(element121);		
		
		Assert.assertNotNull(element1.getUuid());
		Assert.assertNotNull(element11.getUuid());
		Assert.assertNotNull(element12.getUuid());
		Assert.assertNotNull(element121.getUuid());		
		
	}

	private static void delete_test_objects() {
		CompanyProxy cgroups = new CompanyProxy();
				
		cgroups.setPageSize(0);
		cgroups.addExpression(ExpressionFactory.likeIgnoreCaseExp("name","test_company_%"));
		List<Company> cgroups_list = cgroups.getAll();
		if (cgroups_list != null && cgroups_list.size() > 0) {
			cgroups.deleteObjects(cgroups_list);
			cgroups.commitChanges();
		}
		
	}
}

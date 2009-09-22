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
import org.supposition.db.proxy.CgroupProxy;
import org.supposition.utils.DBUtils;

/**
 * To use this test we should create 1000 test users
 * 
 * @see Create100TestUsers
 * @author M.Nurullayev
 */
public class DBTestCGroups {	
	@BeforeClass
	public static void start() {
		delete_test_objects();		
	}
	
	@AfterClass
	public static void end(){
		//delete_test_objects();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_1_common() {
		String ename1 = "test_cgroup_element1";
		String ename11 = "test_cgroup_element11";
		String ename12 = "test_cgroup_element12";
		String ename121 = "test_cgroup_element121";
		
		DataContext _context = DBUtils.getInstance().getDBContext();

		Cgroup element1 = (Cgroup) _context.newObject(Cgroup.class);
		Cgroup element11 = (Cgroup) _context.newObject(Cgroup.class);
		Cgroup element12 = (Cgroup) _context.newObject(Cgroup.class);
		Cgroup element121 = (Cgroup) _context.newObject(Cgroup.class);
		
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
				
		// ###
		element1.addToChilds(element11);
		element1.addToChilds(element12);
		
		_context.commitChanges();
		
		Assert.assertTrue(element1.getChilds().size() == 2);
		
		element12.addToChilds(element121);
		
		_context.commitChanges();
		
		Assert.assertTrue(element12.getChilds().size() == 1);
		
		// ###
		
		List<Cgroup> list1 = element1.getChilds();
		for(Cgroup cgroup:list1){
			Assert.assertTrue(cgroup.getParent().getName() == element1.getName());
			Assert.assertTrue(cgroup.getName() == element11.getName() ||
					cgroup.getName() == element12.getName());
		}
		
		
	}

	private static void delete_test_objects() {
		CgroupProxy cgroups = new CgroupProxy();
				
		cgroups.setPageSize(0);
		cgroups.addExpression(ExpressionFactory.likeIgnoreCaseExp("name","test_cgroup_%"));
		List<Cgroup> cgroups_list = cgroups.getAll();
		if (cgroups_list != null && cgroups_list.size() > 0) {
			cgroups.deleteObjects(cgroups_list);
			cgroups.commitChanges();
		}
		
	}
}

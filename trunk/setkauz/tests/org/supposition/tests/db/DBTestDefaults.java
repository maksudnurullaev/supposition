package org.supposition.tests.db;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.validation.ValidationException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.supposition.db.Default;
import org.supposition.db.proxy.Defaults;
import org.supposition.utils.DBUtils;

public class DBTestDefaults {
	static int generate_test_objects_count = 10;
	static DataContext _context = DBUtils.getInstance().getDBContext();	

	@BeforeClass
	public static void start() {
		delete_test_objects();		
		create_test_objects(generate_test_objects_count);
	}
	
	@AfterClass
	public static void finish(){
		delete_test_objects();
	}	

	@Test
	public void basic(){
		Assert.assertNotNull(_context);
	}

	@Test(expected = ValidationException.class)
	public void test_null_key() throws Exception{
		Defaults defaults = new Defaults();
		Default def = defaults.createNew();
		def.setKey(null);
		def.setValue("null");
		defaults.getContext().commitChanges();
	}
	
	@Test(expected = ValidationException.class)
	public void test_null_value() throws Exception{
		Defaults defaults = new Defaults();
		Default def = defaults.createNew();
		def.setKey("null");
		def.setValue(null);
		defaults.getContext().commitChanges();
	}
	
//	@Test(expected = ValidationException.class)
//	public void test_empty_key(){
//		Defaults defaults = new Defaults();
//		Default def = defaults.createNew();
//		def.setKey(null);
//		def.setValue("null");
//		defaults.getContext().commitChanges();
//	}
//	
//	@Test(expected = ValidationException.class)
//	public void test_empty_value(){
//		Defaults defaults = new Defaults();
//		Default def = defaults.createNew();
//		def.setKey("null");
//		def.setValue(null);
//		defaults.getContext().commitChanges();
//	}	
	
	public static void create_test_objects(int inCount) {
		Default dbobject = null;
		for (int i = 0; i < inCount; i++) {
			dbobject = _context.newObject(Default.class);
			dbobject.setKey(String.format("test_key%d", i));
			dbobject.setValue(String.format("test_value%d", i));
			// batch update
			_context.commitChanges();
		}
	}

	
	private static void delete_test_objects() {
		Defaults defaults_proxy = new Defaults();
		defaults_proxy.setPageSize(0);		
		
		for(Default defaultVal:defaults_proxy.getAll()){
			defaults_proxy.getContext().deleteObject(defaultVal);
			defaults_proxy.getContext().commitChanges();		
		}
		
	}
}

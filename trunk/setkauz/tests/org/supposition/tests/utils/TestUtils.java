package org.supposition.tests.utils;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.supposition.utils.Constants;

/**
 * To use this test we should create 1000 test users
 * 
 * @see Create100TestUsers
 * @author M.Nurullayev
 */
public class TestUtils {

	@BeforeClass
	public static void start() {
	}

	@AfterClass
	public static void finish(){
	}
	
	@Test
	public void testSimple() {
		Assert.assertTrue(Constants.isValidEmailAddress("test_user@tester.com"));
		Assert.assertTrue(!Constants.isValidEmailAddress("test_user#tester.com"));
	}

}

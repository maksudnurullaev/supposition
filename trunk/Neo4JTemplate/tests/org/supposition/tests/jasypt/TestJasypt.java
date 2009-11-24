package org.supposition.tests.jasypt;

import org.junit.Assert;
import org.junit.Test;
import org.supposition.utils.CryptoManager;

public class TestJasypt {
	
	@Test
	public void test_basic() {
		String password = "Strong password";
		String encryptedPassword = CryptoManager.encryptPassword(password);		
		Assert.assertTrue(CryptoManager.checkPassword(password, encryptedPassword));
	}
}

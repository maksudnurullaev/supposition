package org.supposition.tests.defaults;

import org.junit.Assert;
import org.junit.Test;
import org.supposition.utils.MessagesManager;

public class TestDefaults {
	
	@Test
	public void test_basic() {
		Assert.assertTrue(MessagesManager.hasDefaultByKey("project.name"));
		Assert.assertFalse(MessagesManager.hasDefaultByKey("project.wrong.name"));
		Assert.assertTrue(MessagesManager.hasDefaultByKey("default.page.size"));
		Assert.assertTrue(MessagesManager.hasDefaultByKey("DATE.FORMAT.NOW"));
		Assert.assertTrue(MessagesManager.hasDefaultByKey("min.password.length"));
	}
}

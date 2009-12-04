package org.supposition.tests.neo;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo.managers.NeoDbManager;
import org.neo4j.api.core.NeoService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.supposition.text.TextManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
public class TestNeoSpringObjects extends AbstractJUnit4SpringContextTests {
	
	final String _beanNeoDbId = "neoDbManager";	
	final String _beanTextManagerId = "textManager";
	final String _defaultNeoDbPathString = "neo-db";
	
	@Test
	public void test_basic(){
		Assert.assertTrue(applicationContext.containsBean(_beanNeoDbId));
		Assert.assertTrue(applicationContext.containsBean(_beanTextManagerId));
		
		Assert.assertNotNull(applicationContext.getBean(_beanNeoDbId));
		Assert.assertNotNull(applicationContext.getBean(_beanTextManagerId));
		
		Assert.assertTrue(applicationContext.getBean(_beanNeoDbId) instanceof NeoDbManager);
		Assert.assertTrue(applicationContext.getBean(_beanTextManagerId) instanceof TextManager);	
		
		NeoDbManager neoDbManager = (NeoDbManager) applicationContext.getBean(_beanNeoDbId);
		Assert.assertNotNull(neoDbManager.getDbPath());
		Assert.assertNotNull(neoDbManager.getFullPath2NeoDbFolder());
		
		Assert.assertTrue(neoDbManager.checkServices());
		Assert.assertNotNull(neoDbManager.getNeoService());
	}
}

package com.xcesys.extras.test;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

/**
 * Abstract service test case class extends
 * {@link AbstractTransactionalJUnit4SpringContextTests} and configured well.
 * <p>
 * User may extends this abstract case class to write their own JUnit4 test case
 * class with @Test annotation for test methods.
 * 
 * @author Danne Leung
 * 
 */
@ContextConfiguration(locations = { "/applicationContext-base.xml",
		"/test-applicationContext-resources.xml", "/applicationContext-cache.xml",
		"/applicationContext-hibernate.xml", "/applicationContext-aop.xml" })
public class AbstractTransactionalJUnit4ServiceTestCase extends
		AbstractTransactionalJUnit4SpringContextTests {

}

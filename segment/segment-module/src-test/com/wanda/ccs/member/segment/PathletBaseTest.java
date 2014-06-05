package com.wanda.ccs.member.segment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;

import com.google.code.pathlet.core.PathletContainer;
import com.google.code.pathlet.jdbc.ExtJdbcTemplate;
import com.google.code.pathlet.test.ContainerTestHelper;
import com.google.code.pathlet.util.ClassPathResource;
import com.google.code.pathlet.util.ClassUtils;
import com.google.code.pathlet.util.IOUtils;

public class PathletBaseTest {
	
	
	protected PathletContainer getContainer() throws IOException {
		PathletContainer container = ContainerTestHelper.getContainer("testapp");
		if(container == null) {
			File[] configFiles ={
					(new ClassPathResource("/app-config.json")).getFile(),
					(new ClassPathResource("/test-resource-config.json")).getFile() 
					};
			//File[] propertyFiles = {
			//		(new ClassPathResource("/dataSource.properties")).getFile(),
			//		(new ClassPathResource("/dataSourceDw.properties")).getFile()
			//		};
			return ContainerTestHelper.getContainer("testapp", configFiles, null, "UTF-8");
		}
		else {
			return container;
		}
	}
	
	protected String getThisPackageText(String fileName) throws IOException  {
		File file = getThisPackageFile(fileName);
		return FileUtils.readFileToString(file, "UTF-8");
	}
	
	protected File getThisPackageFile(String fileName) throws IOException  {
		ClassLoader cl = this.getClass().getClassLoader();
		String packagePath = ClassUtils.getPackageName(this.getClass()).replace('.', '/');
		return (new ClassPathResource(packagePath + "/" + fileName, cl)).getFile();
	}
	
	
	protected ExtJdbcTemplate getJdbcTemplate() throws IOException {
		return new ExtJdbcTemplate((DataSource)getContainer().getInstance("/dataSource"));
	}
	
	protected ExtJdbcTemplate getJdbcTemplateDw() throws IOException {
		return new ExtJdbcTemplate((DataSource)getContainer().getInstance("/dataSourceDw"));
	}
}

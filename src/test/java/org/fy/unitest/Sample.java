package org.fy.unitest;

import java.io.FileInputStream;

import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;

public class Sample extends DBTestCase {

	// 在构造函数里面设置数据库DB Connection的信息
	public Sample() {
		System.setProperty(
				PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS,
				"com.mysql.jdbc.Driver");
		System.setProperty(
				PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL,
				"jdbc:mysql://localhost:3306/contentsearch?useUnicode=true&characterEncoding=UTF-8");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME,
				"root");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD,
				"123");
	}

	@Override
	// 可选的覆盖，该方法代表运行该测试用例之前，要作的操作。默认就是CLEAN_INSERT
	protected DatabaseOperation getSetUpOperation() throws Exception {
		return DatabaseOperation.CLEAN_INSERT;
	}

	@Override
	// 可选的覆盖，该方法代表运行完该测试，要做的操作。默认就是NONE
	protected DatabaseOperation getTearDownOperation() throws Exception {
		return DatabaseOperation.NONE;
	}

	@Override
	// 这是必须实现的方法，返回的dataset代表数据表里面将要存放的数据。也就是当前该表的数据状态
	protected IDataSet getDataSet() throws Exception {
		// TODO Auto-generated method stub
		return new FlatXmlDataSet(new FileInputStream("sample.xml"));
	}

	public void testSample() {
		// 实现测试用例

	}

}

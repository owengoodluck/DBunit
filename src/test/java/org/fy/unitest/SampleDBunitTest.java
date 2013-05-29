package org.fy.unitest;

import java.io.FileInputStream;

import org.dbunit.Assertion;
import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.SortedTable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;

/**
 * DBunit使用测试类
 * 
 * @author Yung·Fu (fuyung AT gmail DOT com)
 * @version 1.0.0 2013-5-26
 * 
 */
public class SampleDBunitTest extends DBTestCase {

	public SampleDBunitTest() {
		System.setProperty(
				PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS,
				"com.mysql.jdbc.Driver");
		System.setProperty(
				PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL,
				"jdbc:mysql://localhost:3306/dbunit");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME,
				"root");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD,
				"1234");
	}

//	
//	 @Override 
//	 protected IDatabaseConnection getConnection() throws Exception{ 
//		 return null; 
//	 }

	/**
	 * 给定数据集
	 */
	@Override
	protected IDataSet getDataSet() throws Exception {
		System.out.println("read dbunitXML....\ninit dataSet.....");
		return new FlatXmlDataSet(new FileInputStream("dbunitXML.xml"));
	}
	
	/**
	 * 比对整个表中所有的数据
	 * 
	 */
	public void test1() throws Exception {
		// 实际数据库中的数据。读取的是dbunitXML.xml
		// IDataSet dataSet = getConnection().createDataSet();
		// ITable actualDataSet = dataSet.getTable("users");

		/** 2.2以后版本 */
		ITable actualDataSet = getDataSet().getTable("users");

		// 获得预期的结果
		IDataSet dataSet2 = new FlatXmlDataSet(new FileInputStream(
				"exceptedDbunitXML.xml"));
		ITable expectedDataSet = dataSet2.getTable("users");

		Assertion.assertEquals(expectedDataSet, actualDataSet);
	}

	public void test2() throws Exception {
		ITable actualDataSet = getDataSet().getTable("users");

		// 获得预期的结果
		IDataSet dataSet2 = new FlatXmlDataSet(new FileInputStream(
				"exceptedDbunitXML.xml"));
		ITable expectedDataSet = dataSet2.getTable("users");

		// 比较哪一列
		DefaultColumnFilter.includedColumnsTable(actualDataSet,expectedDataSet.getTableMetaData().getColumns());

		
		// 排除到哪一列不比较
		ITable actualTable = DefaultColumnFilter.excludedColumnsTable(actualDataSet, actualDataSet.getTableMetaData().getColumns());
		ITable expectedTable = DefaultColumnFilter.excludedColumnsTable(expectedDataSet, expectedDataSet.getTableMetaData().getColumns());
		
		Assertion.assertEquals(expectedTable, actualTable);

	}

	public void test3() throws Exception {
		ITable actualDataSet = getDataSet().getTable("users");

		ITable expectedDataSet = new FlatXmlDataSet(new FileInputStream(
				"exceptedDbunitXML.xml")).getTable("users");

		SortedTable actualSortedTable = new SortedTable(actualDataSet,
				new String[] { "id" });

		SortedTable expectedSortedTable = new SortedTable(expectedDataSet,
				new String[] { "password" });
		
		Assertion.assertEquals(expectedSortedTable, actualSortedTable);

	}

	/**
	 * 把数据集(XML)里面的数据导入到数据库之前,需要对数据库里面的表的数据执行的操作。
	 */
	@Override
	protected DatabaseOperation getSetUpOperation() throws Exception {
		System.out.println("setUp....");
		return DatabaseOperation.CLEAN_INSERT;
	}

	/**
	 * 测试完成时候,需要对数据库里的表的数据做的操作。
	 */
	@Override
	protected DatabaseOperation getTearDownOperation() throws Exception {
		System.out.println("tearDown.....");
		return DatabaseOperation.REFRESH;
	}
	
}

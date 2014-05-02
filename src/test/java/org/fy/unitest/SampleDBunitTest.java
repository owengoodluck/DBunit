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
 * DBunit使用测试
 * 
 * @author Yung·Fu (fuyung AT aliyun DOT com)
 * @version 1.0.0 2013-5-26
 * 
 */
public class SampleDBunitTest extends DBTestCase {

	/**
	 * 在构造函数里面数据库的 Connection信息
	 */
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

	/**
	 * 给定数据集
	 */
	@Override
	protected IDataSet getDataSet() throws Exception {
		System.out.println("read dbunitXML.xml ....\ninit dataSet.....");
		return new FlatXmlDataSet(new FileInputStream("dbunitXML.xml"));
	}

	/**
	 * 比对整个表中所有的数据
	 * 
	 */
	public void test1() throws Exception {
		// 读取数据库中实际的数据
		IDataSet dataSet = getConnection().createDataSet();
		ITable actualDataSet = dataSet.getTable("users");

		// 直接从getDataSet()方法中读取dbunitXml.xml文件获得users表的数据
		// ITable actualDataSet = getDataSet().getTable("users");

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
		DefaultColumnFilter.includedColumnsTable(actualDataSet, expectedDataSet
				.getTableMetaData().getColumns());

		// 排除到哪一列不比较
		ITable actualTable = DefaultColumnFilter.excludedColumnsTable(
				actualDataSet, actualDataSet.getTableMetaData().getColumns());
		ITable expectedTable = DefaultColumnFilter.excludedColumnsTable(
				expectedDataSet, expectedDataSet.getTableMetaData()
						.getColumns());

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
	 * 
	 * DatabaseOperation定义了对数据库进行的操作，它是一个抽象类，通过静态字段提供了几种内置的实现：
　　	 *   NONE：不执行任何操作，是getTearDownOperation的默认返回值。
　　  *   UPDATE：将数据集中的内容更新到数据库中。它假设数据库中已经有对应的记录，否则将失败。
　　  *   INSERT：将数据集中的内容插入到数据库中。它假设数据库中没有对应的记录，否则将失败。
　　  *   REFRESH：将数据集中的内容刷新到数据库中。如果数据库有对应的记录，则更新，没有则插入。
　　  *   DELETE：删除数据库中与数据集对应的记录。
　　  *   DELETE_ALL：删除表中所有的记录，如果没有对应的表，则不受影响。
　　  *   TRUNCATE_TABLE：与DELETE_ALL类似，更轻量级，不能rollback。
　　  *   CLEAN_INSERT：是一个组合操作，是DELETE_ALL和INSERT的组合。是getSetUpOeration的默认返回值。
	 */
	@Override
	protected DatabaseOperation getSetUpOperation() throws Exception {
		System.out.println("setUp....");
		return DatabaseOperation.NONE;
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

package org.fy;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

/**
 * DBUnit测试,将表生成对应的XML文件
 * 
 * @author Yung·Fu (fuyung AT aliyun DOT com)
 * @version 1.0.0 2013-5-24
 * 
 */
public class DBunitTest {

	public static void main(String[] args) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/dbunit", "root", "1234");
		IDatabaseConnection databaseConnection = new DatabaseConnection(
				connection);
		
		// 根据SQL导出部分数据
		QueryDataSet queryDataSet = new QueryDataSet(databaseConnection);
		queryDataSet.addTable("users",
				"select password from users where id = 10");
		FlatXmlDataSet.write(queryDataSet, new FileOutputStream(
				"dbunitXMLConditation.xml"));
		
		
		// 导出整个库的数据
		IDataSet dataSet = databaseConnection.createDataSet();
		// 将dbunit表中的数据写入到dbunitXML.xml文件中
		FlatXmlDataSet.write(dataSet, new FileOutputStream("dbunitXML.xml"));
		FlatDtdDataSet.write(dataSet, new FileOutputStream("dbunitXML.dtd"));
	}
}

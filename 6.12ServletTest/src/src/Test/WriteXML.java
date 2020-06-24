package src.Test;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WriteXML {

	public static void main(String []args)
			throws ClassNotFoundException, SQLException, UnsupportedEncodingException, FileNotFoundException {

		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/servlettest?serverTimezone=JST",
				"root",
				"a9988765");
		PreparedStatement prep = connection.prepareStatement("SELECT * FROM schema_xml");
		PreparedStatement prepSchema=null;
		prep.executeQuery();
		ResultSet resultSet = prep.getResultSet();
		while (resultSet.next()) {
			String resultRules=null;
			String resultOptions =null;
			String schema_xml = resultSet.getString("schema_xml");
			if (schema_xml != null && schema_xml.contains("<rules>")) {
				int headRulesIndex = schema_xml.indexOf("<rules>");
				int tailRulesIndex = schema_xml.indexOf("</rules>");
				resultRules = schema_xml.substring(headRulesIndex, tailRulesIndex) + "</rules>";
			}
			if (schema_xml != null && schema_xml.contains("<options>")) {
				int headOptionsIndex = schema_xml.indexOf("<options>");
				int tailOptionsIndex = schema_xml.indexOf("</options>");
				resultOptions = schema_xml.substring(headOptionsIndex, tailOptionsIndex) + "</options>";
			}
			String sql="INSERT INTO schema_xml_item (fieldRulesXml,fieldOptionsXml,shopId,itemId) values ('"+resultRules+"','"+resultOptions+"','1','1')";
			prepSchema=connection.prepareStatement(sql);
			prepSchema.execute();
		}
	}

}
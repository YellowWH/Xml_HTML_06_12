package src.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoadAndWriteXML {

	Connection connection = null;

	public LoadAndWriteXML(String name, String password) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		this.connection = DriverManager.getConnection(
				"jdbc:mysql://localhost/servlettest?serverTimezone=JST&characterEncoding=UTF-8", name, password);
	}

	public void showConnect() {
		System.out.println(this.connection);
	}

	public String LoadXmlToString() throws SQLException {
		String str = "";
		PreparedStatement prep = connection.prepareStatement("SELECT * FROM schema_xml limit 1");
		prep.executeQuery();
		ResultSet resultSet = prep.getResultSet();
		while (resultSet.next()) {
			str = resultSet.getString("schema_xml");
		}
		return str;
	}

	public void updatexml(String str) throws SQLException {
		PreparedStatement prep = connection.prepareStatement("INSERT INTO schema_xml_item (fieldRulesXml) values (?)");
		prep.setString(1, str);
		prep.executeUpdate();
	}

	static List<Integer> index_list = new ArrayList<Integer>();
	static int step = 0;
	static int in = 1;
	static String id="";

	public String digui(String str) throws SQLException {
		while (str.contains("<field ")) {
			if (str.indexOf("<field ") > str.indexOf("</field>")) {
				int headfield = str.indexOf("<field ");
				return digui(str.substring(headfield));
			} else {
				int headfield = str.indexOf("<field ");
				int tailfield = str.indexOf("</field>");
				String field= str.substring(headfield, tailfield) + "</field>";
				if(field.contains("<fields>")) {
					String fields= str.substring(0, str.indexOf("<fields>"));
					System.out.println(fields);
					if(str.substring(headfield, tailfield).contains("<rules>")) {
						System.out.println(str.substring(str.indexOf("<rules>"),str.indexOf("</rules>")+8));
						PreparedStatement prep = connection.prepareStatement("UPDATE schema_xml1 SET fieldRulesXml='"+str.substring(str.indexOf("<rules>"),str.indexOf("</rules>")+8)+"' WHERE seq = '"+String.format("%016d", step)+"';");
						prep.executeUpdate();
					}
					if(str.substring(headfield, tailfield).contains("<options>")) {
						System.out.println(str.substring(str.indexOf("<options>"),str.indexOf("</options>")+10));
						PreparedStatement prep = connection.prepareStatement("UPDATE schema_xml1 SET fieldOptionsXml='"+str.substring(str.indexOf("<options>"),str.indexOf("</options>")+10)+"' WHERE seq = '"+String.format("%016d", step)+"';");
						prep.executeUpdate();
					}
					step++;
					System.out.println(step);
					return digui(str.substring(str.indexOf("<fields>")+8));
				}else {
					System.out.println(field);

					if(str.substring(headfield, tailfield).contains("<rules>")) {
						System.out.println(str.substring(str.indexOf("<rules>"),str.indexOf("</rules>")+8));
						PreparedStatement prep = connection.prepareStatement("UPDATE schema_xml1 SET fieldRulesXml='"+str.substring(str.indexOf("<rules>"),str.indexOf("</rules>")+8)+"' WHERE seq = '"+String.format("%016d", step)+"';");
						prep.executeUpdate();
					}
					if(str.substring(headfield, tailfield).contains("<options>")) {
						System.out.println(str.substring(str.indexOf("<options>"),str.indexOf("</options>")+10));
						PreparedStatement prep = connection.prepareStatement("UPDATE schema_xml1 SET fieldOptionsXml='"+str.substring(str.indexOf("<options>"),str.indexOf("</options>")+10)+"' WHERE seq = '"+String.format("%016d", step)+"';");
						prep.executeUpdate();
					}
					step++;
					System.out.println(step);
					return digui(str.substring(str.indexOf("</field>") + 8));
				}
			}
		}
		return "";
	}

}

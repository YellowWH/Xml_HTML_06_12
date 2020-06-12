package src.Test;

import java.sql.*;
import java.util.ArrayList;

public class Database {

	Connection connection = null;

	public Database(String name, String password) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		this.connection = DriverManager.getConnection("jdbc:mysql://localhost/servlettest?serverTimezone=JST", name,
				password);
	}

	public void showConnect() {
		System.out.println(this.connection);
	}

	public int count() {
		int i=0;
		PreparedStatement prep;
		try {
			prep = connection.prepareStatement("SELECT * FROM 6_12test");
			prep.executeQuery();
			ResultSet resultSet = prep.getResultSet();
			while (resultSet.next()) {
				i++;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		return i;
	}

	public ArrayList<String> getAllrules() throws SQLException{

		ArrayList<String> rules = new ArrayList<String>();
		PreparedStatement prep = connection.prepareStatement("SELECT * FROM 6_12test");
		prep.executeQuery();
		ResultSet resultSet = prep.getResultSet();
		while (resultSet.next()) {
			String rule = resultSet.getString("rules");
			rules.add(rule);
		}
		return rules;
	}

	public void close() throws SQLException {
		connection.close();
	}

}

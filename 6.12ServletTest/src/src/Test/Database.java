package src.Test;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
			prep = connection.prepareStatement("SELECT * FROM aaa");
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
		PreparedStatement prep = connection.prepareStatement("SELECT * FROM 6_12_test");
		prep.executeQuery();
		ResultSet resultSet = prep.getResultSet();
		while (resultSet.next()) {
			String rule = resultSet.getString("field_rules");
			rules.add(rule);
		}
		return rules;
	}

	public ArrayList<List<String>> getAllRulesNew() throws SQLException{
		ArrayList<List<String>> allRules = new ArrayList<List<String>>();
		ArrayList<String> rules = new ArrayList<String>();
		ArrayList<String> options = new ArrayList<String>();
		PreparedStatement prep = connection.prepareStatement("SELECT * FROM aaa");
		prep.executeQuery();
		ResultSet resultSet = prep.getResultSet();
		while (resultSet.next()) {
			String option = resultSet.getString("field_options");
			String rule = resultSet.getString("field_rules");
			rules.add(rule);
			options.add(option);
		}
		allRules.add(rules);
		allRules.add(options);
		return allRules;
	}

	public int tab_id_max() throws SQLException{
		int max=0;
		PreparedStatement prep = connection.prepareStatement("select * from aaa order by tab_id desc limit 1");
		prep.executeQuery();
		ResultSet resultSet = prep.getResultSet();
		while (resultSet.next()) {
			max = resultSet.getInt("tab_id");
		}
		return max;
	}

	public int getnotcomplex(int num) throws SQLException{
		int i=0;

		PreparedStatement prep = connection.prepareStatement("SELECT * FROM 6_12_test limit ?,10");
		prep.setInt(1, num);
		prep.executeQuery();
		ResultSet resultSet = prep.getResultSet();
		while (resultSet.next()) {
			String type = resultSet.getString("field_type");
			if(type.equals("complex"))
			{
				i++;
//				int sonNum = resultSet.getInt(i+1);

			}
			else break;
		}
		return i;
	}

	public int checkcomplexifover(int nowplace, int nowdeep) throws SQLException {

		int i = nowdeep;

		PreparedStatement prep = connection.prepareStatement("SELECT * FROM 6_12_test limit ?,1");
		prep.setInt(1, nowplace - 2);
		prep.executeQuery();
		ResultSet resultSet = prep.getResultSet();
		PreparedStatement prep1 = connection.prepareStatement("SELECT * FROM 6_12_test limit ?,1");
		prep1.setInt(1, nowplace - 1);
		prep1.executeQuery();
		ResultSet resultSet1 = prep1.getResultSet();

		while (resultSet.next()) {
			String type = resultSet.getString("field_type");
			if (type.equals("complex")) {
				i++;
			} else {
				while (resultSet1.next()) {
					System.out.println(resultSet1.getInt(i));
					if(resultSet1.getInt(i)==0) {
						i--;
					}

				}
			}
		}
		return i;

	}

	public int newtest(int nowplace) throws SQLException{

		int i=0;
		PreparedStatement prep = connection.prepareStatement("SELECT * FROM aaa limit ?,1");
		prep.setInt(1, nowplace-1);
		prep.executeQuery();
		ResultSet resultSet = prep.getResultSet();
		while (resultSet.next()) {
			String str=resultSet.getString("meisai_c_id");
			int count = 0;
			while(str.indexOf("-") != -1) {
				str = str.substring(str.indexOf("-") + 1,str.length());
				count++;
			}

			if(str.equals("0000"))
			{
				if(resultSet.getString("field_type").equals("complex"))
				{
					i=1;
				}else {
					i=1;
				}
			}else if(count==0) {
				i=2;
			}else {
				i=count+2;
			}
		}
		return i;

	}

	public String getRules(int nowplace) throws SQLException {
		PreparedStatement prep = connection.prepareStatement("SELECT * FROM aaa limit ?,1");
		prep.setInt(1, nowplace-1);
		prep.executeQuery();
		ResultSet resultSet = prep.getResultSet();
		while (resultSet.next()) {
			return resultSet.getString("field_rules");
		}
		return null;
	}

	public String getType(int nowplace) throws SQLException {
		PreparedStatement prep = connection.prepareStatement("SELECT * FROM aaa limit ?,1");
		prep.setInt(1, nowplace-1);
		prep.executeQuery();
		ResultSet resultSet = prep.getResultSet();
		while (resultSet.next()) {
			return resultSet.getString("field_type");
		}
		return null;
	}

	public String getId(int nowplace) throws SQLException {
		PreparedStatement prep = connection.prepareStatement("SELECT * FROM aaa limit ?,1");
		prep.setInt(1, nowplace-1);
		prep.executeQuery();
		ResultSet resultSet = prep.getResultSet();
		while (resultSet.next()) {
			return resultSet.getString("field_id");
		}
		return null;
	}

	public void test() throws SQLException{
		PreparedStatement prep = connection.prepareStatement("SELECT * FROM 6_12_test limit 2,1");
		prep.executeQuery();
		ResultSet resultSet = prep.getResultSet();
		while(resultSet.next()) {
			System.out.println(resultSet.getInt(2));
		}
	}

	public String getRequired(int nowplace) throws SQLException {
		PreparedStatement prep = connection.prepareStatement("SELECT * FROM aaa limit ?,1");
		prep.setInt(1, nowplace-1);
		prep.executeQuery();
		ResultSet resultSet = prep.getResultSet();
		while (resultSet.next()) {
			return resultSet.getString("field_required");
		}
		return null;
	}

	public void close() throws SQLException {
		connection.close();
	}

	public List<String> cssPwPrint(){
		List<String> css = new ArrayList<String>();
		css.add("<html>");
		css.add("<head>");
		css.add("<title>title</title>");
		css.add("<style type=\"text/css\">");
		css.add("*{margin: 0;padding: 0}");
		css.add("html,body{height: 100%}");
		css.add("div{");
		css.add("width: 400px;");
		css.add("border: 2px solid #000;");
		css.add("}");
		css.add("</style>");
		css.add("</head>");
		css.add("<body>");


		return css;
	}


	public List<String> cssPwPrintnew(int max){
		List<String> css = new ArrayList<String>();
		double percent;
		percent = 100.0/max;
		BigDecimal b = new BigDecimal(percent);
		double f1 = b.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
		System.out.println(percent);
		System.out.println(b);
		System.out.println(f1);
		css.add("<html>");
		css.add("<head>");
		css.add("<title>title</title>");
		css.add("<style type=\"text/css\">");
		css.add("*{margin: 0;padding: 0}");
		css.add(".box{width: 80%;margin: 0px auto;border: 1px solid #000;box-sizing: border-box;}");
		css.add(".nav{width: 80%;position: fixed;border: 1px solid #000;box-sizing: border-box;}");
		css.add(".nav>li{list-style: none;width: "+f1+"%;height: 50px;background: orange;text-align: center;line-height: 50px;float: left;}");
		css.add(".nav>.current{background: #ccc;}");
		css.add(".content>li{list-style: none;display: none;}");
		css.add(".all>.newcontent{width:100%;height:800px;border: 1px solid #000;box-sizing: border-box;}");
		css.add(".all>div:first-child{margin-top:50px;}");
		css.add("</style>");
		css.add("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script>");
		css.add("<script>");
		css.add("$(function(){");
		for(int i=1;i<=max;i++)
		{
			css.add("$(\"#li"+i+"\").click(function(){");
			css.add("$(\"html,body\").animate({scrollTop:$(\"#div"+i+"\").offset().top+1});});");
		}
		css.add("$(window).scroll(function(){");
		css.add("var offset = $(\"body\").scrollTop();");
		for(int i=1;i<=max;i++)
		{
			css.add("if(offset >= $(\"#div"+i+"\").offset().top){");
			css.add("$(\"#li"+i+"\").addClass(\"current\");");
			css.add("$(\"#li"+i+"\").siblings().removeClass(\"current\");}");
		}
		css.add("});});");
		css.add("</script>");
		css.add("</head>");
		css.add("<body>");
		css.add("<div class=\"box\">");
		css.add("<ul class=\"nav\">");
		css.add("<li class=\"current\" id=\"li1\">Tag1</li>");
		for (int i = 2;i<=max;i++)
		{
			css.add("<li id=\"li"+i+"\">Tag"+i+"</li>");
		}
		css.add("</ul>");
		return css;
	}

	public List<String> cssPwPrintdiv(int max){
		List<String> css = new ArrayList<String>();
		css.add("<div class=\"all\">");
		for(int i=1;i<=max;i++) {
			css.add("<div class=\"newcontent\" id=\"div"+i+"\"></div>");
		}
		css.add("</div>");
		css.add("</div>");
//		css.add("</body>");
//		css.add("</html>");

		return css;
	}

}

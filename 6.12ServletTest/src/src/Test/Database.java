package src.Test;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		int i = 0;
		PreparedStatement prep;
		try {
			prep = connection.prepareStatement("SELECT * FROM 6_16test_copy2");
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

	public int countmax(int tabid) {
		int i = 0;
		PreparedStatement prep;
		try {
			prep = connection.prepareStatement("SELECT * FROM 6_16test_copy2 where tab_id = ?");
			prep.setInt(1, tabid);
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

	public ArrayList<String> getAllrules() throws SQLException {

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

	public ArrayList<List<String>> getAllRulesNew() throws SQLException {
		ArrayList<List<String>> allRules = new ArrayList<List<String>>();
		ArrayList<String> rules = new ArrayList<String>();
		ArrayList<String> options = new ArrayList<String>();
		PreparedStatement prep = connection.prepareStatement("SELECT * FROM 6_16test");
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

	public ArrayList<List<String>> getAllRulesNewSingle(int nowplace, int tabid) throws SQLException {
		ArrayList<List<String>> allRules = new ArrayList<List<String>>();
		ArrayList<String> rules = new ArrayList<String>();
		ArrayList<String> options = new ArrayList<String>();
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<String> ids = new ArrayList<String>();
		PreparedStatement prep = connection
				.prepareStatement("SELECT * FROM 6_16test_copy2 where tab_id=? order by show_turn limit ?,1");
		prep.setInt(1, tabid);
		prep.setInt(2, nowplace - 1);
		prep.executeQuery();
		ResultSet resultSet = prep.getResultSet();
		while (resultSet.next()) {
			String rule = resultSet.getString("field_rules");
			String option = resultSet.getString("field_options");
			String name = resultSet.getString("field_name");
			String id = resultSet.getString("field_id");
			rules.add(rule);
			options.add(option);
			names.add(name);
			ids.add(id);
		}
		allRules.add(rules);
		allRules.add(options);
		allRules.add(names);
		allRules.add(ids);
		return allRules;
	}

	public int tab_id_max() throws SQLException {
		int max = 0;
		PreparedStatement prep = connection
				.prepareStatement("select * from 6_16test_copy2 order by tab_id desc limit 1");
		prep.executeQuery();
		ResultSet resultSet = prep.getResultSet();
		while (resultSet.next()) {
			max = resultSet.getInt("tab_id");
		}
		return max;
	}

	public int getnotcomplex(int num) throws SQLException {
		int i = 0;

		PreparedStatement prep = connection.prepareStatement("SELECT * FROM 6_12_test limit ?,10");
		prep.setInt(1, num);
		prep.executeQuery();
		ResultSet resultSet = prep.getResultSet();
		while (resultSet.next()) {
			String type = resultSet.getString("field_type");
			if (type.equals("complex")) {
				i++;
//				int sonNum = resultSet.getInt(i+1);

			} else
				break;
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
					if (resultSet1.getInt(i) == 0) {
						i--;
					}

				}
			}
		}
		return i;

	}

	public int newtest(int nowplace, int tabid) throws SQLException {

		int i = 0;
		PreparedStatement prep = connection.prepareStatement("SELECT * FROM 6_16test_copy2 where tab_id=? limit ?,1");
		prep.setInt(1, tabid);
		prep.setInt(2, nowplace - 1);
		prep.executeQuery();
		ResultSet resultSet = prep.getResultSet();
		while (resultSet.next()) {
			String str = resultSet.getString("meisai_c_id");
			int count = 0;
			while (str.indexOf("-") != -1) {
				str = str.substring(str.indexOf("-") + 1, str.length());
				count++;
			}

			if (str.equals("0000")) {
				if (resultSet.getString("field_type").equals("complex")) {
					i = 1;
				} else {
					i = 1;
				}
			} else if (count == 0) {
				i = 2;
			} else {
				i = count + 2;
			}
		}
		return i;

	}

	public String getRules(int nowplace) throws SQLException {
		PreparedStatement prep = connection.prepareStatement("SELECT * FROM 6_16test_copy2 limit ?,1");
		prep.setInt(1, nowplace - 1);
		prep.executeQuery();
		ResultSet resultSet = prep.getResultSet();
		while (resultSet.next()) {
			return resultSet.getString("field_rules");
		}
		return null;
	}

	public String getType(int nowplace, int tabid) throws SQLException {
		PreparedStatement prep = connection
				.prepareStatement("SELECT * FROM 6_16test_copy2 where tab_id=? order by show_turn limit ?,1");
		prep.setInt(1, tabid);
		prep.setInt(2, nowplace - 1);
		prep.executeQuery();
		ResultSet resultSet = prep.getResultSet();
		while (resultSet.next()) {
			return resultSet.getString("field_type");
		}
		return null;
	}

	public String getId(int nowplace) throws SQLException {
		PreparedStatement prep = connection.prepareStatement("SELECT * FROM 6_16test_copy2 limit ?,1");
		prep.setInt(1, nowplace - 1);
		prep.executeQuery();
		ResultSet resultSet = prep.getResultSet();
		while (resultSet.next()) {
			return resultSet.getString("field_id");
		}
		return null;
	}

	public void test() throws SQLException {
		PreparedStatement prep = connection.prepareStatement("SELECT * FROM 6_12_test limit 2,1");
		prep.executeQuery();
		ResultSet resultSet = prep.getResultSet();
		while (resultSet.next()) {
			System.out.println(resultSet.getInt(2));
		}
	}

	public String getRequired(int nowplace) throws SQLException {
		PreparedStatement prep = connection.prepareStatement("SELECT * FROM 6_16test_copy2 limit ?,1");
		prep.setInt(1, nowplace - 1);
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

	public List<String> cssPwPrint() {
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

	public ArrayList<List<String>> getShowTpye(int tabId) throws SQLException {
		ArrayList<List<String>> allTabShowTpye = new ArrayList<List<String>>();
		ArrayList<String> showIds = new ArrayList<String>();
		ArrayList<String> Tpyes = new ArrayList<String>();
		PreparedStatement prep = connection.prepareStatement("SELECT * FROM 6_16test_copy2 where tab_id = ?");
		prep.setInt(1, tabId);
		prep.executeQuery();
		ResultSet resultSet = prep.getResultSet();
		while (resultSet.next()) {
			String showId = resultSet.getString("show_turn");
			String type = resultSet.getString("field_type");
			showIds.add(showId);
			Tpyes.add(type);
		}
		allTabShowTpye.add(showIds);
		allTabShowTpye.add(Tpyes);
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<String> cssBodyAll(int max) throws SQLException {
		List<String> css = new ArrayList<String>();

		List<String> addcss = new ArrayList<String>();

		double percent;
		percent = 100.0 / max;
		BigDecimal b = new BigDecimal(percent);
		double f1 = b.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
		System.out.println(percent);
		System.out.println(b);
		System.out.println(f1);
		css.add("<html>");
		css.add("<head>");
		css.add("<meta charset=\"UTF-8\">");
		css.add("<title>title</title>");
		css.add("<style type=\"text/css\">");
		css.add("*{margin: 0;padding: 0}");

		css.add(".box{width: 80%;margin: 0px auto;border: 3px solid orange;box-sizing: border-box;}");
		css.add(".nav{width: 79.5%;position: fixed;top:0px;border: 0px solid orange;box-sizing: border-box;background:orange;}");
		css.add(".nav>li{list-style: none;width: " + f1
				+ "%;height: 50px;background: orange;text-align: center;line-height: 50px;float: left;}");
		css.add(".nav>.current{background: #ccc;}");
		css.add(".content>li{list-style: none;display: none;}");
		// height可以删除 为了直观设为800px
		css.add(".all>.newcontent{width:100%;border: 1px solid #000;box-sizing: border-box;}");
		css.add(".all>div:first-child{margin-top:47px;}");
		css.add("</style>");
		css.add("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script>");
		css.add("<script>");
		css.add("$(function(){");
		for (int i = 1; i <= max; i++) {
			css.add("$(\"#li" + i + "\").click(function(){");
			css.add("$(\"html,body\").animate({scrollTop:$(\"#div" + i + "\").offset().top-49});});");
		}
		css.add("<!--findme if u wanna add some function-->");
		css.add("$(window).scroll(function(){");
		css.add("var offset = $(\"body\").scrollTop();");
		for (int i = 1; i <= max; i++) {
			css.add("if(offset >= $(\"#div" + i + "\").offset().top-50){");
			css.add("$(\"#li" + i + "\").addClass(\"current\");");
			css.add("$(\"#li" + i + "\").siblings().removeClass(\"current\");}");
		}
		css.add("});});");
		css.add("</script>");
		css.add("</head>");

		int countmax = countmax(1);
		for (int i = 2; i <= max; i++) {
			if (countmax(i) > countmax) {
				countmax = countmax(i);
			}
		}

		css.add("<body>");
		css.add("<div class=\"box\">");
		css.add("<ul class=\"nav\">");
		css.add("<li class=\"current\" id=\"li1\">Tab1</li>");
		for (int i = 2; i <= max; i++) {
			css.add("<li id=\"li" + i + "\">Tab" + i + "</li>");
		}
		css.add("</ul>");
		css.add("<div class=\"all\">");

		List<String> cssColor = new ArrayList<String>();
		cssColor.add("Blue");
		cssColor.add("Green");
		cssColor.add("OrangeRed");
		cssColor.add("Yellow");
		cssColor.add("Gold");
		cssColor.add("Navy");
		int m = 0, chima = 0;
		for (int i = 1; i <= max; i++) {
			css.add("<div class=\"newcontent\" id=\"div" + i + "\" style=\"border:3px solid "
					+ cssColor.get((i + 1) % 6) + ";padding: 50px;\"><br>");
			for (int j = 1; j <= countmax(i); j++) {
				List<Map<String, String>> mapRulesList = new ArrayList<Map<String, String>>();
				List<Map<String, String>> mapOptionsList = new ArrayList<Map<String, String>>();
				ArrayList<List<String>> field_list = getAllRulesNewSingle(j, i);
				List<String> rulesList = field_list.get(0);
				List<String> options_List = field_list.get(1);
				for (String eachRules : rulesList) {
					Map<String, String> mapRules = new HashMap<String, String>();
					if (eachRules != null) {
						mapRules = JsonUtil.json2Map(eachRules);
						mapRulesList.add(mapRules);
						m++;
						System.out.println(m);

					} else {
						mapRulesList.add(mapRules);
						m++;
						System.out.println(m);
					}
				}
				for (String eachOptions : options_List) {
					Map<String, String> mapOptions = new HashMap<String, String>();
					if (eachOptions != null) {
						mapOptions = JsonUtil.json2Map(eachOptions);
						mapOptionsList.add(mapOptions);
					} else {
						mapOptionsList.add(mapOptions);
					}
				}
				System.out.println(getType(j, i));
				if (newtest(j, i) < newtest(j + 1, i)) {
					css.add("<label style=\"margin:2px auto;border:3px solid " + cssColor.get(j % 6) + " ;\">"
							+ field_list.get(2).get(0) + "</label><br>");
					css.add("<div style=\"border:3px solid " + cssColor.get(j % 6)
							+ " ;width :80%;overflow:hidden;  margin:0px 20px;\">");
				} else if (newtest(j, i) == newtest(j + 1, i)) {
					switch (getType(j, i)) {

					case "label":
						css.add("<label style=\"margin:2px auto;\">" + field_list.get(2).get(0) + "</label><br>");
						break;
					case "singleCheck":
						css.add("" + field_list.get(2).get(0) + "<select id=\"" + field_list.get(3).get(0)
								+ "\" size=\"1\" style=\"margin:2px auto;\"");
						Map singleMapsingleCheck = mapRulesList.get(0);
						ArrayList<Map<String, String>> singleMapLsitsingleCheck = (ArrayList<Map<String, String>>) singleMapsingleCheck
								.get("rules");
						css.addAll(rulesTranslate(singleMapLsitsingleCheck, field_list.get(3).get(0)).get(0));
						css.add(">");
						List<String> css2 = rulesTranslate(singleMapLsitsingleCheck, field_list.get(3).get(0)).get(1);
						int findaddfuncionposition = css.indexOf("<!--findme if u wanna add some function-->") + 1;
						for (int addfunction = 0; addfunction < css2.size(); addfunction++) {
							css.add(findaddfuncionposition + addfunction, css2.get(addfunction));
						}
						Map singleOptionsMapsingleCheck = mapOptionsList.get(0);
						ArrayList<Map<String, String>> singleOptionsMapLsitsingleCheck = (ArrayList<Map<String, String>>) singleOptionsMapsingleCheck
								.get("options");
						css.addAll(optionsTranslate(singleOptionsMapLsitsingleCheck).get(0));
						css.add("</select>");
						if(!rulesTranslate(singleMapLsitsingleCheck, field_list.get(3).get(0)).get(2).isEmpty()) {
							css.add("<span style=\"color:red;\">※</span>");
						}
						css.add("<br>");

//						css.add(""+field_list.get(2).get(0)+"<input id=\""+field_list.get(3).get(0)+"\" style=\"margin:2px auto;\" type=\"radio\"");
//						Map singleMapsingleCheck = mapRulesList.get(0);
//						ArrayList<Map<String, String>> singleMapLsitsingleCheck = (ArrayList<Map<String, String>>) singleMapsingleCheck.get("rules");
//						css.addAll(rulesTranslate(singleMapLsitsingleCheck));
//						css.add("><br>");
						break;
					case "input":
						// 如果不要input里面有字就用下面这行
						if (chima == 0 && field_list.get(3).get(0).equals("std_size_prop_20509_-1")) {
							css.add("" + field_list.get(2).get(0) + "<input id=\"" + field_list.get(3).get(0)
									+ "1\" style=\"margin:2px auto;\" value=\"\"");
							chima++;

						} else {
							css.add("" + field_list.get(2).get(0) + "<input id=\"" + field_list.get(3).get(0)
									+ "\" style=\"margin:2px auto;\" value=\"\"");
						}
//						css.add("" + field_list.get(2).get(0) + "<input id=\"" + field_list.get(3).get(0) + "\" style=\"margin:2px auto;\" value=\"" + i + "," + j + "\"");

						Map singleMap = mapRulesList.get(0);
						ArrayList<Map<String, String>> singleMapLsit = (ArrayList<Map<String, String>>) singleMap
								.get("rules");
						css.addAll(rulesTranslate(singleMapLsit, field_list.get(3).get(0)).get(0));

						List<String> css2input = rulesTranslate(singleMapLsit, field_list.get(3).get(0)).get(1);
						int findaddfuncionpositioninput = css.indexOf("<!--findme if u wanna add some function-->") + 1;
						for (int addfunction = 0; addfunction < css2input.size(); addfunction++) {
							css.add(findaddfuncionpositioninput + addfunction, css2input.get(addfunction));
						}

						css.add(">");
						if(!rulesTranslate(singleMapLsit, field_list.get(3).get(0)).get(2).isEmpty()) {
							css.add("<span style=\"color:red;\">※</span>");
						}
						css.add("<br>");

						break;
					case "multiCheck":
						css.add("<label style=\"border:3px solid Black;\"");
						Map singleMapmultiCheck = mapRulesList.get(0);
						ArrayList<Map<String, String>> singleMapLsitmultiCheck = (ArrayList<Map<String, String>>) singleMapmultiCheck
								.get("rules");
						css.addAll(rulesTranslate(singleMapLsitmultiCheck, field_list.get(3).get(0)).get(0));
						css.add(">" + field_list.get(2).get(0) + "：");
						css.add("</label>");
						List<String> css2multi = rulesTranslate(singleMapLsitmultiCheck, field_list.get(3).get(0))
								.get(1);
						int findaddfuncionpositionmulti = css.indexOf("<!--findme if u wanna add some function-->") + 1;
						for (int addfunction = 0; addfunction < css2multi.size(); addfunction++) {
							css.add(findaddfuncionpositionmulti + addfunction, css2multi.get(addfunction));
						}
						Map singleMultiOptionsMapsingleCheck = mapOptionsList.get(0);
						ArrayList<Map<String, String>> singleMultiOptionsMapLsitsingleCheck = (ArrayList<Map<String, String>>) singleMultiOptionsMapsingleCheck
								.get("options");
						css.addAll(multiOptionsTranslate(singleMultiOptionsMapLsitsingleCheck, field_list.get(3).get(0))
								.get(0));

						if(!rulesTranslate(singleMapLsitmultiCheck, field_list.get(3).get(0)).get(2).isEmpty()) {
							css.add("<span style=\"color:red;\">※</span>");
						}
						css.add("<br>");

//						css.add(""+field_list.get(2).get(0)+"<input id=\""+field_list.get(3).get(0)+"\" style=\"margin:2px auto;\" type=\"checkbox\"");
//						Map singleMapmultiCheck = mapRulesList.get(0);
//						ArrayList<Map<String, String>> singleMapLsitmultiCheck = (ArrayList<Map<String, String>>) singleMapmultiCheck.get("rules");
//						css.addAll(rulesTranslate(singleMapLsitmultiCheck));
//						css.add("><br>");
						break;
					default:
						css.add("<p>-----------</p>");
						break;
					}
				} else if (newtest(j, i) > newtest(j + 1, i)) {
					switch (getType(j, i)) {
					case "label":
						css.add("<label style=\"margin:2px auto;\">" + getRules(i) + "</label><br>");
						break;
					case "singleCheck":
						css.add("" + field_list.get(2).get(0) + "<select id=\"" + field_list.get(3).get(0)
								+ "\" size=\"1\" style=\"margin:2px auto;\"");
						Map singleMapsingleCheck = mapRulesList.get(0);
						ArrayList<Map<String, String>> singleMapLsitsingleCheck = (ArrayList<Map<String, String>>) singleMapsingleCheck
								.get("rules");
						css.addAll(rulesTranslate(singleMapLsitsingleCheck, field_list.get(3).get(0)).get(0));
						css.add(">");
						List<String> css2 = rulesTranslate(singleMapLsitsingleCheck, field_list.get(3).get(0)).get(1);
						int findaddfuncionposition = css.indexOf("<!--findme if u wanna add some function-->") + 1;
						for (int addfunction = 0; addfunction < css2.size(); addfunction++) {
							css.add(findaddfuncionposition + addfunction, css2.get(addfunction));
						}
						Map singleOptionsMapsingleCheck = mapOptionsList.get(0);
						ArrayList<Map<String, String>> singleOptionsMapLsitsingleCheck = (ArrayList<Map<String, String>>) singleOptionsMapsingleCheck
								.get("options");
						css.addAll(optionsTranslate(singleOptionsMapLsitsingleCheck).get(0));
						css.add("</select>");
						if(!rulesTranslate(singleMapLsitsingleCheck, field_list.get(3).get(0)).get(2).isEmpty()) {
							css.add("<span style=\"color:red;\">※</span>");
						}
						css.add("<br>");

//						css.add(""+field_list.get(2).get(0)+"<input id=\""+field_list.get(3).get(0)+"\" style=\"margin:2px auto;\" type=\"radio\"");
//						Map singleMapsingleCheck = mapRulesList.get(0);
//						ArrayList<Map<String, String>> singleMapLsitsingleCheck = (ArrayList<Map<String, String>>) singleMapsingleCheck.get("rules");
//						css.addAll(rulesTranslate(singleMapLsitsingleCheck));
//						css.add("><br>");
						break;
					case "input":
						// 如果不要input里面有字就用下面这行
						css.add("" + field_list.get(2).get(0) + "<input id=\"" + field_list.get(3).get(0)
								+ "\" style=\"margin:2px auto;\" value=\"\"");
//						css.add("" + field_list.get(2).get(0) + "<input id=\"" + field_list.get(3).get(0) + "\" style=\"margin:2px auto;\" value=\"" + i + "," + j + "\"");

						Map singleMap = mapRulesList.get(0);
						ArrayList<Map<String, String>> singleMapLsit = (ArrayList<Map<String, String>>) singleMap
								.get("rules");
						css.addAll(rulesTranslate(singleMapLsit, field_list.get(3).get(0)).get(0));

						List<String> css2input = rulesTranslate(singleMapLsit, field_list.get(3).get(0)).get(1);
						int findaddfuncionpositioninput = css.indexOf("<!--findme if u wanna add some function-->") + 1;
						for (int addfunction = 0; addfunction < css2input.size(); addfunction++) {
							css.add(findaddfuncionpositioninput + addfunction, css2input.get(addfunction));
						}

						css.add(">");
						if(!rulesTranslate(singleMapLsit, field_list.get(3).get(0)).get(2).isEmpty()) {
							css.add("<span style=\"color:red;\">※</span>");
						}
						css.add("<br>");
						break;
					case "multiCheck":
						css.add("<label style=\"border:3px solid Black;\"");
						Map singleMapmultiCheck = mapRulesList.get(0);
						ArrayList<Map<String, String>> singleMapLsitmultiCheck = (ArrayList<Map<String, String>>) singleMapmultiCheck
								.get("rules");
						css.addAll(rulesTranslate(singleMapLsitmultiCheck, field_list.get(3).get(0)).get(0));
						css.add(">" + field_list.get(2).get(0) + "：");
						css.add("</label>");
						List<String> css2multi = rulesTranslate(singleMapLsitmultiCheck, field_list.get(3).get(0))
								.get(1);
						int findaddfuncionpositionmulti = css.indexOf("<!--findme if u wanna add some function-->") + 1;
						for (int addfunction = 0; addfunction < css2multi.size(); addfunction++) {
							css.add(findaddfuncionpositionmulti + addfunction, css2multi.get(addfunction));
						}
						Map singleMultiOptionsMapsingleCheck = mapOptionsList.get(0);
						ArrayList<Map<String, String>> singleMultiOptionsMapLsitsingleCheck = (ArrayList<Map<String, String>>) singleMultiOptionsMapsingleCheck
								.get("options");
						css.addAll(multiOptionsTranslate(singleMultiOptionsMapLsitsingleCheck, field_list.get(3).get(0))
								.get(0));

						if(!rulesTranslate(singleMapLsitmultiCheck, field_list.get(3).get(0)).get(2).isEmpty()) {
							css.add("<span style=\"color:red;\">※</span>");
						}
						css.add("<br>");

//						css.add(""+field_list.get(2).get(0)+"<input id=\""+field_list.get(3).get(0)+"\" style=\"margin:2px auto;\" type=\"checkbox\"");
//						Map singleMapmultiCheck = mapRulesList.get(0);
//						ArrayList<Map<String, String>> singleMapLsitmultiCheck = (ArrayList<Map<String, String>>) singleMapmultiCheck.get("rules");
//						css.addAll(rulesTranslate(singleMapLsitmultiCheck));
//						css.add("><br>");
						break;
					default:
						css.add("<p>-----------</p>");
						break;
					}
					for (int k = 0; k < (newtest(j, i) - newtest(j + 1, i)); k++) {
						css.add("</div>");
					}
				}
			}
		}
		css.add("</div>");
		css.add("</div>");
		css.add("</body>");
		css.add("</html>");
		return css;
	}


	public List<List<String>> multiOptionsTranslate(ArrayList<Map<String, String>> optionsList, String name)
			throws SQLException {
		List<List<String>> css = new ArrayList<List<String>>(2);
		List<String> css0 = new ArrayList<String>();
		List<String> css1 = new ArrayList<String>();
		css.add(css0);
		css.add(css1);
//		List<String> css = new ArrayList<String>();
		if (optionsList.size() > 5) {
			css.get(0).add("<div style=\" overflow:auto;height:200px;border:3px solid Black;margin:0px 20px;\">");
			for (int i = 0; i < optionsList.size(); i++) {
				css.get(0)
						.add("<label><input type=\"checkbox\" name=\"" + name + "\" value=\""
								+ optionsList.get(i).get("value") + "\" id = \"" + optionsList.get(i).get("value")
								+ "\">" + optionsList.get(i).get("displayName") + "</label><br>");
			}
			css.get(0).add("</div>");
		} else {
			for (int i = 0; i < optionsList.size(); i++) {
				css.get(0)
						.add("<label><input type=\"checkbox\" name=\"" + name + "\" value=\""
								+ optionsList.get(i).get("value") + "\" id = \"" + optionsList.get(i).get("value")
								+ "\">" + optionsList.get(i).get("displayName") + "</label><br>");
			}
		}

		return css;
	}

	public List<List<String>> optionsTranslate(ArrayList<Map<String, String>> optionsList) throws SQLException {

		List<List<String>> css = new ArrayList<List<String>>(2);
		List<String> css0 = new ArrayList<String>();
		List<String> css1 = new ArrayList<String>();
		css.add(css0);
		css.add(css1);
		css.get(0).add("<option value=\"\">请选择</option>");
		for (int i = 0; i < optionsList.size(); i++) {
			css.get(0).add("<option value=\"" + optionsList.get(i).get("value") + "\">"
					+ optionsList.get(i).get("displayName") + "</option>");
			if (optionsList.get(i).get("value").equals("-1:自定义:-1")) {
				css.get(0).add("<option value=\"均码\" >均码</option>");
			}
		}
		return css;
	}

	public List<List<String>> rulesTranslate(ArrayList<Map<String, String>> rulesList, String id) throws SQLException {
		List<List<String>> css = new ArrayList<List<String>>(2);
		List<String> css0 = new ArrayList<String>();
		List<String> css1 = new ArrayList<String>();
		List<String> css2 = new ArrayList<String>();
		css.add(css0);
		css.add(css1);
		css.add(css2);
		for (int i = 0; i < rulesList.size(); i++) {
			switch ((String) rulesList.get(i).get("name")) {
			case "requiredRule":
				if (rulesList.get(i).get("value").equals("true") || rulesList.get(i).get("value").equals(true)) {
					css.get(0).add(" required");
					css.get(2).add("required");
				}
				break;
			case "valueTypeRule":
				switch ((String) rulesList.get(i).get("value")) {
				case "text":
					css.get(0).add(" tpye=\"text\"");
					break;
				case "url":
					css.get(0).add(" type=\"file\"");
					break;
				case "long":
					// datatype为long时，input里加的属性
					css.get(0).add(" ");
					break;
				case "html":
					css.get(0).add(" type=\"url\"");
					break;
				case "integer":
					css.get(0).add(" type=\"number\" min=\"0\"");
					break;
				case "decimal":
					css.get(0).add(" type=\"number\" min=\"0\" step=\"0.001\"");
					break;
				case "date":
					css.get(0).add(" type=\"date\"");
					break;
				case "time":
					css.get(0).add(" type=\"time\"");
					break;
				default:
					break;
				}
				break;
			// max和min和tip和devtip都用提示
			case "disableRule":
				if (rulesList.get(i).get("value").equals(true) || rulesList.get(i).get("value").equals("true")) {
					css.get(0).add(" disabled");
				}
				if (!((rulesList.get(i).get("dependGroup") == null)
						|| (rulesList.get(i).get("dependGroup") == "null"))) {
					Object a = rulesList.get(i).get("dependGroup");
					HashMap b = (HashMap) a;
					switch (b.get("operator").toString()) {
					case "and":
						List c = (ArrayList) b.get("dependExpressList");
						for (int listnum = 0; listnum < c.size(); listnum++) {
							HashMap d = (HashMap) c.get(listnum);
							switch (d.get("symbol").toString()) {
							case "not contains":
								css.get(1).add("$(\"input[name='" + d.get("fieldId").toString()
										+ "']\").change(function () {");
								css.get(1).add("var prop = $('#" + d.get("value").toString() + "').prop('checked');");
								css.get(1).add("if(prop){$(\"#" + id + "\").prop('disabled', false);}");
								css.get(1).add("else{$(\"#" + id + "\").prop('disabled', true);}});");
								break;
							case "==":
								css.get(1).add("$(\"#" + d.get("fieldId").toString() + "\").change(function(){");
								css.get(1).add("if($(this).val()==\"" + d.get("value").toString() + "\"){");
								css.get(1).add("$(\"#" + id + "\").prop('disabled', false);}");
								css.get(1).add("else{$(\"#" + id + "\").prop('disabled', true);}});");
								break;
							case "!=":
								css.get(1).add("$(\"#" + d.get("fieldId").toString() + "\").change(function(){");
								css.get(1).add("if($(this).val()==\"" + d.get("value").toString() + "\"){");
								css.get(1).add("$(\"#" + id + "\").prop('disabled', false);}");
								css.get(1).add("else{$(\"#" + id + "\").prop('disabled', true);}});");
								break;
							default:
								break;
							}
						}
						break;
					case "or":
						List e = (ArrayList) b.get("dependExpressList");
						for (int listnum = 0; listnum < e.size(); listnum++) {
							HashMap d = (HashMap) e.get(listnum);
							switch (d.get("symbol").toString()) {
							case "not contains":
								css.get(1).add("$(\"#" + d.get("fieldId").toString() + "\").change(function(){");
								css.get(1).add("if($(this).val()==\"" + d.get("value").toString() + "\"){");
								css.get(1).add("$(\"#" + id + "\").prop('disabled', false);}");
								css.get(1).add("else{$(\"#" + id + "\").prop('disabled', true);}});");
								break;
							case "==":
								css.get(1).add("$(\"#" + d.get("fieldId").toString() + "\").change(function(){");
								css.get(1).add("if($(this).val()==\"" + d.get("value").toString() + "\"){");
								css.get(1).add("$(\"#" + id + "\").prop('disabled', false);}");
								css.get(1).add("else{$(\"#" + id + "\").prop('disabled', true);}});");
								break;
							case "!=":
								css.get(1).add("$(\"#" + d.get("fieldId").toString() + "\").change(function(){");
								css.get(1).add("if($(this).val()==\"" + d.get("value").toString() + "\"){");
								css.get(1).add("$(\"#" + id + "\").prop('disabled', false);}");
								css.get(1).add("else{$(\"#" + id + "\").prop('disabled', true);}});");
								break;
							default:
								break;
							}
						}
						break;
					default:
						break;
					}
					List c = (ArrayList) b.get("dependExpressList");
					HashMap d = (HashMap) c.get(0);
					System.out.println(d.get("fieldId"));

				}
				break;
			case "readOnlyRule":
				if (rulesList.get(i).get("value").equals(true) || rulesList.get(i).get("value").equals("true")) {
					css.get(0).add(" readonly");
				}
				break;
			case "regexRule":
				css.get(0).add(" pattern=\"" + rulesList.get(i).get("value") + "\"");
				break;
			case "maxLengthRule":
				css.get(0).add(" maxlength=\"" + rulesList.get(i).get("value") + "\"");
				break;
			case "maxTargetSizeRule":
				css.get(0).add(" maxlength=\"" + rulesList.get(i).get("value") + "\"");
				break;
			case "maxImageSizeRule":
				css.get(0).add(" maxlength=\"" + rulesList.get(i).get("value") + "\"");
				break;
			case "maxValueRule":
				css.get(0).add(" max=\"" + rulesList.get(i).get("value") + "\"");
				break;
			case "minLengthRule":
				css.get(0).add(" minlength=\"" + rulesList.get(i).get("value") + "\"");
				break;
			case "minValueRule":
				css.get(0).add(" min=\"" + rulesList.get(i).get("value") + "\"");
				break;
			case "minImageSizeRule":
				css.get(0).add(" minlength=\"" + rulesList.get(i).get("value") + "\"");
				break;
			}
		}
		css.get(0).add(" title=\"");
		for (int i = 0; i < rulesList.size(); i++) {
			if (rulesList.get(i).get("name").equals("maxLengthRule")) {
				css.get(0).add("最大长度是 " + rulesList.get(i).get("value") + ", ");
			}
			if (rulesList.get(i).get("name").equals("maxTargetSizeRule")) {
				css.get(0).add("最大目标尺寸是 " + rulesList.get(i).get("value") + ", ");
			}
			if (rulesList.get(i).get("name").equals("maxImageSizeRule")) {
				css.get(0).add("最大图片尺寸是 " + rulesList.get(i).get("value") + ", ");
			}
			if (rulesList.get(i).get("name").equals("maxValueRule")) {
				css.get(0).add("最大值 " + rulesList.get(i).get("value") + ", ");
			}
			if (rulesList.get(i).get("name").equals("minLengthRule")) {
				css.get(0).add("最小长度是 " + rulesList.get(i).get("value") + ", ");
			}
			if (rulesList.get(i).get("name").equals("minValueRule")) {
				css.get(0).add("最小值是 " + rulesList.get(i).get("value") + ", ");
			}
			if (rulesList.get(i).get("name").equals("minImageSizeRule")) {
				css.get(0).add("最小图片尺寸是 " + rulesList.get(i).get("value") + ", ");
			}
		}
		for (int i = 0; i < rulesList.size(); i++) {
			if (rulesList.get(i).get("name").equals("tipRule")) {
				css.get(0).add("提示： " + rulesList.get(i).get("value") + ", ");
			}
			if (rulesList.get(i).get("name").equals("devTipRule")) {
				css.get(0).add("开发者提示：  " + rulesList.get(i).get("value") + ", ");
			}
		}
		css.get(0).add("\"");

		return css;
	}

	public List<List<String>> singleMapTranslate(Map<String, String> ruleMap) throws SQLException {
		List<List<String>> css = new ArrayList<List<String>>(2);
		if (!((ruleMap.get("name") != null) && (ruleMap.get("name") != "null"))) {
			System.out.println("wwwwww");
		}

		switch ((String) ruleMap.get("name")) {
		case "requiredRule":
			if (ruleMap.get("value").equals("true") || ruleMap.get("value").equals(true)) {
				css.get(0).add(" required");
			}
			break;
		case "valueTypeRule":
			switch ((String) ruleMap.get("value")) {
			case "text":
				css.get(0).add(" tpye=\"text\"");
				break;
			case "url":
				css.get(0).add(" type=\"url\"");
				break;
			case "long":
				// datatype为long时，input里加的属性
				css.get(0).add(" ");
				break;
			case "html":
				// datatype为html时，input里加的属性
				css.get(0).add(" ");
				break;
			case "integer":
				css.get(0).add(" type=\"number\" min=\"0\"");
				break;
			case "decimal":
				css.get(0).add(" type=\"number\" min=\"0\" step=\"0.001\"");
				break;
			case "date":
				css.get(0).add(" type=\"date\"");
				break;
			case "time":
				css.get(0).add(" type=\"time\"");
				break;
			default:
				break;
			}
			break;
		// max和min和tip和devtip都用提示
		case "disabled":
			if (ruleMap.get("value").equals(true) || ruleMap.get("value").equals("true")) {
				css.get(0).add(" disabled");
			}
			break;
		case "readOnlyRule":
			if (ruleMap.get("value").equals(true) || ruleMap.get("value").equals("true")) {
				css.get(0).add(" readonly");
			}
			break;
		case "regexRule":
			css.get(0).add(" pattern=\"" + ruleMap.get("value") + "\"");
			break;
		case "maxLengthRule":
			css.get(0).add(" maxlength=\"" + ruleMap.get("value") + "\"");
			break;
		case "maxTargetSizeRule":
			css.get(0).add(" maxlength=\"" + ruleMap.get("value") + "\"");
			break;
		case "maxImageSizeRule":
			css.get(0).add(" maxlength=\"" + ruleMap.get("value") + "\"");
			break;
		case "maxValueRule":
			css.get(0).add(" max=\"" + ruleMap.get("value") + "\"");
			break;
		case "minLengthRule":
			css.get(0).add(" minlength=\"" + ruleMap.get("value") + "\"");
			break;
		case "minValueRule":
			css.get(0).add(" min=\"" + ruleMap.get("value") + "\"");
			break;
		case "minImageSizeRule":
			css.get(0).add(" minlength=\"" + ruleMap.get("value") + "\"");
			break;
		}
		css.get(0).add(" title=\"");
		if (ruleMap.get("name").equals("maxLengthRule")) {
			css.get(0).add("最大长度是 " + ruleMap.get("value") + ", ");
		}
		if (ruleMap.get("name").equals("maxTargetSizeRule")) {
			css.get(0).add("最大目标尺寸是 " + ruleMap.get("value") + ", ");
		}
		if (ruleMap.get("name").equals("maxImageSizeRule")) {
			css.get(0).add("最大图片尺寸是 " + ruleMap.get("value") + ", ");
		}
		if (ruleMap.get("name").equals("maxValueRule")) {
			css.get(0).add("最大值 " + ruleMap.get("value") + ", ");
		}
		if (ruleMap.get("name").equals("minLengthRule")) {
			css.get(0).add("最小长度是 " + ruleMap.get("value") + ", ");
		}
		if (ruleMap.get("name").equals("minValueRule")) {
			css.get(0).add("最小值是 " + ruleMap.get("value") + ", ");
		}
		if (ruleMap.get("name").equals("minImageSizeRule")) {
			css.get(0).add("最小图片尺寸是 " + ruleMap.get("value") + ", ");
		}
		if (ruleMap.get("name").equals("tipRule")) {
			css.get(0).add("提示： " + ruleMap.get("value") + ", ");
		}
		if (ruleMap.get("name").equals("devTipRule")) {
			css.get(0).add("开发者提示： " + ruleMap.get("value") + ", ");
		}
		css.get(0).add("\"");

		return css;
	}
}

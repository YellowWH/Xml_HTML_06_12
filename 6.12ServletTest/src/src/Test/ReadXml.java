package src.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taobao.top.schema.enums.FieldTypeEnum;
import com.taobao.top.schema.exception.TopSchemaException;
import com.taobao.top.schema.factory.SchemaReader;
import com.taobao.top.schema.field.ComplexField;
import com.taobao.top.schema.field.Field;
import com.taobao.top.schema.field.MultiCheckField;
import com.taobao.top.schema.field.MultiComplexField;
import com.taobao.top.schema.field.SingleCheckField;

public class ReadXml {

	static List<Integer> index_list = new ArrayList<Integer>();
	static int step = 0;
	static int i = 1;
	static int seq = 0;

	public static void main(String[] args) {
		List<String> lines = new ArrayList<>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost/servlettest?characterEncoding=UTF-8&serverTimezone=JST", "root",
					"a9988765");

			PreparedStatement prep1 = con.prepareStatement(
					"CREATE TABLE `schema_xml1` (\r\n" + "	`seq` BIGINT(20) NOT NULL AUTO_INCREMENT,\r\n"
							+ "	`shopId` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8_general_ci',\r\n"
							+ "	`categoryId` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8_general_ci',\r\n"
							+ "	`f_Id` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8_general_ci',\r\n"
							+ "	`meisai_c_Id` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8_general_ci',\r\n"
							+ "	`isProduct` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8_general_ci',\r\n"
							+ "	`kbn` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8_general_ci',\r\n"
							+ "	`itemId` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8_general_ci',\r\n"
							+ "	`fieldNo` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8_general_ci',\r\n"
							+ "	`fieldId` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8_general_ci',\r\n"
							+ "	`isParent` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8_general_ci',\r\n"
							+ "	`isChild` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8_general_ci',\r\n"
							+ "	`layerNo` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8_general_ci',\r\n"
							+ "	`pathInfo` LONGTEXT NULL DEFAULT NULL COLLATE 'utf8_general_ci',\r\n"
							+ "	`fieldName` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8_general_ci',\r\n"
							+ "	`fieldType` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8_general_ci',\r\n"
							+ "	`fieldRulesXml` LONGTEXT NULL DEFAULT NULL COLLATE 'utf8_general_ci',\r\n"
							+ "	`fieldOptionsXml` LONGTEXT NULL DEFAULT NULL COLLATE 'utf8_general_ci',\r\n"
							+ "	`fieldRulesMap` LONGTEXT NULL DEFAULT NULL COLLATE 'utf8_general_ci',\r\n"
							+ "	`fieldOptionsMap` LONGTEXT NULL DEFAULT NULL COLLATE 'utf8_general_ci',\r\n"
							+ "	`isRequire` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8_general_ci',\r\n"
							+ "	`groupId` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8_general_ci',\r\n"
							+ "	`showOrder` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8_general_ci',\r\n"
							+ "	`isShow` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8_general_ci',\r\n"
							+ "	`isDelete` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8_general_ci',\r\n"
							+ "   PRIMARY KEY (`seq`) USING BTREE\r\n" + ")\r\n" + "COLLATE='utf8_general_ci'\r\n"
							+ "ENGINE=InnoDB\r\n" + ";\r\n");
			prep1.execute();

			StringBuffer sb = new StringBuffer(
					"INSERT INTO schema_xml1 (fieldNo, f_Id, meisai_c_Id, groupId, showOrder, fieldName, fieldType, fieldId, fieldRulesMap, fieldOptionsMap, isRequire) VALUES ");

			String str = "";
			PreparedStatement prep = con.prepareStatement("SELECT * FROM schema_xml WHERE seq='7'");
			prep.executeQuery();
			ResultSet resultSet = prep.getResultSet();
			String shopId = "", categoryId = "", isProduct = "", kbn = "";
			int shopIdint, categoryIdint, kbnint;
			while (resultSet.next()) {
				str = resultSet.getString("schema_xml");
				shopId = resultSet.getString("shopId");
				categoryId = resultSet.getString("categoryId");
				isProduct = resultSet.getString("isProduct");
				kbn = resultSet.getString("kbn");
			}

			if (str.contains("&quot;")) {
				str = str.replace("&quot;", "'");
			}

			lines.add(str);

			List<Field> fieldList = SchemaReader.readXmlForList(String.join("", lines));
			for (Field field : fieldList) {
				createSql(field, sb);
				// 親階層のインデックスを増す
				i++;
			}
			String sql = sb.substring(0, sb.lastIndexOf(","));
			System.out.println(sql);
			PreparedStatement ps = con.prepareStatement(sql);
			ps.executeUpdate();
			System.out.println(digui(str, con));

			shopIdint = Integer.parseInt(shopId);
			categoryIdint = Integer.parseInt(categoryId);
			kbnint = Integer.parseInt(kbn);

			PreparedStatement prep2 = con.prepareStatement("UPDATE schema_xml1 SET shopId = '"
					+ String.format("%016d", shopIdint) + "', categoryId = '" + String.format("%016d", categoryIdint)
					+ "', isProduct = '" + isProduct + "', kbn = '" + String.format("%016d", kbnint) + "'");
			prep2.executeUpdate();

			set_Parent_Path(con);
			checkPath(con);
			System.out.println("finish!");

		} catch (IOException | TopSchemaException | SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void createSql(Field field, StringBuffer sb) throws JsonProcessingException {

		String options = "";
		if (field.getType() == FieldTypeEnum.SINGLECHECK) {
			SingleCheckField fieldSC = (SingleCheckField) field;
			// 文字列にする
			options = new ObjectMapper().writeValueAsString(fieldSC.getOptions());
			// 「'」から「"」に換える
			options = formatString(options.replace("'", "&apos;"), "options");
		} else if (field.getType() == FieldTypeEnum.MULTICHECK) {
			MultiCheckField fieldMC = (MultiCheckField) field;
			// 文字列にする
			options = new ObjectMapper().writeValueAsString(fieldMC.getOptions());
			// 「'」から「"」に換える
			options = formatString(options.replace("'", "&apos;"), "options");
		}
		String str1 = new ObjectMapper().writeValueAsString(field.getRules());
		// 文字列にする
		String rules = formatString(new ObjectMapper().writeValueAsString(field.getRules()), "rules");
		String required = "";
		if (!"".equals(rules)) {
			if (rules.contains("\"name\":\"requiredRule\",\"value\":\"true\"")) {
				required = "Y";
			}
			// 「'」から「"」に換える
			rules = rules.replace("'", "&apos;");
		}

		if (step == 0) {
			// 親階層のSQL文の作成
			sb.append("('" + String.format("%016d", seq) + "','" + String.format("%09d", i) + "','"
					+ String.format("%04d", 0) + "','" + "0" + "','" + "0" + "','" + field.getName() + "','"
					+ field.getType() + "','" + field.getId() + "','" + rules + "','" + options + "','" + required
					+ "'),");
			seq++;
		} else {

			String meisai_c_id = "";
			// 明細子階層のidを作成する
			for (int j = 0; j < step; j++) {
				meisai_c_id += String.format("%04d", index_list.get(j)) + "-";
			}
			// 該当階層のループ回数を子階層idに追加
			meisai_c_id += String.format("%04d", ++i);
			// 親idを取り除く
			meisai_c_id = meisai_c_id.substring(meisai_c_id.indexOf("-") + 1);
			// 子階層のSQL文の作成
			sb.append("('" + String.format("%016d", seq) + "','" + String.format("%09d", index_list.get(0)) + "','"
					+ meisai_c_id + "','" + "0" + "','" + "0" + "','" + field.getName() + "','" + field.getType()
					+ "','" + field.getId() + "','" + rules + "','" + options + "','" + required + "'),");
			seq++;
		}

		if (field.getType() == FieldTypeEnum.COMPLEX) {
			ComplexField complex = (ComplexField) field;
			List<Field> fieldList = complex.getFieldList();
			if (fieldList.size() > 0) {
				// リストのサイズと階層の比較
				if (index_list.size() < step + 1) {
					// 該当階層のインデックスをリストに追加
					index_list.add(i);
				} else {
					// 該当階層のインデックスでリストの値を変更
					index_list.set(step, i);
				}
				// 階層用インデックスを増す
				step++;
				// 次階層ループ用パラメータを用意する
				i = 0;
				for (Field field_child : fieldList) {
					createSql(field_child, sb);
				}
				// 階層用インデックスを減る
				step--;
				// 階層インデックスによるロープ回数を取得
				i = index_list.get(step);
			}
		} else if (field.getType() == FieldTypeEnum.MULTICOMPLEX) {
			MultiComplexField multi_complex = (MultiComplexField) field;
			List<Field> fieldList = multi_complex.getFieldList();
			if (fieldList.size() > 0) {
				// リストのサイズと階層の比較
				if (index_list.size() < step + 1) {
					// 該当階層のインデックスをリストに追加
					index_list.add(i);
				} else {
					// 該当階層のインデックスでリストの値を変更
					index_list.set(step, i);
				}
				// 階層用インデックスを増す
				step++;
				// 次階層ループ用パラメータを用意する
				i = 0;
				for (Field field_child : fieldList) {
					createSql(field_child, sb);
				}
				// 階層用インデックスを減る
				step--;
				// 階層インデックスによるロープ回数を取得
				i = index_list.get(step);
			}
		}
	}

	private static String formatString(String str, String name) {
		str = "{\"" + name + "\":" + str + "}";
		return str;
	}

	public static String digui(String str, Connection con) throws SQLException {
		while (str.contains("<field ")) {
			if ((str.indexOf("<field ") > str.indexOf("</field>")) && str.contains("</field>")) {
				int headfield = str.indexOf("<field ");
				return digui(str.substring(headfield), con);
			} else {
				int headfield = str.indexOf("<field ");
				int tailfield = str.indexOf("</field>");
				String field = str.substring(headfield, tailfield) + "</field>";
				if (field.contains("<fields>")) {
					String fields = str.substring(0, str.indexOf("<fields>"));
					System.out.println(fields);
					if (fields.contains("<rules>")) {
						String rule = fields.substring(fields.indexOf("<rules>"), fields.indexOf("</rules>") + 8);
						rule = rule.replace("'", "&apos;");
						PreparedStatement prep = con.prepareStatement("UPDATE schema_xml1 SET fieldRulesXml='" + rule
								+ "' WHERE seq = '" + String.format("%016d", step) + "';");
						prep.executeUpdate();
					}
					if (fields.contains("<options>")) {
						String option = fields.substring(fields.indexOf("<options>"),
								fields.indexOf("</options>") + 10);
						option = option.replace("'", "&apos;");
						PreparedStatement prep = con.prepareStatement("UPDATE schema_xml1 SET fieldOptionsXml='"
								+ option + "' WHERE seq = '" + String.format("%016d", step) + "';");
						prep.executeUpdate();
					}
					step++;
					System.out.println(step);
					return digui(str.substring(str.indexOf("<fields>") + 8), con);
				} else {
					if (field.indexOf("<field ") != field.lastIndexOf("<field ")) {
						String fieldSmall = field.substring(0, field.indexOf("<field ", field.indexOf("<field ") + 7));
						System.out.println(fieldSmall);
						if (str.substring(headfield, tailfield).contains("<rules>")) {
							String rule = str.substring(str.indexOf("<rules>"), str.indexOf("</rules>") + 8);
							rule = rule.replace("'", "&apos;");
							PreparedStatement prep = con.prepareStatement("UPDATE schema_xml1 SET fieldRulesXml='"
									+ rule + "' WHERE seq = '" + String.format("%016d", step) + "';");
							prep.executeUpdate();
						}
						step++;
						if (field.contains("<default-complex-values>")) {
							return digui(str.substring(str.indexOf("</default-complex-values>") + 25), con);
						} else {
							return digui(str.substring(str.indexOf("<field ", str.indexOf("<field ") + 7)), con);
						}
					} else {
						System.out.println(field);
						if (str.substring(headfield, tailfield).contains("<rules>")) {
							String rule = str.substring(str.indexOf("<rules>"), str.indexOf("</rules>") + 8);
							rule = rule.replace("'", "&apos;");
							PreparedStatement prep = con.prepareStatement("UPDATE schema_xml1 SET fieldRulesXml='"
									+ rule + "' WHERE seq = '" + String.format("%016d", step) + "';");
							prep.executeUpdate();
						}
						if (str.substring(headfield, tailfield).contains("<options>")) {
							String option = str.substring(str.indexOf("<options>"), str.indexOf("</options>") + 10);
							option = option.replace("'", "&apos;");
							PreparedStatement prep = con.prepareStatement("UPDATE schema_xml1 SET fieldOptionsXml='"
									+ option + "' WHERE seq = '" + String.format("%016d", step) + "';");
							prep.executeUpdate();
						}
						step++;
						System.out.println(step);
						if (field.contains("<default-complex-values>")) {
							return digui(str.substring(str.indexOf("</default-complex-values>") + 25), con);
						} else {
							return digui(str.substring(str.indexOf("</field>") + 8), con);
						}
					}

				}
			}
		}
		return "";
	}

	public static void set_Parent_Path(Connection con) throws SQLException {
		PreparedStatement prepSetParent = con
				.prepareStatement("UPDATE schema_xml1 SET isParent = \"1\" WHERE meisai_c_Id = \"0000\"");
		prepSetParent.executeUpdate();

		PreparedStatement prep = con.prepareStatement("SELECT * FROM schema_xml1");
		prep.execute();
		ResultSet resultSet = prep.getResultSet();

		int count = 0;
		while (resultSet.next()) {
			count++;
			int cengshu = newtest(count, con);
			PreparedStatement prepCount = con
					.prepareStatement("UPDATE schema_xml1 SET layerNo = \"" + cengshu + "\" WHERE seq = ?");
			prepCount.setInt(1, count);
			prepCount.executeUpdate();
		}
		PreparedStatement re_prep = con.prepareStatement("SELECT * FROM schema_xml1");
		re_prep.execute();
		ResultSet re_resultSet = re_prep.getResultSet();
		int re_count = 0;
		while (re_resultSet.next()) {
			re_count++;
			PreparedStatement prepParent = con
					.prepareStatement("UPDATE schema_xml1 SET isParent = ? , isChild = ? WHERE seq = ?");
			List<Boolean> pc = checkParent(re_count, con, count);
			if (pc.get(0)) {
				prepParent.setInt(1, 1);
			} else {
				prepParent.setInt(1, 0);
			}
			if (pc.get(1)) {
				prepParent.setInt(2, 1);
			} else {
				prepParent.setInt(2, 0);
			}
			prepParent.setInt(3, re_count);
			prepParent.executeUpdate();
		}
	}

	public static void checkPath(Connection con) throws SQLException {
		PreparedStatement prepPath = con.prepareStatement("SELECT * FROM schema_xml1");
		prepPath.execute();
		ResultSet resultSet = prepPath.getResultSet();
		List<List<String>> SNPCLP = new ArrayList<List<String>>(5);
		List<String> seq = new ArrayList<String>();
		List<String> fieldNo = new ArrayList<String>();
		List<String> parent = new ArrayList<String>();
		List<String> child = new ArrayList<String>();
		List<String> layer = new ArrayList<String>();
		List<String> path = new ArrayList<String>();
		while(resultSet.next())
		{
			seq.add(String.valueOf(resultSet.getInt("seq")));
			fieldNo.add(resultSet.getString("fieldNo"));
			parent.add(resultSet.getString("isParent"));
			child.add(resultSet.getString("isChild"));
			layer.add(resultSet.getString("layerNo"));
		}
		SNPCLP.add(seq);
		SNPCLP.add(fieldNo);
		SNPCLP.add(parent);
		SNPCLP.add(child);
		SNPCLP.add(layer);
		path.add("main");
		for(int i=1;i<seq.size();i++) {
			if("1".equals(layer.get(i))){
				path.add("main");
			}else {
				String pre_LayerNo = layer.get(i-1);
				if("1".equals(pre_LayerNo)) {
					path.add(String.valueOf(seq.get(i-1)));
				}else if (Integer.parseInt(layer.get(i))==Integer.parseInt(layer.get(i-1))){
					path.add(path.get(i-1));

//					for(int j = i-1; j>0;j--) {
//						if(Integer.parseInt(layer.get(j))<Integer.parseInt(pre_LayerNo))
//						{
//							path.add(path.get(j-1)+"-"+String.valueOf(seq.get(j)));
//							break;
//						}
//					}
				}else if(Integer.parseInt(layer.get(i))>Integer.parseInt(layer.get(i-1))) {
					path.add(path.get(i-1)+"-"+String.valueOf(seq.get(i-1)));
				}else {
					int minus=Integer.parseInt(layer.get(i-1))-Integer.parseInt(layer.get(i));
					String pathtemp=path.get(i-1);
					for(int j=0;j<minus;j++) {
						pathtemp=pathtemp.substring(0, pathtemp.lastIndexOf("-"));
					}
					path.add(pathtemp);
				}

			}
		}
		for ( int i = 0; i<seq.size();i++) {
			PreparedStatement updatePrep = con.prepareStatement("UPDATE schema_xml1 SET pathInfo = ? WHERE seq = ?");
			updatePrep.setString(1, path.get(i));
			updatePrep.setInt(2, i+1);
			updatePrep.executeUpdate();
		}

	}

	public static List<Boolean> checkParent(int nowplace, Connection con, int max) throws SQLException {
		List<Boolean> pc = new ArrayList<Boolean>(2);
		while (nowplace < max) {
			int now = newtest(nowplace, con);
			int next = newtest(nowplace + 1, con);
			if (now >= next) {
				pc.add(false);
				if (now == 1) {
					pc.add(false);
				} else {
					pc.add(true);
				}
				return pc;
			} else {
				pc.add(true);
				pc.add(false);
				return pc;
			}
		}
		if (newtest(nowplace, con) == 1) {
			pc.add(false);
			pc.add(false);
		} else {
			pc.add(false);
			pc.add(true);
		}
		return pc;
	}

	public static int newtest(int nowplace, Connection con) throws SQLException {

		int i = 0;
		PreparedStatement prep = con.prepareStatement("SELECT * FROM schema_xml1 LIMIT ?,1;");
		prep.setInt(1, nowplace - 1);
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
				if (resultSet.getString("fieldType").equals("complex")) {
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
}

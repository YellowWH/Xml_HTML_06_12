package src.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

public class ZhangReadXml {

	static List<Integer> index_list = new ArrayList<Integer>();
	static int step = 0;
	static int i = 1;

	public static void main(String[] args) {
		List<String> lines = new ArrayList<>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/servlettest?characterEncoding=UTF-8&serverTimezone=JST", "root", "a9988765");
			StringBuffer sb =new StringBuffer("INSERT INTO xml_info (f_id, meisai_c_id, tab_id, show_turn, field_name, field_type, field_id, field_rules, field_options, field_required) VALUES ");

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(
							"C:\\Users\\yellowwh\\Desktop\\test(1).xml"),
					"UTF-8"));

			String str = "";
			while ((str = reader.readLine()) != null) {
				if(str.contains("&quot;")) {
					str = str.replace("&quot;", "'");
				}
				lines.add(str);
			}

			List<Field> fieldList = SchemaReader.readXmlForList(String.join("", lines));
			for (Field field : fieldList) {
				createSql(field,sb);
				//親階層のインデックスを増す
				i++;
			}
			String sql = sb.substring(0, sb.lastIndexOf(","));
			PreparedStatement ps = con.prepareStatement(sql);
            ps.executeUpdate();
			reader.close();
		} catch (IOException | TopSchemaException | SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void createSql(Field field, StringBuffer sb) throws JsonProcessingException {

		String options = "";
		if (field.getType() == FieldTypeEnum.SINGLECHECK) {
			SingleCheckField fieldSC = (SingleCheckField) field;
			//文字列にする
			options = new ObjectMapper().writeValueAsString(fieldSC.getOptions());
			//「'」から「"」に換える
			options = formatString(options.replace("'", "&apos;"),"options");
		} else if (field.getType() == FieldTypeEnum.MULTICHECK) {
			MultiCheckField fieldMC = (MultiCheckField) field;
			//文字列にする
			options = new ObjectMapper().writeValueAsString(fieldMC.getOptions());
			//「'」から「"」に換える
			options = formatString(options.replace("'", "&apos;"),"options");
		}

		//文字列にする
		String rules = formatString(new ObjectMapper().writeValueAsString(field.getRules()),"rules");
		String required = "";
		if(!"".equals(rules)) {
			if(rules.contains("\"name\":\"requiredRule\",\"value\":\"true\"")) {
				required = "Y";
			}
			//「'」から「"」に換える
			rules = rules.replace("'", "&apos;");
		}

		if(step == 0) {
			//親階層のSQL文の作成
			sb.append("('" + String.format("%09d", i) + "','" + String.format("%04d", 0) + "','" + "" +  "','" + "" +  "','"
					+ field.getName() + "','" + field.getType() + "','" + field.getId() + "','" + rules + "','" + options + "','" + required + "'),");
		}else {

			String meisai_c_id = "";
			//明細子階層のidを作成する
			for(int j = 0;j < step;j++) {
				meisai_c_id += String.format("%04d", index_list.get(j)) + "-";
			}
			//該当階層のループ回数を子階層idに追加
			meisai_c_id += String.format("%04d", ++i);
			//親idを取り除く
			meisai_c_id = meisai_c_id.substring(meisai_c_id.indexOf("-") + 1);
			//子階層のSQL文の作成
			sb.append("('" + String.format("%09d", index_list.get(0)) + "','" + meisai_c_id +  "','" + "" +  "','" + "" +  "','"
					+ field.getName() + "','" + field.getType() + "','" + field.getId() + "','" + rules + "','" + options + "','" + required + "'),");
		}

		if (field.getType() == FieldTypeEnum.COMPLEX) {
			ComplexField complex = (ComplexField) field;
			List<Field> fieldList =  complex.getFieldList();
			if(fieldList.size() > 0) {
				//リストのサイズと階層の比較
				if(index_list.size() < step + 1) {
					//該当階層のインデックスをリストに追加
					index_list.add(i);
				}else {
					//該当階層のインデックスでリストの値を変更
					index_list.set(step, i);
				}
				//階層用インデックスを増す
				step++;
				//次階層ループ用パラメータを用意する
				i = 0;
				for (Field field_child : fieldList) {
					createSql(field_child,sb);
				}
				//階層用インデックスを減る
				step--;
				//階層インデックスによるロープ回数を取得
				i = index_list.get(step);
			}
		} else if (field.getType() == FieldTypeEnum.MULTICOMPLEX) {
			MultiComplexField multi_complex = (MultiComplexField) field;
			List<Field> fieldList =  multi_complex.getFieldList();
			if(fieldList.size() > 0) {
				//リストのサイズと階層の比較
				if(index_list.size() < step + 1) {
					//該当階層のインデックスをリストに追加
					index_list.add(i);
				}else {
					//該当階層のインデックスでリストの値を変更
					index_list.set(step, i);
				}
				//階層用インデックスを増す
				step++;
				//次階層ループ用パラメータを用意する
				i = 0;
				for (Field field_child : fieldList) {
					createSql(field_child,sb);
				}
				//階層用インデックスを減る
				step--;
				//階層インデックスによるロープ回数を取得
				i = index_list.get(step);
			}
		}
	}

	private static String formatString(String  str,String name) {
		str = "{\"" +  name + "\":" + str + "}";
		return str;
	}
}

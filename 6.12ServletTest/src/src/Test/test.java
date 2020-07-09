package src.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class test {
	public static void main(String[] args) throws SQLException, ClassNotFoundException{

		Database database= new Database("root", "a9988765");
		LoadAndWriteXML loadxml = new LoadAndWriteXML("root", "a9988765");
//		System.out.println(loadxml.LoadXmlToString());
//		loadxml.updatexml(loadxml.LoadXmlToString());


		System.out.println(loadxml.digui(loadxml.LoadXmlToString()));

//		int j=database.count();

//		ArrayList<String> ruleslist = database.getAllrules();
//		for(String each: ruleslist)
//		{
//			System.out.println(each);
//		}

//		for(int i=0;i<database.count();i++)
//		{
//			System.out.println(database.getnotcomplex(i));
//			if(database.getnotcomplex(i))
//		}
//		for(int i=1; i<j+1;i++)
//		{
//			System.out.println(database.newtest(i));
//		}

//		System.out.println(database.tab_id_max());

//		List<String> css = new ArrayList<String>();
//		List<String> css1 = new ArrayList<String>();
//		css.add("asdasdasd");
//		css.add("asdasdasd");
//		css.add("asdasdasd");
//		css.add("asdasdasd");
//		css.add("asdasdasd");
//		css.add("22222");
//		css.add("asdasdasd");
//		css.add("asdasdasd");
//		css.add("asdasdasd");
//
//		css1.add("qweqweqwe1");
//		css1.add("qweqweqwe2");
//		css1.add("qweqweqwe3");
//		css1.add("qweqweqwe4");
//
//		for(int i=0;i<css1.size();i++) {
//			System.out.println(css.indexOf("22222"));
//			css.add(6+i, css1.get(i));
//		}
//
//		for(int i=0;i<css.size();i++) {
//			System.out.println(css.get(i));
//		}


//
//		database.test();



	}
}

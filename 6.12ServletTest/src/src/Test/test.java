package src.Test;

import java.sql.SQLException;
import java.util.ArrayList;

public class test {
	public static void main(String[] args) throws SQLException, ClassNotFoundException{

		Database database= new Database("root", "a9988765");
		int j=database.count();

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
		for(int i=1; i<j+1;i++)
		{
			System.out.println(database.newtest(i));
		}

		System.out.println(database.tab_id_max());
//
//		database.test();
	}
}

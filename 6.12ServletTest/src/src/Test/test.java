package src.Test;

import java.sql.SQLException;
import java.util.ArrayList;

public class test {
	public static void main(String[] args) throws SQLException, ClassNotFoundException{

		Database database= new Database("root", "a9988765");
		System.out.println(database.count());

		ArrayList<String> ruleslist = database.getAllrules();
		for(String each: ruleslist)
		{
			System.out.println(each);
		}

	}

}

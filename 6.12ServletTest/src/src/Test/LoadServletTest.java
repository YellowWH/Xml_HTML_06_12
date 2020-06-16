package src.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class LoadServletTest
 */
@WebServlet("/LoadServletTest")
public class LoadServletTest extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoadServletTest() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());

		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setHeader("content-type", "text/html;charset=utf-8");
		PrintWriter pw= response.getWriter();


		Database database;
		try {
			database = new Database("root", "a9988765");
			System.out.println(database.count());
			int max=database.tab_id_max();
			List<String> css=database.cssPwPrintnew(max);
			for(String eachcss:css)
			{
				pw.println(eachcss);
			}
			List<String> css1=database.cssPwPrintdiv(max);
			for(String eachcss1:css1)
			{
				pw.println(eachcss1);
			}

//			ObjectMapper mapper = new ObjectMapper();
//			List<Map<String, String>> mapRulesList = new ArrayList<Map<String, String>>();
//			List<Map<String, String>> mapOptionsList = new ArrayList<Map<String, String>>();
//			ArrayList<List<String>> field_list = database.getAllRulesNew();
//			List<String> rulesList=field_list.get(0);
//			List<String> options_List=field_list.get(1);
//			for (String eachRules : rulesList) {
//				Map<String, String> mapRules = new HashMap<String,String>();
//				if (eachRules.length() == 2) {
//					mapRulesList.add(mapRules);
//				} else {
//					mapRules = mapper.readValue(eachRules, Map.class);
//					mapRulesList.add(mapRules);
//				}
//			}
//			for(String eachOptions:options_List) {
//				Map<String,String> mapOptions=new HashMap<String,String>();
//				if(eachOptions.length()==2) {
//					mapOptionsList.add(mapOptions);
//				} else {
//					mapOptions=mapper.readValue(eachOptions, Map.class);
//					mapOptionsList.add(mapOptions);
//				}
//			}

			int count=database.count();
			for(int i=1;i<=count;i++) {
				if(database.newtest(i)<database.newtest(i+1))
				{
					pw.println("<div>");
				}else if(database.newtest(i)==database.newtest(i+1))
				{
					switch(database.getType(i))
					{
					case "label" :
						pw.println("<label>"+database.getRules(i)+"</label>");
						break;
					case "singleCheck":
						pw.println("<input type=\"radio\">");
						break;
					case "input":
						pw.println("<input value=\"" + i + "\">");
						break;
					case "multiCheck":
						pw.println("<input type=\"checkbox\">");
						break;
					default :
						pw.println("<p>-----------</p>");
						break;
					}
				}else if(database.newtest(i)>database.newtest(i+1))
				{
					switch(database.getType(i))
					{
					case "label" :
						pw.println("<label>"+database.getRules(i)+"</label>");
						break;
					case "singleCheck":
						pw.println("<input type=\"radio\">");
						break;
					case "input":
						pw.println("<input value=\"" + i + "\">");
						break;
					case "multiCheck":
						pw.println("<input type=\"checkbox\">");
						break;
					default :
						pw.println("<p>-----------</p>");
						break;
					}
					for (int j=0;j<(database.newtest(i)-database.newtest(i+1));j++)
					pw.println("</div>");
				}
			}


		} catch (ClassNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		pw.println("</body>");
		pw.println("</html>");
		pw.flush();
		pw.close();


//		doGet(request, response);
	}

}

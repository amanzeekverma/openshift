import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//keyword
/*
site?type=GET&res=LIST
site?type=GET&res=ITEM&id=1
site?type=ADD&res=LIST&name=NEW LIST
site?type=ADD&res=ITEM&id=1&name=NEW ITEM

* 
TRUNCATE LIST; TRUNCATE ITEMS;
ALTER TABLE LIST AUTO_INCREMENT = 1;
ALTER TABLE ITEMS AUTO_INCREMENT = 1;
* 


*/
public class ZMSLnIServlet extends HttpServlet {
	
	/**
	 * something for Anjali
	 */
	private static final long serialVersionUID = 2L;
	private Connection con = null;
	
	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
	    PrintWriter writer = response.getWriter();  
	    
		String requestType=request.getParameter("type");
		try {
		if (requestType.equals("GET")){
			//GET
				// Create connection
				//$conn = new mysqli($mysql_host, $mysql_user, $mysql_password, $mysql_database);
				// Check connection
				String resource = request.getParameter("res");
				String sql = "SELECT LIST_ID, LIST_NAME from LIST WHERE IS_ACTIVE = 1 "; //to be overridden
						if (resource.equals("LIST")){
							sql = "SELECT LIST_ID, LIST_NAME from LIST WHERE IS_ACTIVE = 1 ";
					} else if (resource.equals("ITEM")){
							String list_id = request.getParameter("id");
							sql = "SELECT ITEM_ID, ITEM from ITEMS WHERE IS_ACTIVE = 1 AND LIST_ID = "+list_id;
					}
				ResultSet rs = executeQuery(sql);
				writer.println(sql+"<br>\n");
				boolean pass = false;
				while(rs.next()){
					// output data of each row
					writer.println("PASS:"+rs.getString(1)+" ,"+rs.getString(2)+",<br>\n");
					pass = true;
				} 
				if (!pass){
					writer.println("FAIL: No Result<br>\n");
				}
				
		} else if (requestType.equals("ADD")){
			//ADD
				// Create connection
				/*$conn = new mysqli($mysql_host, $mysql_user, $mysql_password, $mysql_database);
				// Check connection
				if ($conn->connect_error) {
					die("FAIL: Connection failed: " . $conn->connect_error);
				}*/
				String resource = request.getParameter("res");
				String sql = "something "; //to be overridden, just a place holder
					if (resource.equals("LIST")){
							String list_name = request.getParameter("name");
							sql = "INSERT INTO LIST (LIST_NAME, IS_ACTIVE) VALUES ('"+list_name+"',1)";
					} else if (resource.equals("ITEM")){
							String list_id = request.getParameter("id");
							String item_name=request.getParameter("name");
							sql = "INSERT INTO ITEMS (ITEM, LIST_ID, IS_ACTIVE) VALUES ('"+item_name+"',"+list_id+",1) ";
					}
					executeQuery(sql);
					writer.println(sql+"<br>\n");
					
		} else if (requestType.equals("DEL")){
			//ADD
			// Create connection
			/*$conn = new mysqli($mysql_host, $mysql_user, $mysql_password, $mysql_database);
			// Check connection
			if ($conn->connect_error) {
				die("FAIL: Connection failed: " . $conn->connect_error);
			}*/
			String resource = request.getParameter("res");
			String sql = "something "; //to be overridden, just a place holder
				if (resource.equals("LIST")){
						String list_id = request.getParameter("id");
						sql = "UPDATE LIST SET IS_ACTIVE = 0 WHERE LIST_ID = "+list_id;
				} else if (resource.equals("ITEM")){
						String list_id = request.getParameter("id");//bad var name
						sql = "UPDATE ITEMS SET IS_ACTIVE = 0 WHERE ITEM_ID = "+list_id;
				}
				executeQuery(sql);
				writer.println(sql+"<br>\n");
				
	} else {
			writer.println("Sorry, This is not a public site. To know more, please contact Aman ZeeK Verma at avD.ZeeK@gmail.com");
		}
		}catch(SQLException e){
			writer.println("SQLException : "+e.getMessage());
			e.printStackTrace();
		}
		finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			con = null;
		}
	}
	
	public ResultSet executeQuery(String query) throws SQLException{
		ResultSet rs = null;
		if (con == null){
			
			String host = System.getenv("MYSQL_SERVICE_HOST");
			String port = System.getenv("MYSQL_SERVICE_PORT");
			String username = System.getenv("MYSQL_USER");
			String password = System.getenv("MYSQL_PASSWORD");

			String url = String.format("jdbc:mysql://%s:%s/java", host, port);
			 try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			con = DriverManager.getConnection(url, username, password);
			
		}
		Statement s = con.createStatement();
		if (query.startsWith("SELECT"))
			rs = s.executeQuery(query);
		else {
			s.executeUpdate(query);
		}
		return rs;
	}
}


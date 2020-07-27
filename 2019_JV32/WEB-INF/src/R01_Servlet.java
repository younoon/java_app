import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//サーブレットクラスを作成するにあたって
//ルールが３つ(教科書p83~)
//①必ずHttpServletクラスを継承する。
//②doGetメソッドの再定義を必ずする。(over ride)
//③必要になるクラスは、必ずimportする。
//@webservlet(urlPatterns= {"/R01","/01"})-tomcat7以降
public class R01_Servlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO 自動生成されたメソッド・スタブ
		//super.doGet(req, resp);
		funcServ(req, resp,"GET");
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO 自動生成されたメソッド・スタブ
		//super.doPost(req, resp);
		funcServ(req, resp,"POST");
	}

	protected void funcServ(HttpServletRequest req, HttpServletResponse resp ,String strReq ) throws ServletException, IOException {
		//HTML文章の出力
		resp.setContentType("text/html; charset=UTF-8");

		req.setCharacterEncoding("UTF-8");

		String strPath = "/jsp/R01_output.jsp";

		//JSPから値を受け取る
		String strMod = req.getParameter("txtMod");
		String strId = req.getParameter("txtId");
		String strName = req.getParameter("txtName");
		String strAge = req.getParameter("txtAge");

		String strMessage = "";

		//URLに値が存在したか
		if(strId == null || strName == null || strAge == null) {
			//存在しない⇨err.jspへ遷移
			strPath = "/jsp/err.jsp?err=1";
		}else {


/*		PrintWriter out;
		out = resp.getWriter();
		out.println("<html>");
		out.println("<body>");
		out.println("HelloWorld!");
		out.println("リクエスト:" +strReq);
		out.println("</body>");
		out.println("</html>");
*/
			if (strMod.equals("input")) {
				strPath = "/jsp/R01_check.jsp";
				//入力検証
				//未入力検証
				if(strId.equals("") || strName.equals("") || strAge.equals("") ) {
					//いずれかが未入力
					strMessage = "未入力の項目があります";
				}else {
					//桁数検証
					if (strId.length() >5 || strName.length() >50 || strAge.length()>3) {
						//いずれかで桁数オーバー
						strMessage = "桁数が多い項目があります";
					}
				}
			}else if(strMod.equals("cheak")) {
				//DB連携
				java.sql.Connection con = null;
				//Connection con = null;
				java.sql.Statement stmt = null ;
				//Statement stmt =null;

				try {
					Class.forName("com.mysql.jdbc.Driver");
					con = DriverManager.getConnection("jdbc:mysql://localhost/2019jv32", "jv32", "jv32");
					//con = DriverManager.getConnection("jdbc:mtsql://localhost/2019jv32", "jv32", "jv32");
					stmt = con.createStatement();
					String strSQL = "";
					strSQL +=" insert into t_user ";
					strSQL +=" values ";
					strSQL +=" ( ";
					strSQL +=" '" + strId   +"' , ";
					strSQL +=" '" + strName +"' , ";
					strSQL +=" " + strAge   +"   ";
					strSQL +=" ) ";

					stmt.execute(strSQL);
				}catch (ClassNotFoundException e) {
					// TODO: handle exception
					strPath = "/jsp/err.jsp?err=2";
					System.out.println(e.getMessage());
				}catch (SQLException e) {
					// TODO: handle exception
					strPath = "/jsp/err.jsp?err=3";
					req.setAttribute("errmsg", e.getMessage());
					System.out.println(e.getMessage());
				}finally {
					try {
						if (stmt !=null) {
							stmt.close();
						}
						if(con !=null) {
							con.close();
						}
					}catch (SQLException e) {
						// TODO: handle exception
						strPath = "/jsp/err.jsp?err=4";
						System.out.println(e.getMessage());
					}
				}
			}

		//サーブレットからJSPへ値を渡す
		//setAttribute
			req.setAttribute("userid", strId);
			req.setAttribute("username", strName);
			req.setAttribute("userage", strAge);
			req.setAttribute("message", strMessage);
		}
		//jspへ遷移
		ServletContext sc =null;
		sc = getServletContext();
		sc.getRequestDispatcher(strPath).forward(req, resp);
	}
}

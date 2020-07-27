import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Mybeans;

//サーブレットクラスを作成するにあたって
//ルールが３つ(教科書p83~)
//①必ずHttpServletクラスを継承する。
//②doGetメソッドの再定義を必ずする。(over ride)
//③必要になるクラスは、必ずimportする。
//@webservlet(urlPatterns= {"/R01","/01"})-tomcat7以降
public class R04_Servlet extends HttpServlet {

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

		String strPath = "/jsp/R04_search_disp.jsp";

		//JSPから値を受け取る
		String strName = req.getParameter("txtName");

		String strMessage = "";

		//URLに値が存在したか
		if(strName == null) {
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
				//DB連携
				java.sql.Connection con = null;
				//Connection con = null;
				java.sql.Statement stmt = null ;
				//Statement stmt =null;
				ResultSet rs = null;

				ArrayList<Mybeans> ary = null;
				//document.getElementById

				try {
					Class.forName("com.mysql.jdbc.Driver");
					con = DriverManager.getConnection("jdbc:mysql://localhost/2019jv32", "jv32", "jv32");
					//con = DriverManager.getConnection("jdbc:mtsql://localhost/2019jv32", "jv32", "jv32");
					stmt = con.createStatement();
					String strSQL = "";
					strSQL +=" select ";
					strSQL +=" * ";
					strSQL +=" from ";
					strSQL +=" t_user ";
					strSQL +=" where ";
					strSQL +="  f_name like '%"+strName+"%' ";

					rs = stmt.executeQuery(strSQL);
					//select文はexcuteQuery insert文はUpdate

					ary = new ArrayList<Mybeans>();

					//ArrayListクラス⇨動的配列クラス
					//HashMapクラス⇨連想配列クラス
					while(rs.next()) {
						Mybeans bean = new Mybeans();

						bean.setId(rs.getString("f_id"));
						bean.setName(rs.getString("f_name"));
						bean.setAge(rs.getString("f_age"));

						ary.add(bean);
					}

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
						if (rs != null) {
							rs.close();
						}

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


		//サーブレットからJSPへ値を渡す
		//setAttribute
			req.setAttribute("username", strName);
			req.setAttribute("message", strMessage);
			req.setAttribute("data", ary);
			//req.setAttribute("ary2", ary);
		}
		//jspへ遷移
		ServletContext sc =null;
		sc = getServletContext();
		sc.getRequestDispatcher(strPath).forward(req, resp);
	}
}

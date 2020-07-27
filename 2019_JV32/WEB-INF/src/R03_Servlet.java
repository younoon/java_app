import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Mybeans;



//サーブレットクラスを作成するにあたって
//ルールが３つ(教科書p83～)
//①必ずHttpServletクラスを継承する。
//②doGetメソッドの再定義を必ずする。
//③必要になるクラスは、必ずimportする。
//Tomcat7以降
//@WebServlet("/R03_Servlet")
public class R03_Servlet extends HttpServlet {

	@Override
	protected void doGet
	(HttpServletRequest req,
	 HttpServletResponse resp)
	throws ServletException, IOException {
		// TODO 自動生成されたメソッド・スタブ
//		super.doGet(req, resp);
		funcServ(req, resp,"GET");
	}

	@Override
	protected void doPost(
			HttpServletRequest req,
			HttpServletResponse resp)
	throws ServletException, IOException {
		// TODO 自動生成されたメソッド・スタブ
//		super.doPost(req, resp);
		funcServ(req, resp, "POST");
	}

	protected void funcServ(
			HttpServletRequest req,
			HttpServletResponse resp,
			String strReq)
	throws ServletException, IOException {
		//HTML文章の出力
		req.setCharacterEncoding("UTF-8");

		String strPath = "/jsp/R03_Beans_sample_04.jsp";

		//Servletの中でBeans生成
		Mybeans bean = new Mybeans();
		bean.setId("003");

		//サーブレットからJSPへ値を渡す
		//  setAttribute
		req.setAttribute("bean3", bean);

		//JSPへ遷移
		ServletContext sc = null;
		sc = getServletContext();
		sc.getRequestDispatcher
		  (strPath).forward(req, resp);
	}
}

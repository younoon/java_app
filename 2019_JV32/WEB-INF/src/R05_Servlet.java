import java.io.IOException;

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
public class R05_Servlet extends HttpServlet {

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

		String strPath = "/jsp/R05_data_result.jsp";

		//JSPから値を受け取る
		String strId = req.getParameter("txtId");
		String strName = req.getParameter("txtName");
		String strAge = req.getParameter("txtAge");

		Mybeans bean = new Mybeans();
		bean.setId(strId);
		bean.setName(strName);
		bean.setAge(strAge);

		String strMsg = "";
		if(bean.dataCheck()) {
			//エラーなし
			if(bean.dataAdd()) {
				//成功
				strMsg = "Insert成功";

			}else {
				//失敗
				strMsg = "Insert失敗["+bean.getMessage()+"]";
			}
		}else {
			//エラーあり
			strMsg = "エラーあり:"+bean.getMessage();
		}

		//サーブレットからJSPへ値を渡す
		//setAttribute
		req.setAttribute("message", strMsg);

		//jspへ遷移
		ServletContext sc =null;
		sc = getServletContext();
		sc.getRequestDispatcher(strPath).forward(req, resp);
	}
}

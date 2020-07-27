package struts_sample;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import common.Db_access;

public class LoginAction extends Action {
	public ActionForward execute
	(ActionMapping mapping,
	ActionForm form,
	HttpServletRequest req,
	HttpServletResponse res)
	throws Exception{


		LoginActionForm formbean = (LoginActionForm)form;

		//入力されてる情報をもらう。
		String strId = formbean.getIn_id();

		String strPass = formbean.getIn_pass();

		String strMsg = "";
		boolean successFlg = false;

		//認証ロジック
		//DB連携
		Db_access db = new Db_access();
		if(db.dLoad()) {
			if(db.connect()) {
				db.setstrSQL("SELECT * FROM t_user WHERE f_id = ? AND f_age = ?");
				ArrayList<Object> arrayList = new ArrayList<Object>();
				if(db.sqlShaping()) {
					arrayList.add(strId);
					arrayList.add(strPass);
					db.setAryBind(arrayList);
					if(db.sqlParse()) {
						if(db.exec()) {
							if(db.getAryData() != null && db.getAryData().size() > 0) {
								strMsg = "認証成功";
								successFlg = true;
							} else {
								strMsg = "認証失敗";
							}
						} else {
							strMsg = "SQL失敗";
						}
					} else {
						strMsg = "SQL解析失敗";
					}
				} else {
					strMsg = "ほげー";
				}
			} else {
				strMsg = "DB接続失敗";
			}
		} else {
			strMsg = "ドライバ読み込み失敗";
		}

//		if(strId.equals("katoka") && strPass.equals("ktok")) {
//			//認証成功
//			strMsg = "認証成功";
//		}else {
//			//認証失敗
//			strMsg = "認証失敗";
//			blnFlg = true;
//		}

		//FormBeanに認証メッセージを設定
		formbean.setOut_msg(strMsg);

		ActionForward forward;

		if (successFlg) {
			//認証成功時
			return mapping.findForward("success");
		}else {
			//入力元(Input)に戻る
			forward = mapping.getInputForward();
		}
		return forward;
	}
}

package struts_sample;

import org.apache.struts.validator.ValidatorForm;

//FormBean(ActionFormクラスを継承)
//public class LoginActionForm extends ActionForm{

//入力検証つきformBean(ValidatorFormクラスを継承)
public class LoginActionForm extends ValidatorForm{

	//プロパティ(html:form内で定義した入力エリア)
	private String in_id;
	private String in_pass;
	private String out_msg;

	//コンストラクタ
	public LoginActionForm() {
		in_id = "";
		in_pass = "";
		out_msg = "";
	}

	//アクセッサー
	public void setIn_id(String strId) {
		in_id = strId;
	}
	public String getIn_id() {
		return in_id;
	}
	public void setIn_pass(String strPass) {
		in_pass = strPass;
	}
	public String getIn_pass() {
		return in_pass;
	}

	public void setOut_msg(String strMsg) {
		out_msg = strMsg;
	}

	public String getOut_msg() {
		return out_msg;
	}

}

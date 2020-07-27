package beans;

import java.io.Serializable;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class Mybeans implements Serializable{

	//プロパティ
	private String id;
	private String name;
	private String age;

	private String message;

	//idFlg true:idが存在する false:存在しない
	private boolean idFlg;


	//コンストラクタ
	public Mybeans() {
		id="";
		name="";
		age="";
		message="";
		idFlg=false;
	}

	//セッター
	public void setId(String in_id) {
		id = in_id;
	}

	public void setName(String in_name) {
		name = in_name;
	}

	public void setAge(String in_age) {
		age = in_age;
	}

	//ゲッター
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getAge() {
		return age;
	}

	//ゲッター（メッセージ）
	public String getMessage() {
		return message;
	}

	//ビジネスロジック（データ追加）
	//追加成功：true
	//追加失敗：false
	public boolean dataAdd() {
		message = "";

		boolean flg = true;
		//データベース連携

		java.sql.Connection con = null;
		//Connection con = null;
		java.sql.Statement stmt = null ;

		try {
			//Class.forName("com.mysql.jdbc.Driver");
			//con = DriverManager.getConnection("jdbc:mysql://localhost/2019jv32", "jv32", "jv32");
			//con = DriverManager.getConnection("jdbc:mtsql://localhost/2019jv32", "jv32", "jv32");

			InitialContext initCon = new InitialContext();

			DataSource ds = (DataSource)initCon.lookup("java:comp/env/jdbc/mysql_local");

			con = ds.getConnection();

			stmt = con.createStatement();
			String strSQL = "";
			strSQL +=" insert into ";
			strSQL +=" t_user ";
			strSQL +=" values ( ";
			strSQL +="  '"+id+"' , ";
			strSQL +="  '"+name+"' , ";
			strSQL +="   "+age+" ";
			strSQL +=" ) ";

			stmt.executeUpdate(strSQL);

		}catch (SQLException e) {
			// TODO: handle exception
			message ="SQLエラー:"+e.getMessage();
			System.out.println(e.getMessage());
			flg = false;
		} catch (NamingException e) {
			// TODO 自動生成された catch ブロック
			message ="SQLエラー:"+e.getMessage();
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
				message ="SQLエラー:"+e.getMessage();
				System.out.println(e.getMessage());
				flg = false;
			}
		}
		return flg;
	}

	//ビジネスロジック(データ検証)
	//エラーなし :true
	//エラーあり :false
	public boolean dataCheck(){
		boolean flg = true;
		//未入力チェック(空白 or null)
		if( id != null && name != null && age != null ) {
			if(!id.equals("") && !name.equals("") && !age.equals("")) {
				//桁数チェック
				if( id.length() <= 5 && name.length() <= 50 && age.length() <= 3) {
					//書式チェック
					try {
						int intAge = Integer.valueOf(age);
						//IDの重複チェック
						//  →9/11までに使用を考えること
						idCheck();
						if (idFlg == true) {
							message = "IDが重複している";
							flg = false;
						}
					}catch(Exception e) {
						//TODO: handle exception
						message = "年齢が数値以外です。" +age;
						flg = false;
					}
				}else {
					message = "入力桁数が長いです。";
					flg = false;
				}
			}else {
				//プロパティが空白の場合
				message = "プロパティが空白です。";
				flg = false;
			}
		}else {
			//プロパティがnullの場合
			message = "プロパティがnullです。";
			flg = false;
		}
		return flg;
	}



	//ビジネスロジック(idの重複チェック)
	public void idCheck() {
		//idFlgの初期化
		idFlg = false;
		//idが空白か確認
		if(!id.equals(null)) {
			//データベース検索
			java.sql.Connection con = null;
			//Connection con = null;
			java.sql.Statement stmt = null ;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://localhost/2019jv32", "jv32", "jv32");
				//con = DriverManager.getConnection("jdbc:mtsql://localhost/2019jv32", "jv32", "jv32");
				stmt = con.createStatement();
				String strSQL = "";
				strSQL += " select count(*) as cnt ";
				strSQL += " from t_user ";
				strSQL += " where ";
				strSQL += " f_id = '"+id+"' ";

				rs = stmt.executeQuery(strSQL);
				if(rs.next()) {
					if(rs.getInt("cnt")>0) {
						idFlg = true;
					}
				}

			}catch (ClassNotFoundException e) {
				// TODO: handle exception
				message = "ドライバーエラー:"+e.getMessage();
				System.out.println(e.getMessage());
			}catch (SQLException e) {
				// TODO: handle exception
				message ="SQLエラー:"+e.getMessage();
				System.out.println(e.getMessage());
			}finally {
				try {
					if (rs !=null) {
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
					message ="SQLエラー:"+e.getMessage();
					System.out.println(e.getMessage());
				}
			}
		}
		//
		//
		//
	}

}

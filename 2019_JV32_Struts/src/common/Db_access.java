package common;

/**********************************************************
 * データベース操作の汎用クラス
 *   設定されたSQL文を元に解析・実行する。
 *
 *   ver1.0  2019/06/11  created by t.kataoka
 *     単一テーブルの状態で初期リリース(プリペアド未対応)
 *   ver1.1  2019/06/14  created by t.kataoka
 *     プリペアド対応
 *   ver1.2  2019/07/02  created by t.kataoka
 *     データ判定ロジック追加
 *     →ArrayList<Object>の内容を判定する部分
 *
 **********************************************************/

/* クラスのimport */
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class Db_access implements Db_interface{

	private Connection con;                            //接続用
	private Statement stmt;                            //実行用
	private PreparedStatement pstmt;                   //プリペアド用
	private ResultSet   rs;                            //データ結果用
	private String kbn;                                //区分
	private String errMsg;                             //エラーメッセージ用
	private String strSQL;                             //SQL格納用
	private String strDbName;                          //データベース名用
	private String[] arySQL;                           //SQL格納用(" "で分解後に格納)
	private ArrayList<String> aryColmns;               //列名格納用
	private ArrayList<HashMap<String,String>> aryData; //データ格納用
	private int intType;                               //SQL種類格納用

	//プリペアド設定用(画面から入力された値を設定)
	//  セッター呼び出し前に設定したい順番で配列生成
	//    →セッター呼び出し→SQL解析中に配列を呼び出しBind
	private ArrayList<Object> aryBind;

	/*
	 * コンストラクタ
	 * */
	public Db_access() {
		con = null;
		stmt = null;
		pstmt = null;
		rs = null;
		kbn = "MySQL";
		errMsg = "";
		aryColmns = null;
		strDbName = "";
		intType = 0;
	}

	/*
	 * コンストラクタ
	 *   引数：区分
	 *   区分の目的：接続先DBの種類(MySQL,Oracle)
	 * */
	public Db_access(String strKbn) {
		con = null;
		stmt = null;
		pstmt = null;
		rs = null;
		kbn = strKbn;
		errMsg = "";
		aryColmns = null;
		strDbName = "";
		intType = 0;
	}

	/*
	 * SQL設定メソッド
	 * クラスのフィールドに引数のSQL文を格納
	 * */
	public void setstrSQL(String in_sql) {
		strSQL = in_sql;
	}

	/*
	 * ステートメント生成メソッド
	 * 戻り値：成功(true) or 失敗(false)
	 * 失敗の時は、errMsgにメッセージ格納
	 * */
	public boolean createStmt() {
		try {
			stmt = con.createStatement();
		}catch (Exception e) {
			// TODO: handle exception
			errMsg = "ステートメント生成に失敗。";
			return false;
		}
		return true;
	}

	/*
	 * プリペアドステートメント生成メソッド
	 * 戻り値：成功(true) or 失敗(false)
	 * 失敗の時は、errMsgにメッセージ格納
	 * */
	public boolean createPStmt() {
		try {
			pstmt = con.prepareStatement(strSQL);
		}catch (Exception e) {
			// TODO: handle exception
			errMsg = "プリペアドステートメント生成に失敗。";
			return false;
		}
		return true;
	}

	/*
	 * SQL実行メソッド
	 * 戻り値：成功(true) or 失敗(false)
	 * 失敗の時は、errMsgにメッセージ格納
	 *   createStmt関数：ステートメント作成する関数
	 *   storeData関数：抽出データを配列に格納する関数
	 * */
	public boolean exec() {
		try {
			switch (intType) {
			case 1://stmt,select
				//ステートメント生成(Nullの場合のみ)
				if(stmt == null) {
					createStmt();
				}
				rs = stmt.executeQuery(strSQL);
				storeData();                   //select結果の格納関数呼び出し
				break;
			case 2://pstmt,select
				//プリペアド設定用
				rs = pstmt.executeQuery();
				storeData();                   //select結果の格納関数呼び出し
				break;
			case 3://stmt,no-select
				//ステートメント生成(Nullの場合のみ)
				if(stmt == null) {
					createStmt();
				}
				stmt.executeUpdate(strSQL);    //そのまま実行
				break;
			case 4://pstmt,no-select
				pstmt.executeUpdate();         //プリペアド実行
				break;
			default:
				break;
			}
		}catch (Exception e) {
			// TODO: handle exception
			errMsg = "実行エラー["+strSQL+","+e.getMessage()+"]";
			return false;
		}
		return true;

	}

	/*
	 * SQL整形メソッド
	 * 戻り値：true(プリペアド) or false(プリペアド以外)
	 * SQL文を小文字に変換→" "で分割して格納
	 * →?をチェックして存在していればtrue,存在しなければfalse
	 * */
	public boolean sqlShaping() {
		//格納済みSQLを小文字に変換(検索文字等に大文字くると・・・)
		strSQL = strSQL.toLowerCase();
		//格納済みSQLを" "で分割し、配列に格納
		arySQL = strSQL.split(" ");

		//SQL種類の判定
		if(strSQL.indexOf("?") > 0) {
			return true;
		}else{
			return false;
		}
	}
	/*
	 * SQLの構文解析する関数(拡張性あり)
	 * 戻り値：成功(true) or 失敗(false)
	 * 失敗の時は、errMsgにメッセージ格納
	 *   sqlType関数：SQL種類を判定する関数
	 *   getColumns関数：全列抽出時にDBより列定義を取得する関数
	 * */
	public boolean sqlParse() {
		try {
			sqlType();

			//fromの初回出現位置を取得(+1でテーブル名、-1で列名)
			//  繰り返し変数iをforの後方で流用する為、宣言を抜き出した。
			int i=0;
			for(i=0;i<arySQL.length;i++) {
				if(arySQL[i].equals("from")) {
					break;
				}
			}
			//SQL文で*の出現位置を捜索
			if(strSQL.indexOf('*') >= 0) {
				//抽出列が全列の場合
				getColumns(arySQL[i+1]);
			}else {
				//SQLから列名を取得
				//,で分割
				String[] col = arySQL[i-1].split(",");
				aryColmns = new ArrayList<String>();
				for(int j=0;j<col.length;j++) {
					aryColmns.add(col[j]);
				}
			}

			//プリペアド対応(SQL文中の?を検索)
			if(strSQL.indexOf("?") >= 0) {

				// ?がある場合は、?の個数を取得。
				int intMarkCnt = 0;
				for(char c: strSQL.toCharArray()) {
					if(c == '?'){
						intMarkCnt++;
					}
				}
				//    ?の個数と配列要素数の比較。
				if(aryBind.size() == intMarkCnt) {
					//       一致しない場合は、Bind不可
					//       一致する場合は、配列とプリペアドをBindする。

					//プリペアドなかった時の生成(基本、通らないが念の為)
					if(pstmt == null) {
						createPStmt();
					}
					/* ここのロジックは変えない予定だけど、
					 * 値判定のロジックの影響で変えるかも。。。。 */
					int intBindCol = 1;                    //
					for(Object obj:aryBind) {              //拡張for文(phpのforeachと同等)
						Class cl = obj.getClass();         //配列内からClassの抜き出し
						String ClassType = cl.getName();   //抜き出したからClassからクラス名取得
						switch (ClassType) {               //クラス名からプリペアドへのバインドを変える
							case "java.lang.String":
								pstmt.setString(intBindCol, (String)obj);
								break;
							case "java.lang.Integer":
								pstmt.setInt(intBindCol, (Integer)obj);
								break;
							case "java.lang.Date":
								pstmt.setDate(intBindCol, (Date)obj);
								break;
							default:
								pstmt.setObject(intBindCol, obj);
								break;
						}
						intBindCol++;
					}
				}
			}
			// ?が無い場合は、プリペアドではない。
		}catch (Exception e) {
			// TODO: handle exception
			errMsg = "パースエラー:["+strSQL+"]";
			return false;
		}
		return true;
	}

	/*
	 * DBから列名を取得する関数(現行はMySQL限定)
	 * 戻り値：成功(true) or 失敗(false)
	 * 失敗の時は、errMsgにメッセージ格納
	 * */
	private boolean getColumns(String strTable) {

		//ステートメント生成(Nullの場合のみ)
		if(stmt == null) {
			createStmt();
		}
		//列定義を抽出するSQL文の設定
		String strSqlDesc = "";
		strSqlDesc += " select * from information_schema.columns where ";
		strSqlDesc += " table_schema = '"+strDbName+"' and ";
		strSqlDesc += " table_name = '"+strTable+"' ";
		strSqlDesc += " order by ";
		strSqlDesc += " table_name, ";
		strSqlDesc += " ordinal_position ";
		try {
			aryColmns = new ArrayList<String>();
			rs = stmt.executeQuery(strSqlDesc);
			//列名を配列に格納
			while(rs.next()) {
				aryColmns.add(rs.getString("column_name"));
			}
		}catch (SQLException e) {
			// TODO: handle exception
			errMsg = "実行エラー["+strSQL+","+e.getMessage()+"]";
			return false;
		}
		return true;
	}

	/*
	 * SQLの実行種類判定
	 * 戻り値：整数
	 * 失敗の時は、errMsgにメッセージ格納
	 * */
	private int sqlType() {
		//SQL配列の先頭を元にSELECT文か判定
		//(switchで文字列使用=Java7以降)
		switch (arySQL[0]) {
		case "select":
			//select文
			if(strSQL.indexOf("?") < 0) {        //?がなければ1
				intType = 1;
			}else { //?があれば2
				intType = 2;
			}
			break;
		default:
			//select文以外
			if(strSQL.indexOf("?") < 0) {        //?がなければ3
				intType = 3;
			}else { //pstmtがあれば4
				intType = 4;
			}
			break;
		}
		return intType;
	}

	/*
	 * エラーメッセージ取得用関数
	 * 戻り値：文字列
	 * */
	public String getMsg() {
		return errMsg;
	}

	/*
	 * DB接続用関数
	 *   引数なし(デフォルト)
	 * 戻り値：成功(true) or 失敗(false)
	 *   Db_interfaceの定数を利用
	 *   失敗した場合は、メッセージを格納
	 * */
	public boolean connect() {
		String strURL = URL_TOP;
		String user = "";
		String pass = "";
		switch (kbn) {
		case "MySQL":
			strURL += URL_MYSQL;
			strURL += "//"+URL_LHOST;
			strURL += "/"+URL_DNAME;
			strDbName = URL_DNAME;
			user = "root";
			pass = "root";
			break;
		case "Oracle":
			strURL += URL_ORACLE;
			strURL += "@"+URL_LHOST;
			strURL += URL_OPORT;
			strURL += ":"+URL_DNAME;
			strDbName = URL_DNAME;
			user = "system";
			pass = "oraclexe";
			break;
		default:
			strURL += URL_MYSQL;
			strURL += "//"+URL_LHOST;
			strURL += "/"+URL_DNAME;
			strDbName = URL_DNAME;
			break;
		}
		try {
			this.con = DriverManager.getConnection(strURL, user, pass);
		}catch (SQLException e) {
			// TODO: handle exception
			errMsg = "接続エラー["+strURL+","+user+","+pass+"]";
			return false;
		}
		return true;
	}

	/*
	 * DB接続用関数
	 *   引数あり
	 * 戻り値：成功(true) or 失敗(false)
	 *   Db_interfaceの定数と引数の情報を利用
	 *   失敗した場合は、メッセージを格納
	 * */
	public boolean connect(String host,String dbname,String user,String pass) {

		String strURL = URL_TOP;
		switch (kbn) {
		case "MySQL":
			strURL += URL_MYSQL;
			strURL += "//"+host;
			strURL += "/"+dbname;
			break;
		case "Oracle":
			strURL += URL_ORACLE;
			strURL += "@"+host;
			strURL += URL_OPORT;
			strURL += ":"+dbname;
			break;
		}
		strDbName = dbname;
		try {
			this.con = DriverManager.getConnection(strURL, user, pass);
		}catch (SQLException e) {
			// TODO: handle exception
			errMsg = "接続エラー["+strURL+","+user+","+pass+"]";
			return false;
		}
		return true;
	}

	/*
	 * ドライバ読み込み用関数
	 *   引数なし(デフォルト)
	 * 戻り値：成功(true) or 失敗(false)
	 *   Db_interfaceの定数を利用
	 *   失敗した場合は、メッセージを格納
	 * */
	public boolean dLoad() {
		String strDriver = "";
		try {
			//以下、switch文はJava7以降のみ有効。
			switch (this.kbn) {
			case "Oracle":
				strDriver = ORACLE_DRIVER;
				break;
			case "MySQL":
			default:
				strDriver = MYSQL_DRIVER;
				break;
			}
			Class.forName(strDriver);
		}catch (Exception e) {
			errMsg = "ドライバロードに失敗";
			return false;
		}
		return true;
	}
	/*
	 * DB切断用関数
	 *   引数：なし
	 * 戻り値：なし
	 * */
	public void close() {
		try {
			if(rs != null) {
				rs.close();
			}
			if(pstmt != null) {
				pstmt.close();
			}
			if(stmt != null) {
				stmt.close();
			}
			if(con != null) {
				con.close();
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

	/*
	 * データ格納用関数
	 *   引数：なし
	 * 戻り値：なし
	 *   抽出済みのデータを動的配列に格納。
	 *   失敗した場合は、メッセージを格納
	 * */
	private void storeData() {
		//配列にデータを格納
		//  利用データ：aryColumns(列名格納配列)
		try {
			aryData = new ArrayList<HashMap<String, String>>();
			while(rs.next()) {
				HashMap<String, String> map =
						new HashMap<String,String>();
				//列名の配列を元に、抽出結果を取得し配列に格納。
				for(int i=0;i<aryColmns.size();i++) {
					map.put(aryColmns.get(i),rs.getString(aryColmns.get(i)));
				}
				aryData.add(map);
			}
		}catch (SQLException e) {
			// TODO: handle exception
			this.errMsg = "storeData err:"+e.getMessage();
		}
	}


	/*
	 * データ取得関数
	 *   引数：なし
	 * 戻り値：配列
	 *   取得済みのデータ配列を返す関数。
	 * */
	public ArrayList<HashMap<String, String>> getAryData(){
		return aryData;
	}
	/*
	 * 列名取得関数
	 *   引数：なし
	 * 戻り値：配列
	 *   取得済みの列名配列を返す関数。
	 * */
	public ArrayList<String> getAryColmns(){
		return aryColmns;
	}

	/*
	 * プリペアド用配列への設定関数(セッター)
	 *   引数：一次元配列
	 *   戻り値：なし
	 */
	public void setAryBind(ArrayList<Object> obj) {
		aryBind = obj;
		//
	}

}

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
// import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;



//サーブレットクラスを作成するにあたって
//ルールが３つ(教科書p83～)
//①必ずHttpServletクラスを継承する。
//②doGetメソッドの再定義を必ずする。
//③必要になるクラスは、必ずimportする。
//Tomcat7以降
@WebServlet("/R06_PDF_Servlet")
public class R06_PDF_Servlet extends HttpServlet {

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

		//PDF文書のオブジェクトを作成
		//Documentのクラス
		Document doc = new Document(PageSize.A4, 30, 30, 30, 30);

		//出力用ストリームを作成
		//ByteArrayOutputStreamクラス
		ByteArrayOutputStream byteout = new ByteArrayOutputStream();

		try {
			//PDF出力用クラス
			//PDFWriterクラス
			PdfWriter pdfwriter = PdfWriter.getInstance(doc, byteout);

			//フォント指定
			//(ゴシック15pt(太字)
			Font font_header = new Font(BaseFont.createFont("HeiseiKakuGo-W5","UniJIS-UCS2-H",BaseFont.NOT_EMBEDDED),15,Font.BOLD);
		    //ゴシック11pt
			Font font_g11 = new Font(BaseFont.createFont("HeiseiKakuGo-W5","UniJIS-UCS2-H",BaseFont.NOT_EMBEDDED),11);
			//ゴシック24pt
			Font font_g24 = new Font(BaseFont.createFont("HeiseiKakuGo-W5","UniJIS-UCS2-H",BaseFont.NOT_EMBEDDED),24);
		    //ゴシック10pt
			Font font_g10 = new Font(BaseFont.createFont("HeiseiKakuGo-W5","UniJIS-UCS2-H",BaseFont.NOT_EMBEDDED),10);
			//明朝10pt
			Font font_m10 = new Font(BaseFont.createFont("HeiseiMin-W3", "UniJIS-UCS2-HW-H",BaseFont.NOT_EMBEDDED),10);
		    //ゴシック11pt(下線あり)
			Font font_underline_11 = new Font(BaseFont.createFont("HeiseiKakuGo-W5","UniJIS-UCS2-H",BaseFont.NOT_EMBEDDED),11,Font.UNDERLINE);
			//ゴシック11pt(赤)
			Font font_red_11 = new Font(BaseFont.createFont("HeiseiKakuGo-W5","UniJIS-UCS2-H",BaseFont.NOT_EMBEDDED),11);
			font_red_11.setColor(255,0,0);


			//ヘッダー・フッターの設定
			//  pdfwriterのsetPageEvent
			pdfwriter.setPageEvent(
					new PdfPageEventHelper() {
						public void onEndPage(PdfWriter pdfw,Document docum) {
							//ヘッダー・フッター設定用の変数
							//PdfContentByteの生成
							PdfContentByte pdfcd = pdfw.getDirectContent();

							BaseFont head_font = null;

							try {
								head_font = BaseFont.createFont("HeiseiKakuGo-W5","UniJIS-UCS2-H",BaseFont.NOT_EMBEDDED);
							}catch (Exception e) {
								// TODO: handle exception
							}

							pdfcd.setFontAndSize(head_font, 11);

							//描画中のPDFを保存
							pdfcd.saveState();

							//テキスト出力開始
							pdfcd.beginText();

							//ヘッダーの設定

							String strHead = "create by む";

							pdfcd.showTextAligned(PdfContentByte.ALIGN_RIGHT, strHead, docum.right(), docum.top(-15), 0);

							//フッターの設定
							String strPageNo = " - "+pdfw.getPageNumber()+"-";
							pdfcd.showTextAligned(PdfContentByte.ALIGN_CENTER, strPageNo, (docum.left()+docum.right())/2, docum.bottom(-30), 0);

							//テキスト出力終了
							pdfcd.endText();

							//ContentByteに書き出したデータを再設定
							pdfcd.restoreState();
						}
					}
					);

			//文章の開始
			doc.open();

			//文章(文字)の追加
			//Paragraphクラス
			//Paragraph para_1 = new Paragraph("初めてのPDF", font_red_11);

			//doc.add(para_1);

			//doc.add(new Paragraph("風邪には注意！",font_underline_11));
			doc.add(new Paragraph("Welocome to a whole new document experience.",font_g24));
			doc.add( new Paragraph("With Adobe Document Cloud — which includes the world’s leading PDF and electronic signature solutions — you can turn manual document processes into efficient digital ones. Now your team can take quick action on documents, workflows, and tasks — across multiple screens and devices — anywhere, any time, and inside your favorite Microsoft and enterprise apps.",font_g10));


			//画像の読み込み

			//requestからプロジェクトのパスをもらう。
			String strPjPath = req.getRealPath("/");

			Image img = Image.getInstance(strPjPath+"images/adobe.png");
			//埋め込み先の座標設定(表示位置)
			//（注意点）原点は左下
			img.setAbsolutePosition(40, doc.getPageSize().getHeight()-500);

			//元画像の比率設定
			double dScale = 1;
			Double dWidth = img.getWidth()*dScale;
			Double dHeight = img.getHeight()*dScale;
			img.scaleAbsolute(dWidth.floatValue(),dHeight.floatValue());


			PdfContentByte pdfc = pdfwriter.getDirectContent();
			pdfc.addImage(img);

			//改ページ(次ページ)
			doc.newPage();
			//doc.add(new Paragraph("会議、嫌だ",font_underline_11));

			//表形式の表示
			// PdfPTable,PdfPcell
			//   ↓PDFPTableの引数は列数
			PdfPTable ptable = new PdfPTable(3);

			//Cellの作成
			PdfPCell cell = new PdfPCell(new Paragraph("Ad",font_g11));

			//セルの背景色(グレー)
			cell.setGrayFill(0.8f);

			ptable.addCell(cell);
			ptable.addCell(new PdfPCell(new Paragraph("o",font_g11)));
			ptable.addCell(new PdfPCell(new Paragraph("be",font_g11)));

			//セルの結合(setColspan:列結合,setRowspan:行結合
			//セルの作成
			PdfPCell cell_2_1 = new PdfPCell(new Paragraph("Adobe,Adobe,Adobe,Adobe,Adobe,Adobe",font_g11));
			cell_2_1.setColspan(2);
			PdfPCell cell_2_2 = new PdfPCell(new Paragraph("PDF!PDF!",font_g11));
			ptable.addCell(cell_2_1);
			ptable.addCell(cell_2_2);

			doc.add(ptable);



			//文章の終了
			doc.close();

		}catch (DocumentException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		//ブラウザにPDF出力
		resp.setContentType("application/pdf");
		resp.setContentLength(byteout.size());
		java.io.OutputStream out = resp.getOutputStream();
		out.write(byteout.toByteArray());
		out.close();
	}


}

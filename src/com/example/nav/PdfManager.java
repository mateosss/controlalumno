//clase encargada de manejar todo lo relacionado al pdf
package com.example.nav;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public abstract class PdfManager {
	private static Context mContext;
	private static Font catFont;
	private static Font subFont;
	private static Font smallBold;
	private static Font smallFont;
	private static Font italicFont;
	private static Font italicFontBold;
	private static BaseFont unicode;
	private static File fontFile = new File("assets/fonts/arialuni.ttf");

	// Constructor
	public PdfManager(Context context) throws IOException, DocumentException {

	}

	// crea el directorio y el archivo a guardar o lo sobrescribe en su defecto
	private static String createDirectoryAndFileName(String nombre) {

		String FILENAME = nombre + ".pdf";
		String fullFileName = "";
		// Obtenemos el directorio raiz "/sdcard"
		String extStorageDirectory = Environment.getExternalStorageDirectory()
				+ "/MisCursos";
		File pdfDir = new File(extStorageDirectory + File.separator);

		try {
			if (!pdfDir.exists()) {
				pdfDir.mkdir();
			}

			fullFileName = Environment.getExternalStorageDirectory()
					+ "/MisCursos" + "/" + FILENAME;

			File outputFile = new File(fullFileName);

			if (outputFile.exists()) {
				outputFile.delete();
			}
		} catch (ActivityNotFoundException e) {
			Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		return fullFileName;
	}

	// agrega una linea vacía al pdf
	private static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

	// envia un intent para mostrar un archivo pdf
	public static void showPdfFile(String fileName, Context context) {
		String sdCardRoot = Environment.getExternalStorageDirectory().getPath()
				+ "/MisCursos";
		String path = sdCardRoot + "/" + fileName + ".pdf";

		File file = new File(path);
		System.out.println("PATH: " + path);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/pdf");
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			context.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(context,
					context.getResources().getString(R.string.no_pdf),
					Toast.LENGTH_SHORT).show();
		}
	}

	// envia un intent para enviar un arhchivo pdf por mail
	public static void sendPdfByEmail(String fileName, String emailTo,
			String emailCC, Context context) {

		Intent emailIntent = new Intent(Intent.ACTION_SEND);

		emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { emailTo });
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailCC);
		emailIntent.putExtra(Intent.EXTRA_TEXT, context.getResources()
				.getString(R.string.mail_text));

		String sdCardRoot = Environment.getExternalStorageDirectory().getPath()
				+ "/MisCursos";
		String fullFileName = sdCardRoot + File.separator + fileName + ".pdf";

		Uri uri = Uri.fromFile(new File(fullFileName));
		emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
		emailIntent.setType("application/pdf");

		context.startActivity(Intent.createChooser(emailIntent, context
				.getResources().getString(R.string.mail_toast)));
	}

	// crea un pdf con todo el formato de un alumno
	public static void crearPdfAlumno(Context context, Curso curso,
			Alumno alumno) {
		try {
			mContext = context;
			unicode = BaseFont.createFont(fontFile.getAbsolutePath(),
					BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			catFont = new Font(unicode, 22, Font.BOLD, BaseColor.BLACK);
			subFont = new Font(unicode, 16, Font.BOLD, BaseColor.BLACK);
			smallBold = new Font(unicode, 12, Font.BOLD, BaseColor.BLACK);
			smallFont = new Font(unicode, 12, Font.NORMAL, BaseColor.BLACK);
			italicFont = new Font(unicode, 12, Font.ITALIC, BaseColor.BLACK);
			italicFontBold = new Font(unicode, 12, Font.ITALIC | Font.BOLD,
					BaseColor.BLACK);
			String fullFileName = createDirectoryAndFileName(alumno.getNombre());
			if (fullFileName.length() > 0) {
				// /////////////////////
				Document document = new Document();
				PdfWriter.getInstance(document, new FileOutputStream(
						fullFileName));
				document.open();
				document.addTitle(context.getResources().getString(
						R.string.pdf_titulo)
						+ alumno.getNombre());
				document.addSubject(context.getResources().getString(
						R.string.pdf_asunto));
				document.addKeywords(context.getResources().getString(
						R.string.pdf_keywords)
						+ alumno.getNombre());
				document.addAuthor(Properties.getMail());
				document.addCreator(Properties.getMail());
				// /////////////////////
				Bitmap bitMap = BitmapFactory.decodeResource(
						mContext.getResources(), R.drawable.ic_launcher);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitMap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
				byte[] bitMapData = stream.toByteArray();
				Image image = Image.getInstance(bitMapData);
				image.setAbsolutePosition(400f, 650f);
				document.add(image);
				// /////////////////////
				Paragraph preface = new Paragraph();
				addEmptyLine(preface, 1);
				preface.add(new Paragraph(alumno.getNombre(), catFont));
				preface.add(new Paragraph(context.getResources().getString(
						R.string.pdf_curso)
						+ curso.getCurso(), italicFont));
				preface.add(new Paragraph(context.getResources().getString(
						R.string.pdf_fecha)
						+ new Date().toString(), italicFont));
				preface.add(new Paragraph(context.getResources().getString(
						R.string.pdf_nota_maxima)
						+ alumno.getNotaMax(), smallFont));
				preface.add(new Paragraph(context.getResources().getString(
						R.string.pdf_nota_minima)
						+ alumno.getNotaMin(), smallFont));
				preface.add(new Paragraph(context.getResources().getString(
						R.string.pdf_estado)
						+ alumno.getEstado(), smallFont));
				preface.add(new Paragraph(context.getResources().getString(
						R.string.pdf_promedio)
						+ alumno.getPromedio(), smallFont));
				addEmptyLine(preface, 1);
				document.add(preface);

				// /////////////////////

				Paragraph paragraph = new Paragraph();

				int TABLE_COLUMNS = 3;
				PdfPTable table = new PdfPTable(TABLE_COLUMNS);
				float[] columnWidths = new float[] { 80f, 200f, 230f };
				table.setWidths(columnWidths);
				table.setWidthPercentage(100);

				// Definimos los títulos para cada una de las 5 columnas
				PdfPCell cell = new PdfPCell(new Phrase(context.getResources()
						.getString(R.string.pdf_nota), smallBold));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(context.getResources()
						.getString(R.string.pdf_fecha1), smallBold));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(context.getResources()
						.getString(R.string.pdf_anotacion), smallBold));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				// Creamos la fila de la tabla con las cabeceras
				table.setHeaderRows(1);

				// Creamos las lineas con los artículos de la factura;
				for (int i = 0; i < alumno.getHistorial().getNotas().length; i++) {

					PdfPCell cell1 = new PdfPCell();

					cell1.setPhrase(new Phrase(
							alumno.getHistorial().getNotas()[i].getNota() + "",
							smallFont));
					cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(cell1);

					cell1.setPhrase(new Phrase(
							alumno.getHistorial().getNotas()[i].getFecha()
									.toString(), smallFont));
					cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(cell1);

					cell1.setPhrase(new Phrase(
							alumno.getHistorial().getNotas()[i].getAnotacion(),
							smallFont));
					cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(cell1);
				}

				paragraph.add(table);
				document.add(paragraph);
				// /////////////////////
				document.close();

				Toast.makeText(mContext,
						context.getResources().getString(R.string.pdf_toast),
						Toast.LENGTH_SHORT).show();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// crea un pdf con todo el formato de un curso con cada alumno
	public static void crearPdfCurso(Context context, Curso curso) {
		try {
			mContext = context;
			unicode = BaseFont.createFont(fontFile.getAbsolutePath(),
					BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			catFont = new Font(unicode, 22, Font.BOLD, BaseColor.BLACK);
			subFont = new Font(unicode, 16, Font.BOLD, BaseColor.BLACK);
			smallBold = new Font(unicode, 12, Font.BOLD, BaseColor.BLACK);
			smallFont = new Font(unicode, 12, Font.NORMAL, BaseColor.BLACK);
			italicFont = new Font(unicode, 12, Font.ITALIC, BaseColor.BLACK);
			italicFontBold = new Font(unicode, 12, Font.ITALIC | Font.BOLD,
					BaseColor.BLACK);
			String fullFileName = createDirectoryAndFileName(curso.getCurso());
			if (fullFileName.length() > 0) {
				// /////////////////////
				Document document = new Document();
				PdfWriter.getInstance(document, new FileOutputStream(
						fullFileName));
				document.open();
				document.addTitle(context.getResources().getString(
						R.string.pdf_titulo)
						+ curso.getCurso());
				document.addSubject(context.getResources().getString(
						R.string.pdf_asunto));
				document.addKeywords(context.getResources().getString(
						R.string.pdf_keywords)
						+ curso.getCurso());
				document.addAuthor(Properties.getMail());
				document.addCreator(Properties.getMail());
				// /////////////////////
				Bitmap bitMap = BitmapFactory.decodeResource(
						mContext.getResources(), R.drawable.ic_launcher);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitMap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
				byte[] bitMapData = stream.toByteArray();
				Image image = Image.getInstance(bitMapData);
				image.setAbsolutePosition(400f, 650f);
				document.add(image);
				// /////////////////////
				Paragraph preface = new Paragraph();
				addEmptyLine(preface, 1);
				preface.add(new Paragraph(curso.getCurso(), catFont));
				preface.add(new Paragraph(context.getResources().getString(
						R.string.pdf_cantidad_de_alumnos)
						+ curso.getAlumnos().length, italicFont));
				preface.add(new Paragraph(context.getResources().getString(
						R.string.pdf_fecha)
						+ new Date().toString(), italicFont));

				addEmptyLine(preface, 1);
				preface.add(new Paragraph(context.getResources().getString(
						R.string.pdf_alumnos), italicFontBold));
				for (int i = 0; i < curso.getAlumnos().length; i++) {
					preface.add(new Paragraph(context.getResources().getString(
							R.string.separador)
							+ curso.getAlumnos()[i].getNombre(), italicFont));
				}
				addEmptyLine(preface, 1);
				document.add(preface);

				// /////////////////////
				Alumno alumno = new Alumno(null);
				for (int i = 0; i < curso.getAlumnos().length; i++) {
					alumno = curso.getAlumnos()[i];
					Paragraph prefaceAlumn = new Paragraph();
					addEmptyLine(prefaceAlumn, 1);
					prefaceAlumn
							.add(new Paragraph(alumno.getNombre(), catFont));
					prefaceAlumn.add(new Paragraph(context.getResources()
							.getString(R.string.pdf_curso) + curso.getCurso(),
							catFont));
					prefaceAlumn.add(new Paragraph(context.getResources()
							.getString(R.string.pdf_fecha)
							+ new Date().toString(), italicFont));
					prefaceAlumn.add(new Paragraph(context.getResources()
							.getString(R.string.pdf_nota_maxima)
							+ alumno.getNotaMax(), smallFont));
					prefaceAlumn.add(new Paragraph(context.getResources()
							.getString(R.string.pdf_nota_minima)
							+ alumno.getNotaMin(), smallFont));
					prefaceAlumn.add(new Paragraph(context.getResources()
							.getString(R.string.pdf_estado)
							+ alumno.getEstado(), smallFont));
					prefaceAlumn.add(new Paragraph(context.getResources()
							.getString(R.string.pdf_promedio)
							+ alumno.getPromedio(), smallFont));
					addEmptyLine(prefaceAlumn, 1);
					document.add(prefaceAlumn);

					Paragraph paragraph = new Paragraph();

					int TABLE_COLUMNS = 3;
					PdfPTable table = new PdfPTable(TABLE_COLUMNS);
					float[] columnWidths = new float[] { 80f, 200f, 230f };
					table.setWidths(columnWidths);
					table.setWidthPercentage(100);

					// Definimos los títulos para cada una de las 5 columnas
					PdfPCell cell = new PdfPCell(new Phrase(context
							.getResources().getString(R.string.pdf_nota),
							smallBold));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(context.getResources()
							.getString(R.string.pdf_fecha1), smallBold));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(context.getResources()
							.getString(R.string.pdf_anotacion), smallBold));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(cell);

					// Creamos la fila de la tabla con las cabeceras
					table.setHeaderRows(1);

					// Creamos las lineas con los artículos de la factura;
					for (int j = 0; j < alumno.getHistorial().getNotas().length; j++) {

						PdfPCell cell1 = new PdfPCell();

						cell1.setPhrase(new Phrase(alumno.getHistorial()
								.getNotas()[j].getNota() + "", smallFont));
						cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
						table.addCell(cell1);

						cell1.setPhrase(new Phrase(alumno.getHistorial()
								.getNotas()[j].getFecha().toString(), smallFont));
						cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
						table.addCell(cell1);

						cell1.setPhrase(new Phrase(alumno.getHistorial()
								.getNotas()[j].getAnotacion(), smallFont));
						cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
						table.addCell(cell1);

					}
					paragraph.add(table);

					document.add(paragraph);
				}

				// /////////////////////
				document.close();

				Toast.makeText(mContext,
						context.getResources().getString(R.string.pdf_toast),
						Toast.LENGTH_SHORT).show();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

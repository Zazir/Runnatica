package com.runnatica.runnatica.PDF;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.runnatica.runnatica.poho.Usuario;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class PlantillaPDF {

    private Usuario usuario = Usuario.getUsuarioInstance();


    //Datos para enviar correo
    private String correo = "runnaticapp@gmail.com";
    private String contraseña = "Lotoloto2125";
    private Session session;

    //Text Constant
    private Font TITULOS = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private Font SUBTITULOS = new Font(Font.FontFamily.HELVETICA, 14, Font.ITALIC);
    private Font TEXTO = new Font(Font.FontFamily.TIMES_ROMAN, 12);
    private Font FECHA = new Font(Font.FontFamily.ZAPFDINGBATS, 10, Font.BOLD);

    private Context context;

    public File archivoPDF;
    public FileProvider archivoPDFN;
    private Document documento;
    private PdfWriter pdfWriter;
    private Paragraph parrafo;

    public PlantillaPDF(Context context) {
        this.context = context;
    }

    public void crearArchivo() {
        File folder = new File(Environment.getExternalStorageDirectory().toString(), "UserDocs");
        if(!folder.exists())
            folder.mkdir();
        archivoPDF = new File(folder, "Datos_competidor.pdf");
    }

    public void abrirArchivo() {
        try{
            documento = new Document(PageSize.A4);
            pdfWriter = PdfWriter.getInstance(documento, new FileOutputStream(archivoPDF));
            documento.open();
        }catch (Exception e){
            Log.e("Open pdf document", "Unable to open document: " + e.toString());
        }
    }

    public void cerrarDocumento() {
        documento.close();
    }

    public void addMetadata(String titulo, String subject, String author) {
        documento.addTitle(titulo);
        documento.addSubject(subject);
        documento.addSubject(author);
    }

    public void addHeaders(String TituloDoc, String SubtituloDoc, String FechaDoc) {
        try {
            parrafo = new Paragraph();
            addChildParagraph(new Paragraph(TituloDoc, TITULOS));
            addChildParagraph(new Paragraph(SubtituloDoc, SUBTITULOS));
            addChildParagraph(new Paragraph("Emitido: " + FechaDoc, FECHA));
            parrafo.setSpacingAfter(30);
            documento.add(parrafo);
        }catch (Exception e){
            Log.e("Create pdf document", "Unable to create document: " + e.toString());
        }
    }

    public void addImage(Bitmap bitmap) {
        try {
            Image image = Image.getInstance("");
            documento.add(image);
        }
        catch(IOException | DocumentException ex)
        {
            return;
        }
    }

    public void addParagraph(String contenidoParrafo) {
        try {
            parrafo = new Paragraph(contenidoParrafo, TEXTO);
            parrafo.setSpacingAfter(5);
            documento.add(parrafo);
        }catch (Exception e){
            Log.e("Add paragraph", "Unable to add a paragraph on the document: " + e.toString());
        }
    }

    /**
     *      Método privado utilizado para definir el primer párrafo del documento
     *      Este método alinea al centro los valores que le pases como parámetro
     * */
    private void addChildParagraph(Paragraph childParagraph) {
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        parrafo.add(childParagraph);
    }

    /*public void sendPDF(Activity activity) {
        if (archivoPDF.exists()) {
        sendMail();

            Uri uri = Uri.fromFile(archivoPDF);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            try {
                activity.startActivity(intent);
            }catch (ActivityNotFoundException ae){
                Toast.makeText(activity.getApplicationContext(), "No tienes aplicaciones para visualizar un pdf", Toast.LENGTH_LONG).show();
            }
        }else
            Toast.makeText(activity.getApplicationContext(), "No se pudo encontrar el archivo pdf", Toast.LENGTH_SHORT).show();
        }*/


    public void sendMail() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.googlemail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        try {
            session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(correo, contraseña);
                }
            });

            if (session != null){
                BodyPart text = new MimeBodyPart();
                BodyPart adjunto = new MimeBodyPart();
                MimeMultipart mimeMultipart = new MimeMultipart();

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(correo));

                text.setText("Datos de la inscripción");
                adjunto.setDataHandler(new DataHandler(new FileDataSource(archivoPDF.getAbsolutePath())));
                adjunto.setFileName("Datos_inscripcion.pdf");
                mimeMultipart.addBodyPart(text);
                mimeMultipart.addBodyPart(adjunto);

                message.setSubject("Runnatica Inscrición");
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(usuario.getCorreo()));
                message.setContent(mimeMultipart, "text/html; charset=utf-8");

                Transport.send(message);
            }
        }catch (Exception e) {
            Toast.makeText(context, "No se pudo enviar el email con tus datos de inscripción", Toast.LENGTH_SHORT).show();
            Log.e("Error con mail", "" + e);
        }
    }
}

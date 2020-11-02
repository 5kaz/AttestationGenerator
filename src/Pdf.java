import com.itextpdf.text.* ;
import com.itextpdf.text.pdf.*;
import java.util.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;	

public class Pdf {
	private static final String FILE__NAME = "attest.pdf";
	private static final String SOURCE__FILE__NAME = "./ressources/attest_vierge.pdf";
	private static final String MAIL_CC = "soso.hw34@gmail.com";

	private String MAIL_DEST = "soso.hw34@gmail.com";
	private String nom;	
	private String date_naissance;
	private String lieu_naissance;
	private String adresse;
	private String ville;
	
	public static void main(String[] args) throws IOException {		
		new GUI();
	}
	
	
	public String getNom() {
		return nom;
	}


	public String getDate_naissance() {
		return date_naissance;
	}


	public String getLieu_naissance() {
		return lieu_naissance;
	}


	public String getAdresse() {
		return adresse;
	}


	public String getVille() {
		return ville;
	}

	public String getMail() {
		return MAIL_DEST;
	}

	public void setMail(String mail) {
		this.MAIL_DEST = mail;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}


	public void setDate_naissance(String date_naissance) {
		this.date_naissance = date_naissance;
	}


	public void setLieu_naissance(String lieu_naissance) {
		this.lieu_naissance = lieu_naissance;
	}


	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}


	public void setVille(String ville) {
		this.ville = ville;
	}


	public void editPdf(String nom, String date_naissance, String lieu_naissance, String ville, String adresse, String date, String heure, String raison) {
	try{
			
			Document document = new Document(PageSize.A4);
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(FILE__NAME));
			document.open();
			PdfContentByte cb = writer.getDirectContent();
			
			
			FileInputStream fis = new FileInputStream(SOURCE__FILE__NAME);
			PdfReader reader = new PdfReader(fis);
			PdfImportedPage page = writer.getImportedPage(reader, 1); 
			
			document.newPage();
			cb.addTemplate(page, 0, 0);
			
			Image check = Image.getInstance("./ressources/check.png");
			check.scaleAbsolute(14, 14);
			
			String[] raisons = {"Aller au taf/à l'école",
					"Faire des courses",
					"Consultation, examens médicaux",
					"Assistance aux personnes vulnérables",
					"Déplacement des personnes en situation de handicap",
					"Sortir prendre l'air pour 1h max",
					"Convocation judiciaire",
					"Participation à des TIG",
					"Chercher des enfants à l'école"};
			
			if (raison.equals(raisons[0])) {
				check.setAbsolutePosition(70, 583);
			}
			if (raison.equals(raisons[1])) {
				check.setAbsolutePosition(70, 534);
			}
			if (raison.equals(raisons[2])) {
				check.setAbsolutePosition(70, 486);
			}
			if (raison.equals(raisons[3])) {
				check.setAbsolutePosition(70, 449);
			}
			if (raison.equals(raisons[4])) {
				check.setAbsolutePosition(70, 413);
			}
			if (raison.equals(raisons[5])) {
				check.setAbsolutePosition(70, 388);
			}
			if (raison.equals(raisons[6])) {
				check.setAbsolutePosition(70, 315);
			}
			if (raison.equals(raisons[7])) {
				check.setAbsolutePosition(70, 291);
			}
			if (raison.equals(raisons[8])) {
				check.setAbsolutePosition(70, 267);
			}
	
			
			Paragraph NOM = new Paragraph(105,nom);
			NOM.setFirstLineIndent(100);
			
			Paragraph NAISSANCE = new Paragraph(18,date_naissance);
			NAISSANCE.add("	         	 à :  ");
			NAISSANCE.add(lieu_naissance);
			NAISSANCE.setFirstLineIndent(120);

			Paragraph VILLE = new Paragraph(19,adresse);
			VILLE.setFirstLineIndent(118);
			VILLE.add(" ,   ");
			VILLE.add(ville);
			
			Paragraph VILLE2 = new Paragraph(430,ville);
			VILLE2.setFirstLineIndent(80);
			
			Paragraph DATE = new Paragraph(20,date);
			DATE.setFirstLineIndent(65);
			DATE.add("                                          ");
			DATE.add(heure);
			
			
			document.add(NOM);
			document.add(NAISSANCE);
			document.add(VILLE);
			document.add(VILLE2);
			document.add(DATE);
			document.add(check);
			
			document.close();
		}catch(Exception e) {
			System.out.println(e);
		}
		
		System.out.println("Pdf généré");
	}
	
	public static void sendMail(String mail_dest) throws IOException {
		Properties properties = new Properties();
	    properties.setProperty("mail.transport.protocol", "smtp");
	    properties.setProperty("mail.smtp.host", "smtp.gmail.com");
	    properties.setProperty("mail.smtp.user", "attestationgenerator@gmail.com");
	    properties.setProperty("mail.smtp.starttls.enable", "true");
	    properties.setProperty("mail.smtp.port", "25");
	    Session session = Session.getInstance(properties);
	    
	    try {
	    	MimeBodyPart attachPart = new MimeBodyPart();
	    	String attachFile = FILE__NAME;
	    	attachPart.attachFile(attachFile);
	    
	    	Multipart mime = new MimeMultipart();

	        mime.addBodyPart(attachPart);

	    
	        MimeMessage message = new MimeMessage(session);
	        message.setSubject("ATTESTATION DEROGATOIRE");
	        message.setContent(mime);

	    
	        Transport transport = null;

	        transport = session.getTransport("smtp");
	        transport.connect("attestationgenerator@gmail.com", "cazouyano");
	        transport.sendMessage(message, new Address[] {  new InternetAddress(mail_dest),
	                                                        new InternetAddress(MAIL_CC) });
	        
	        if (transport != null) {
                transport.close();
            }
       
	    } catch (MessagingException e) {
	        e.printStackTrace();
	    } 
	        
		System.out.println("Message envoyé");   
	    
	}

	
}

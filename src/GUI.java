import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GUI extends JFrame implements ActionListener, KeyListener {

	JButton button1;
	JButton button2;
	JButton save;
	JTextField nom;
	JTextField prenom;
	JTextField ville;
	JTextField adresse;
	JTextField ville_naissance;
	JTextField date_naissance;
	JTextField mail;
	JComboBox raison;
	
	private String[] st= {	"NOM",
							"Prénom",
							"Ville de résidence",
							"Date de naissance",
							"Lieu de naissance",
							"Adresse postale",
							"Adresse mail"};
	
	public GUI() {
		
		
		button1 = new JButton("Generer");
		button1.setBounds(10,400,100,40);
		button1.addActionListener(this);
		button1.setFocusable(false);

		button2 = new JButton("Envoyer par mail");
		button2.setBounds(110,400,150,40);
		button2.setFocusable(false);
		button2.addActionListener(this);
		
		nom = new JTextField();
		nom.setBounds(10, 50, 250, 30);
		nom.setText("NOM");
		nom.addKeyListener(this);
		prenom = new JTextField();
		prenom.setBounds(10, 90, 250, 30);
		prenom.setText("Prénom");
		ville = new JTextField();
		ville.setBounds(10, 130, 250, 30);
		ville.setText("Ville de résidence");
		date_naissance = new JTextField();
		date_naissance.setBounds(10, 170, 250, 30);
		date_naissance.setText("Date de naissance");
		ville_naissance = new JTextField();
		ville_naissance.setBounds(10, 210, 250, 30);
		ville_naissance.setText("Lieu de naissance");
		adresse = new JTextField();
		adresse.setBounds(10, 250, 250, 30);
		adresse.setText("Adresse postale");
		mail = new JTextField();
		mail.setBounds(10, 290, 250, 30);
		mail.setText("Adresse mail");
		
		try {
			chargerProfil();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String[] raisons = {"Aller au taf/à l'école",
							"Faire des courses",
							"Consultation, examens médicaux",
							"Assistance aux personnes vulnérables",
							"Déplacement des personnes en situation de handicap",
							"Sortir prendre l'air pour 1h max",
							"Convocation judiciaire",
							"Participation à des TIG",
							"Chercher des enfants à l'école"};
		
		raison = new JComboBox(raisons);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.add(raison);
		raison.setBounds(10, 10, 250, 30);
		raison.doLayout();
		raison.setSelectedIndex(1);

		
		save = new JButton("Save");
		save.setBounds(60, 330, 150, 30);
		save.setFocusable(false);
		save.addActionListener(this);
		
		
		this.setTitle("Attestation Generator");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(270,480);
		this.setVisible(true);
		this.setResizable(false);
		this.getContentPane().setBackground(Color.GRAY);
		this.add(raison);

		this.add(button1);
		this.add(button2);
		this.add(nom);
		this.add(prenom);
		this.add(ville);
		this.add(date_naissance);
		this.add(ville_naissance);
		this.add(adresse);
		this.add(mail);
		this.add(save);

	}
	
	private void chargerProfil() throws IOException {
		File file = new File("./ressources/profil.txt");
		if (file != null) {
			BufferedReader br = new BufferedReader(new FileReader(file)); 
			
			String [] st2 = st.clone();
			int i=0;
			while (i<7 ) {
				st[i] = br.readLine();
				System.out.println("Loading field "+(i+1)+" : "+st[i]); 
			    i++;
			} 
			for (i=0; i<7; i++) {
				if (st[i]==null) {
					st[i]=st2[i];
				} 
			}
			nom.setText(st[0]);
			prenom.setText(st[1]);
			ville.setText(st[2]);
			date_naissance.setText(st[3]);
			ville_naissance.setText(st[4]);
			adresse.setText(st[5]);
			mail.setText(st[6]);
			br.close();

		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button1) {
			Pdf pdf = new Pdf();
			pdf.setAdresse(adresse.getText());
			pdf.setNom(nom.getText()+" "+prenom.getText());
			pdf.setDate_naissance(date_naissance.getText());
			pdf.setLieu_naissance(ville_naissance.getText());
			pdf.setVille(ville.getText());
			
			DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			Date dateD = new Date();
			String date = format.format(dateD);
		    
			DateFormat format2 = new SimpleDateFormat("HH:mm");
			Date heureD = new Date();
			String heure = format2.format(heureD);
			
			pdf.editPdf(pdf.getNom(), pdf.getDate_naissance(), pdf.getLieu_naissance(), pdf.getVille(), pdf.getAdresse(), date, heure, raison.getSelectedItem().toString());

		}
		
		if (e.getSource() == button2) {
			Pdf pdf = new Pdf();
			pdf.setAdresse(adresse.getText());
			pdf.setNom(nom.getText()+" "+prenom.getText());
			pdf.setDate_naissance(date_naissance.getText());
			pdf.setLieu_naissance(ville_naissance.getText());
			pdf.setVille(ville.getText());
			pdf.setMail(mail.getText());
			
			DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			Date dateD = new Date();
			String date = format.format(dateD);
		    
			DateFormat format2 = new SimpleDateFormat("HH:mm");
			Date heureD = new Date();
			String heure = format2.format(heureD);
			
			pdf.editPdf(pdf.getNom(), pdf.getDate_naissance(), pdf.getLieu_naissance(), pdf.getVille(), pdf.getAdresse(), date, heure, raison.getSelectedItem().toString());
			try {
				Pdf.sendMail(pdf.getMail());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == save) {
			try {
				File file = new File("./ressources/profil.txt");

				if (file.createNewFile()) {
					System.out.println("File created");
				}
				else {
					System.out.println("File already exists");
				}
				BufferedWriter output = new BufferedWriter(new FileWriter(file));
				
				System.out.println((nom.getText()+"\n"+prenom.getText()+"\n"+ville.getText()+"\n"+date_naissance.getText()+"\n"+ville_naissance.getText()+"\n"+adresse.getText()+"\n"+mail.getText()));
				output.write((nom.getText()+"\n"+prenom.getText()+"\n"+ville.getText()+"\n"+date_naissance.getText()+"\n"+ville_naissance.getText()+"\n"+adresse.getText()+"\n"+mail.getText()));
				
				output.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}

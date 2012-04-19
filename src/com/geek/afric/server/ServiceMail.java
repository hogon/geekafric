package com.geek.afric.server;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.geek.afric.shared.Mail;

/**
 * @author sagara Classe de gestion des Emails sortant
 */
public class ServiceMail {

	private Properties props = new Properties();
	private Session session = Session.getDefaultInstance(props, null);
	String url = "http://apps.geekafric.com/";
	private static final Logger log = Logger.getLogger(ServiceMail.class
			.getName());
	private static final String emailSender = "admin@geekafric.com";
	private static final String nameSender = "Geekafric";

	public void sendWelcomeMessage(String email, String firstname) {
		String sujet = "Bienvenue sur Geekafric!";
		String content = "Bonjour " + firstname
				+ ",<br>Merci de t'être inscrit sur Geekafric."
				+ "<br>Nous te souhaitons la bienvenue."
				+ "<br> A très bientôt pour de nouvelles mises à jour! :-)"
				+ "<br><br> <a href=\"" + url
				+ "\"> <b>L'équipe Geekafric.</b></a>";
		sendMail(email, sujet, content);

	}

	public String sendRetrievePasswordLink(String email, String userid) {

		String sujet = "Votre mot de passe sur Geekafric!";

		String content = "Bonjour, <br> Pour re-initialiser ton mot de passe clique sur le lien suivant: <br>"
				+ "<a href=\""
				+ url
				+ "#password="
				+ userid
				+ "\"> Changez mon mot de passe.</a>"
				+ "<br><br> <a href=\""
				+ url + "\"> <b>L'équipe Geekafric.</b></a>";

		sendMail(email, sujet, content);
		return content;
	}

	public void sendMail(String email, String objet, String contenu) {

		try {

			MimeMessage msg = new MimeMessage(session);
			try {
				msg.setFrom(new InternetAddress(emailSender, nameSender));
			} catch (UnsupportedEncodingException e) {
				log.warning("Problème Email:" + e.getMessage());
			}
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					email));
			msg.setSubject(objet, "utf-8");
			msg.setContent(contenu, "text/html; charset=UTF-8");
			Transport.send(msg);
		} catch (Exception e) {
			log.warning("Problème Email:" + e.getMessage());
		}

	}

	public void sendMail(Mail mail) {

		try {

			MimeMessage msg = new MimeMessage(session);
			try {
				msg.setFrom(new InternetAddress(mail.getSender(), mail
						.getSenderName()));
			} catch (UnsupportedEncodingException e) {
				log.warning("Problème Email:" + e.getMessage());
			}
			msg.addRecipient(Message.RecipientType.TO,
					new InternetAddress(mail.getReceiver()));
			msg.setSubject(mail.getObject(), "utf-8");
			msg.setContent(mail.getMessage(), "text/html; charset=UTF-8");

			Transport.send(msg);
		} catch (Exception e) {
			log.warning("Problème Email:" + e.getMessage());
		}

	}

	public String getWelcomeMessage(String firstname) {
		String content = "Bonjour "
				+ firstname
				+ ",<br>Merci de t'être inscrit sur Geekafric."
				+ "<br>Nous te souhaitons la bienvenue."
				+ "<br>Nous espérons t'offrir le maximum de ce que tu cherches."
				+ "<br><br> <a href=\"" + url
				+ "\"> <b>L'équipe Geekafric.</b></a>";
		return content;
	}

	public String getWelcomeObject() {
		return "Bienvenue sur Geekafric!";
	}

	public String sendConfirmEmailLink(String email, String id) {
		String sujet = "Confirmation de votre email!";

		String content = "Bonjour, <br> Pour confirmer votre email veuillez cliquez sur le lien suivant: <br>"
				+ "<a href=\""
				+ url
				+ "#confirm="
				+ id
				+ "\"> Confirmer mon email.</a>"
				+ "<br><br> <a href=\""
				+ url
				+ "\"> <b>L'équipe Geekafric.</b></a>";

		sendMail(email, sujet, content);
		return content;
	}
}

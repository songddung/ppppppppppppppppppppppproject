package com.example.project.email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SMTPAuthenticator extends Authenticator {
  @Override
  protected PasswordAuthentication getPasswordAuthentication() {
    return new PasswordAuthentication(
        "answhdrhks@gmail.com", "nsutdipxhxdsltpl");
  }
}
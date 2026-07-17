package com.pourymovie.provider;

public interface EmailProvider {
  void sendPasswordResetEmail(String toEmail, String resetLink);
}

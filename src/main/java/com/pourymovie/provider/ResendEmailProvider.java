package com.pourymovie.provider;

import com.pourymovie.config.AppDefaults;
import com.pourymovie.dto.request.SendEmailDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ResendEmailProvider implements EmailProvider {
  private final AppDefaults appDefaults;

  @Value("classpath:templates/emails/password-reset.html")
  private Resource templateResource;

  private String htmlTemplateContent;

  @PostConstruct
  public void init() {
    try {
      this.htmlTemplateContent =
          StreamUtils.copyToString(templateResource.getInputStream(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private final RestClient restClient =
      RestClient.builder().baseUrl("https://api.resend.com").build();

  @Async
  public void sendPasswordResetEmail(String toEmail, String resetLink) {
    String finalHtml = this.htmlTemplateContent.replace("{{RESET_LINK}}", resetLink);

    SendEmailDto requestBody =
        new SendEmailDto(
            appDefaults.getFromEmail(),
            List.of(toEmail),
            "بازیابی رمز عبور | PouryMovie",
            finalHtml);

    try {
      restClient
          .post()
          .uri("/emails")
          .header("Authorization", "Bearer " + appDefaults.getResendEmailToken())
          .contentType(MediaType.APPLICATION_JSON)
          .body(requestBody)
          .retrieve()
          .toBodilessEntity();

      log.info("Password reset email sent asynchronously to: {}", toEmail);
    } catch (Exception e) {
      log.error("Failed to send password reset email to {}: {}", toEmail, e.getMessage());
    }
  }
}

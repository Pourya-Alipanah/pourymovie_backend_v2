package com.pourymovie.config;

import com.google.genai.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GeminiConfig {

  private final AppDefaults appDefaults;

  @Bean
  public Client geminiClient() {
    return Client.builder().apiKey(appDefaults.getGeminiApiKey()).build();
  }
}

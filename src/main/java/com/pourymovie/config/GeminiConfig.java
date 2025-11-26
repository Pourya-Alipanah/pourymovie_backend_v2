package com.pourymovie.config;

import com.google.genai.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeminiConfig {

  @Autowired
  private AppDefaults appDefaults;

  @Bean
  public Client geminiClient() {
    return Client.builder()
        .apiKey(appDefaults.getGeminiApiKey())
        .build();
  }
}

package com.pourymovie.provider;

import com.google.genai.Client;
import com.google.genai.ResponseStream;
import com.google.genai.types.*;
import com.pourymovie.config.AppDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GeminiProvider {
  @Autowired
  private Client client;

  @Autowired
  private AppDefaults appDefaults;

  public ResponseStream<GenerateContentResponse> generateResponseStream(String prompt) {

    SafetySetting safetySetting = SafetySetting.builder()
            .category(HarmCategory.Known.HARM_CATEGORY_DANGEROUS_CONTENT)
            .threshold(HarmBlockThreshold.Known.BLOCK_NONE)
            .build();

    GenerateContentConfig config = GenerateContentConfig.builder()
            .safetySettings(safetySetting)
            .build();

    return client.models.generateContentStream(
            appDefaults.getGeminiModelName(),
            prompt,
            config
    );
  }
}

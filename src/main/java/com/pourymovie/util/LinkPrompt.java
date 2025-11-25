package com.pourymovie.util;

import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
public class LinkPrompt {
  private final String prompt;

  public LinkPrompt(boolean foundLink , String title , String userLanguage , @Nullable String link) {
    String languageInstruction = userLanguage.equalsIgnoreCase("fa") ?
        "Respond in Persian." :
        "Respond in English.";

    if (foundLink) {
      String resolvedLink = link != null ? link : "N/A";
      this.prompt = String.format(
              "The user requested information about \"%s\". You found it in the database. Here is the link: %s. Provide a summary, genre, and other details. %s",
              title, resolvedLink, languageInstruction
      );
    } else {
      this.prompt = String.format(
              "The user asked about \"%s\", but it does not exist in the database. Still, give a detailed summary and provide the IMDb link. %s",
              title, languageInstruction
      );
    }
  }
}

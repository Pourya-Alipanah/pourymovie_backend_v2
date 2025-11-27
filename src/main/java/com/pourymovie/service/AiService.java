package com.pourymovie.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.ResponseStream;
import com.google.genai.types.GenerateContentResponse;
import com.pourymovie.config.AppDefaults;
import com.pourymovie.dto.request.AiTitleSummaryDto;
import com.pourymovie.entity.CommentEntity;
import com.pourymovie.enums.TitleType;
import com.pourymovie.provider.GeminiProvider;
import com.pourymovie.util.BaseRequestPrompt;
import com.pourymovie.util.LinkPrompt;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AiService {

  @Autowired private GeminiProvider geminiProvider;

  @Autowired private CommentService commentService;

  @Autowired private TitleService titleService;

  @Autowired private AppDefaults appDefaults;

  public ResponseStream<GenerateContentResponse> getCommentsSummary(Long titleId) {
    var comments = commentService.getAllTitleComments(titleId);
    if (comments.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No comments found");
    }
    String commentsString =
        comments.stream().map(CommentEntity::getContent).reduce("", (a, b) -> a + "\n- " + b);

    String summaryPrompt = new BaseRequestPrompt(commentsString).getCommentSummaryPrompt();

    return geminiProvider.generateResponseStream(summaryPrompt);
  }

  public ResponseStream<GenerateContentResponse> getTitleSummary(
      AiTitleSummaryDto aiTitleSummaryDto) {
    String summaryPrompt = new BaseRequestPrompt(aiTitleSummaryDto.name()).getSummaryPrompt();

    return geminiProvider.generateResponseStream(summaryPrompt);
  }

  public void chat(String userMessage, Consumer<String> emitter) {
    String chatPrompt = new BaseRequestPrompt(userMessage).getChatPrompt();

    new Thread(
            () -> {
              try (var stream = geminiProvider.generateResponseStream(chatPrompt)) {
                processStreamChunks(stream, emitter);
              } catch (Exception e) {
                emitter.accept("Error: " + e.getMessage());
              }
            })
        .start();
  }

  private void processStreamChunks(
      ResponseStream<GenerateContentResponse> stream, Consumer<String> emitter) {
    StringBuilder jsonBuilder = new StringBuilder();
    boolean insideJson = false;

    for (GenerateContentResponse chunk : stream) {
      String text = chunk.text();
      if (text == null) continue;

      if (!insideJson && text.contains("{")) {
        insideJson = true;
        jsonBuilder.append(text);
      } else if (insideJson) {
        jsonBuilder.append(text);
        if (text.contains("}")) {
          insideJson = false;
          JsonContentDto contentDto = parseJsonContent(jsonBuilder.toString());
          var link = findTitleLinkByGeminiResponse(contentDto);
          String responsePrompt =
              new LinkPrompt(
                      link.isPresent(),
                      contentDto.movies().getFirst(),
                      contentDto.userLanguage(),
                      link.orElse(null))
                  .getPrompt();
          var responseStream = geminiProvider.generateResponseStream(responsePrompt);
          processStreamChunks(responseStream, emitter);
        }
      } else {
        emitter.accept(text);
      }
    }
  }

  private Optional<String> findTitleLinkByGeminiResponse(JsonContentDto jsonContentDto) {
    var titleName = jsonContentDto.movies.getFirst();
    try {
      var title = titleService.findLinkByTitleName(titleName);
      if (title.type().equals(TitleType.Movie)) {
        return Optional.of(
            appDefaults.getFrontendUrl()
                + "/"
                + appDefaults.getFrontendMoviePath()
                + "/"
                + title.slug());
      }
      if (title.type().equals(TitleType.Series)) {
        return Optional.of(
            appDefaults.getFrontendUrl()
                + "/"
                + appDefaults.getFrontendSeriesPath()
                + "/"
                + title.slug());
      }
    } catch (Exception e) {
      return Optional.empty();
    }
    return Optional.empty();
  }

  private JsonContentDto parseJsonContent(String json) {
    try {
      json = json.trim().replaceAll("(?s)^.*?\\{", "{").replaceAll("(?s)}.*$", "}");

      ObjectMapper mapper = new ObjectMapper();
      return mapper.readValue(json, JsonContentDto.class);

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private record JsonContentDto(List<String> movies, String userLanguage) {}
}

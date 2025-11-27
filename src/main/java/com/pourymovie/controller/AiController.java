package com.pourymovie.controller;

import com.google.genai.ResponseStream;
import com.google.genai.types.GenerateContentResponse;
import com.pourymovie.dto.request.AiTitleSummaryDto;
import com.pourymovie.service.AiService;
import jakarta.validation.Valid;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/ai")
public class AiController {
  @Autowired private AiService aiService;

  @GetMapping(value = "/comments-summary/{titleId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter commentsSummary(@PathVariable Long titleId) {
    SseEmitter emitter = new SseEmitter();

    new Thread(
            () -> {
              try (ResponseStream<GenerateContentResponse> stream =
                  aiService.getCommentsSummary(titleId)) {
                for (GenerateContentResponse chunk : stream) {
                  emitter.send(Objects.requireNonNull(chunk.text()));
                }
                emitter.complete();
              } catch (Exception e) {
                emitter.completeWithError(e);
              }
            })
        .start();

    return emitter;
  }

  @PostMapping(value = "/title-summary", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter titleSummary(@Valid @RequestBody AiTitleSummaryDto aiTitleSummaryDto) {
    SseEmitter emitter = new SseEmitter();

    new Thread(
            () -> {
              try (ResponseStream<GenerateContentResponse> stream =
                  aiService.getTitleSummary(aiTitleSummaryDto)) {
                for (GenerateContentResponse chunk : stream) {
                  emitter.send(Objects.requireNonNull(chunk.text()));
                }
                emitter.complete();
              } catch (Exception e) {
                emitter.completeWithError(e);
              }
            })
        .start();

    return emitter;
  }
}

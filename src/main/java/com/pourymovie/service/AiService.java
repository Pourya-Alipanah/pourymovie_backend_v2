package com.pourymovie.service;

import com.google.genai.ResponseStream;
import com.google.genai.types.GenerateContentResponse;
import com.pourymovie.dto.request.TitleSummaryDto;
import com.pourymovie.entity.CommentEntity;
import com.pourymovie.provider.GeminiProvider;
import com.pourymovie.util.BaseRequestPrompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AiService {

  @Autowired
  private GeminiProvider geminiProvider;

  @Autowired
  private CommentService commentService;

  public ResponseStream<GenerateContentResponse> getCommentsSummary(Long titleId){
    var comments = commentService.getAllTitleComments(titleId);
    if(comments.isEmpty()){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No comments found");
    }
    String commentsString = comments.stream()
            .map(CommentEntity::getContent)
            .reduce("", (a, b) -> a + "\n- " + b);

    String summaryPrompt = new BaseRequestPrompt(commentsString)
            .getCommentSummaryPrompt();

    return geminiProvider.generateResponseStream(summaryPrompt);

  }

  public ResponseStream<GenerateContentResponse> getTitleSummary(TitleSummaryDto titleSummaryDto) {
    String summaryPrompt = new BaseRequestPrompt(titleSummaryDto.name())
            .getSummaryPrompt();

    return geminiProvider.generateResponseStream(summaryPrompt);
  }
}

package com.pourymovie.websocket;

import com.pourymovie.service.AiService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class AiChatWebSocketHandler extends TextWebSocketHandler {
  @Autowired private AiService aiService;

  @Override
  public void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) {

    aiService.chat(
        message.getPayload(),
        chunk -> {
          try {
            session.sendMessage(new TextMessage(chunk));
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
  }
}

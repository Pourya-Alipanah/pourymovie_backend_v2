package com.pourymovie.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.util.ArrayList;

public class PageDeserializer extends JsonDeserializer<Page<?>> {
  @Override
  public Page<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    JacksonPage<?> jacksonPage = p.getCodec().readValue(p, JacksonPage.class);

    return new PageImpl<>(
        jacksonPage.getContent() != null ? jacksonPage.getContent() : new ArrayList<>(),
        PageRequest.of(
            Math.max(0, jacksonPage.getNumber()),
            jacksonPage.getSize() > 0 ? jacksonPage.getSize() : 10),
        jacksonPage.getTotalElements());
  }
}

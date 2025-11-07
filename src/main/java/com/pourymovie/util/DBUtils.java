package com.pourymovie.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class DBUtils {

  public static <T> List<T> safeJsonToList(String json, TypeReference<List<T>> typeRef) {
    if (json == null) {
      return new ArrayList<>();
    }
    try {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.readValue(json, typeRef);
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }
}

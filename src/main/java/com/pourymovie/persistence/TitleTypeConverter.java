package com.pourymovie.persistence;

import com.pourymovie.enums.TitleType;

public class TitleTypeConverter extends EnumConverter<TitleType> {
  public TitleTypeConverter() {
    super(TitleType.class);
  }
}

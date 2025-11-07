package com.pourymovie.persistence;

import com.pourymovie.enums.BaseAsymmetricEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public abstract class EnumConverter<E extends Enum<E> & BaseAsymmetricEnum>
        implements AttributeConverter<E, String> {

  private final Class<E> enumClass;

  protected EnumConverter(Class<E> enumClass) {
    this.enumClass = enumClass;
  }

  @Override
  public String convertToDatabaseColumn(E attribute) {
    return attribute == null ? null : attribute.getValue();
  }

  @Override
  public E convertToEntityAttribute(String dbData) {
    if (dbData == null) return null;
    for (E e : enumClass.getEnumConstants()) {
      if (e.getValue().equalsIgnoreCase(dbData) || e.name().equalsIgnoreCase(dbData)) {
        return e;
      }
    }
    throw new IllegalArgumentException(
            "Unknown " + enumClass.getSimpleName() + " value: " + dbData);
  }
}

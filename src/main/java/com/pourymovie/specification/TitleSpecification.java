package com.pourymovie.specification;

import com.pourymovie.dto.request.TitleFilterDto;
import com.pourymovie.entity.TitleEntity;
import com.pourymovie.enums.TitleType;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import static com.pourymovie.util.SpecificationUtil.*;

import java.util.List;

public class TitleSpecification {

  public static Specification<TitleEntity> withFilters(TitleFilterDto filters) {

    Specification<TitleEntity> spec =
        Specification.allOf(
            titleContains(filters.title()),
            typeEquals(filters.type()),
            releaseYearBetween(filters.minYear(), filters.maxYear()),
            ratingBetween(filters.minRating(), filters.maxRating()),
            flagEquals("isTop250", filters.isTop250()),
            flagEquals("hasSubtitle", filters.hasSubtitle()),
            genresIn(filters.genres()),
            actorsIn(filters.actors()),
            directorsIn(filters.directors()),
            writersIn(filters.writers()),
            countriesIn(filters.countries()));

    return spec.and(distinct());
  }

  private static Specification<TitleEntity> titleContains(String keyword) {
    return (root, query, cb) -> {
      if (keyword == null || keyword.isBlank()) return null;
      String pattern = "%" + keyword.toLowerCase() + "%";
      return cb.or(
          cb.like(cb.lower(root.get("titleEn")), pattern),
          cb.like(cb.lower(root.get("titleFa")), pattern));
    };
  }

  private static Specification<TitleEntity> typeEquals(TitleType type) {
    return (root, query, cb) -> type == null ? null : cb.equal(root.get("type"), type);
  }

  private static Specification<TitleEntity> releaseYearBetween(Integer min, Integer max) {
    return (root, query, cb) -> {
      if (min != null && max != null) return cb.between(root.get("releaseYear"), min, max);
      if (min != null) return cb.ge(root.get("releaseYear"), min);
      if (max != null) return cb.le(root.get("releaseYear"), max);
      return null;
    };
  }

  private static Specification<TitleEntity> ratingBetween(Float min, Float max) {
    return (root, query, cb) -> {
      if (min != null && max != null) return cb.between(root.get("imdbRating"), min, max);
      if (min != null) return cb.ge(root.get("imdbRating"), min);
      if (max != null) return cb.le(root.get("imdbRating"), max);
      return null;
    };
  }

  private static Specification<TitleEntity> genresIn(List<String> values) {
    return listSpec(
        values,
        (root, cb) -> {
          Join<Object, Object> join = root.join("genres");
          return join.get("nameEn").in(values);
        });
  }

  private static Specification<TitleEntity> actorsIn(List<String> values) {
    return listSpec(
        values,
        (root, cb) -> {
          Join<Object, Object> join = root.join("actorLinks");
          return join.get("person").get("nameEn").in(values);
        });
  }

  private static Specification<TitleEntity> directorsIn(List<String> values) {
    return listSpec(
        values,
        (root, cb) -> {
          Join<Object, Object> join = root.join("directorLinks");
          return join.get("person").get("nameEn").in(values);
        });
  }

  private static Specification<TitleEntity> writersIn(List<String> values) {
    return listSpec(
        values,
        (root, cb) -> {
          Join<Object, Object> join = root.join("writerLinks");
          return join.get("person").get("nameEn").in(values);
        });
  }

  private static Specification<TitleEntity> countriesIn(List<String> values) {
    return listSpec(values, (root, cb) -> root.get("country").get("nameEn").in(values));
  }
}

package com.pourymovie.util;

import com.pourymovie.entity.TitleEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public class SpecificationUtil {

  @FunctionalInterface
  public interface PredicateSupplier<T> {
    Predicate supply(Root<T> root, CriteriaBuilder cb);
  }

  public static <T> Specification<T> listSpec(List<?> values, PredicateSupplier<T> supplier) {
    return (root, query, cb) ->
        Optional.ofNullable(values)
            .filter(v -> !v.isEmpty())
            .map(v -> supplier.supply(root, cb))
            .orElse(null);
  }

  public static <T> Specification<T> distinct() {
    return (root, query, cb) -> {
      query.distinct(true);
      return null;
    };
  }
}

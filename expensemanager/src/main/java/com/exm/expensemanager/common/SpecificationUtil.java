package com.exm.expensemanager.common;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.BeanRegistry;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

public class SpecificationUtil {


    public static <T> Specification<T> containsStringIgnoreCase(String field, String value)
    {
        return (( (root, query, criteriaBuilder) ->
        {
            if(value == null) return null;
            return criteriaBuilder.like(
                            criteriaBuilder.upper(root.get(field)), "%" + value.toUpperCase() + "%");
        }));
    }

    public static <T, E> Specification<E> equalsEnum(String field, T value)
    {
        return ((root, query, criteriaBuilder) -> {
            if (value == null) return null;

            return criteriaBuilder.equal(root.get(field), value);
        });
    }

    public static <T extends Comparable<T>, E> Specification<E> betweenTwo(String field, T minValue, T maxValue)
    {
        return ((root, query, criteriaBuilder) ->
        {
            if(maxValue != null && minValue != null) return criteriaBuilder.between(root.get(field), minValue, maxValue);
            if(maxValue != null) return criteriaBuilder.lessThanOrEqualTo(root.get(field), maxValue);
            if(minValue != null) return criteriaBuilder.greaterThanOrEqualTo(root.get(field), minValue);

            return null;
        });
    }

    public static <T, V> Specification<T> equals(String field, V value)
    {
        return (root, query, cb) -> {
            if (value == null) {
                return null;
            }

            if (field.contains(".")) {
                String[] fields = field.split("\\.");

                return cb.equal(
                        root.get(fields[0]).get(fields[1]),
                        value
                );
            }

            return cb.equal(root.get(field), value);
        };
    };
}

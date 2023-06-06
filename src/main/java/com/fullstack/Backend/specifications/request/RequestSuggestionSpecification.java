package com.fullstack.Backend.specifications.request;

import com.fullstack.Backend.entities.Request;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import static com.fullstack.Backend.constant.constant.*;

@Data
@NoArgsConstructor
public class RequestSuggestionSpecification implements Specification<Request> {
    public Specification<Request> outputSuggestion(int fieldColumn, String keyword) {
        return new Specification<Request>() {

            @Override
            public Predicate toPredicate(Root<Request> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (keyword != null) {
                    switch (fieldColumn) {
                        case REQUEST_REQUEST_ID_COLUMN:
                            return criteriaBuilder.like(root.<String>get("request_id"), "%" + keyword + "%");
                        case REQUEST_DEVICE_NAME_COLUMN:
                            return criteriaBuilder.like(root.<String>get("device").get("name"), "%" + keyword + "%");
                        case REQUEST_REQUESTER_COLUMN:
                            return criteriaBuilder.like(root.<String>get("requester").get("userName"), "%" + keyword + "%");
                        case REQUEST_CURRENT_KEEPER_COLUMN:
                            return criteriaBuilder.like(root.<String>get("currentKeeper").get("userName"), "%" + keyword + "%");
                        case REQUEST_NEXT_KEEPER_COLUMN:
                            return criteriaBuilder.like(root.<String>get("nextKeeper").get("userName"), "%" + keyword + "%");
                    }
                }
                return null;
            }
        };
    }

    @Override
    public Predicate toPredicate(Root<Request> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return null;
    }
}

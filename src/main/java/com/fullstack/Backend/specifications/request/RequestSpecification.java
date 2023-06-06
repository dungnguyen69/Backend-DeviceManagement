package com.fullstack.Backend.specifications.request;

import com.fullstack.Backend.dto.device.DeviceFilterDTO;
import com.fullstack.Backend.dto.request.RequestFilterDTO;
import com.fullstack.Backend.entities.Request;
import com.fullstack.Backend.enums.Origin;
import com.fullstack.Backend.enums.Project;
import com.fullstack.Backend.enums.RequestStatus;
import com.fullstack.Backend.enums.Status;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RequestSpecification implements Specification<Request> {
    private final RequestFilterDTO criteria;

    public RequestSpecification(RequestFilterDTO criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Request> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        final List<Predicate> predicates = new ArrayList<>();
        if (criteria.getRequestId() != null) {
            final Predicate requestId = builder.equal(builder.lower(root.<String>get("request_id")), criteria.getRequestId());
            predicates.add(requestId);
        }
        if (criteria.getRequester() != null) {
            final Predicate requester = builder.equal(root.<String>get("requester").get("userName"), criteria.getRequester());
            predicates.add(requester);
        }
        if (criteria.getCurrentKeeper() != null) {
            final Predicate currentKeeper = builder.equal(builder.lower(root.<String>get("currentKeeper").get("userName")),
                    criteria.getCurrentKeeper());
            predicates.add(currentKeeper);
        }
        if (criteria.getNextKeeper() != null) {
            final Predicate nextKeeper = builder.equal(builder.lower(root.<String>get("nextKeeper").get("userName")),
                    criteria.getNextKeeper());
            predicates.add(nextKeeper);
        }
        if (criteria.getDevice() != null) {
            final Predicate name = builder.equal(builder.lower(root.<String>get("device").get("name")),
                    criteria.getDevice());
            predicates.add(name);
        }
        if (criteria.getRequestStatus() != null) {
            final Predicate status = builder.equal(root.get("requestStatus"), RequestStatus.valueOf(criteria.getRequestStatus()));
            predicates.add(status);
        }
        if (criteria.getBookingDate() != null) {
            final Predicate bookingDate = builder.greaterThanOrEqualTo(root.<Date>get("booking_date"), criteria.getBookingDate());
            predicates.add(bookingDate);
        }
        if (criteria.getReturnDate() != null) {
            final Predicate returnDate = builder.lessThanOrEqualTo(root.<Date>get("return_date"), criteria.getReturnDate());
            predicates.add(returnDate);
        }

        return null;
    }
}

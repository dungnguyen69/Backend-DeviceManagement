package com.fullstack.Backend.specifications.device;

import static com.fullstack.Backend.constant.constant.DEVICE_INVENTORY_NUMBER_COLUMN;
import static com.fullstack.Backend.constant.constant.DEVICE_NAME_COLUMN;
import static com.fullstack.Backend.constant.constant.DEVICE_OWNER_COLUMN;
import static com.fullstack.Backend.constant.constant.DEVICE_PLATFORM_NAME_COLUMN;
import static com.fullstack.Backend.constant.constant.DEVICE_PLATFORM_VERSION_COLUMN;
import static com.fullstack.Backend.constant.constant.DEVICE_RAM_COLUMN;
import static com.fullstack.Backend.constant.constant.DEVICE_SCREEN_COLUMN;
import static com.fullstack.Backend.constant.constant.DEVICE_SERIAL_NUMBER_COLUMN;
import static com.fullstack.Backend.constant.constant.DEVICE_STORAGE_COLUMN;

import org.springframework.data.jpa.domain.Specification;

import com.fullstack.Backend.entities.Device;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@SuppressWarnings("serial")
public class DeviceSuggestionSpecification implements Specification<Device> {
	
	public Specification<Device> outputSuggestion(int fieldColumn, String keyword) {
		return new Specification<Device>() {
			@Override
			public Predicate toPredicate(Root<Device> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				switch (fieldColumn) {
				case DEVICE_NAME_COLUMN:
					if (keyword != null)
						return criteriaBuilder.like(root.<String>get("name"), "%" + keyword + "%");
					break;
				case DEVICE_PLATFORM_NAME_COLUMN:
					if (keyword != null)
						return criteriaBuilder.like(root.<String>get("platform").get("name"), "%" + keyword + "%");
					break;
				case DEVICE_PLATFORM_VERSION_COLUMN:
					if (keyword != null)
						return criteriaBuilder.like(root.<String>get("platform").get("version"), "%" + keyword + "%");
					break;
				case DEVICE_RAM_COLUMN:
					if (keyword != null)
						return criteriaBuilder.like(root.<String>get("ram").get("size"), "%" + keyword + "%");
					break;
				case DEVICE_SCREEN_COLUMN:
					if (keyword != null)
						return criteriaBuilder.like(root.<String>get("screen").get("size"), "%" + keyword + "%");
					break;
				case DEVICE_STORAGE_COLUMN:
					if (keyword != null)
						return criteriaBuilder.like(root.<String>get("storage").get("size"), "%" + keyword + "%");
					break;
				case DEVICE_OWNER_COLUMN:
					if (keyword != null)
						return criteriaBuilder.like(root.<String>get("owner").get("userName"), "%" + keyword + "%");
					break;
				case DEVICE_INVENTORY_NUMBER_COLUMN:
					if (keyword != null)
						return criteriaBuilder.like(root.<String>get("inventoryNumber"), "%" + keyword + "%");
					break;
				case DEVICE_SERIAL_NUMBER_COLUMN:
					if (keyword != null)
						return criteriaBuilder.like(root.<String>get("serialNumber"), "%" + keyword + "%");
					break;
				}
				return null;
			}
		};
	}

	@Override
	public Predicate toPredicate(Root<Device> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		// TODO Auto-generated method stub
		return null;
	}

}

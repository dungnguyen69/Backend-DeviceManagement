package com.fullstack.Backend.specifications;

import static com.fullstack.Backend.constant.constant.DEVICE_INVENTORY_NUMBER_COLUMN;
import static com.fullstack.Backend.constant.constant.DEVICE_NAME_COLUMN;
import static com.fullstack.Backend.constant.constant.DEVICE_OWNER_COLUMN;
import static com.fullstack.Backend.constant.constant.DEVICE_PLATFORM_NAME_COLUMN;
import static com.fullstack.Backend.constant.constant.DEVICE_PLATFORM_VERSION_COLUMN;
import static com.fullstack.Backend.constant.constant.DEVICE_RAM_COLUMN;
import static com.fullstack.Backend.constant.constant.DEVICE_SCREEN_COLUMN;
import static com.fullstack.Backend.constant.constant.DEVICE_SERIAL_NUMBER_COLUMN;
import static com.fullstack.Backend.constant.constant.DEVICE_STORAGE_COLUMN;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;

import com.fullstack.Backend.dto.device.DeviceFilterDTO;
import com.fullstack.Backend.entities.Device;
import com.fullstack.Backend.enums.Origin;
import com.fullstack.Backend.enums.Project;
import com.fullstack.Backend.enums.Status;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@SuppressWarnings("serial")
public class DeviceSpecification implements Specification<Device> {
	private final DeviceFilterDTO criteria;

	public DeviceSpecification(DeviceFilterDTO criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<Device> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		final List<Predicate> predicates = new ArrayList<>();
		if (criteria.getName() != null) {
			final Predicate name = builder.equal(builder.lower(root.<String>get("name")), criteria.getName());
			predicates.add(name);
		}
		if (criteria.getStatus() != null) {
			final Predicate status = builder.equal(root.get("status"), Status.valueOf(criteria.getStatus()));
			predicates.add(status);
		}
		if (criteria.getPlatformName() != null) {
			final Predicate platform = builder.equal(builder.lower(root.<String>get("platform").get("name")),
					criteria.getPlatformName());
			predicates.add(platform);
		}
		if (criteria.getPlatformVersion() != null) {
			final Predicate platform = builder.equal(builder.lower(root.<String>get("platform").get("version")),
					criteria.getPlatformVersion());
			predicates.add(platform);
		}
		if (criteria.getItemType() != null) {
			final Predicate name = builder.equal(builder.lower(root.<String>get("itemType").get("name")),
					criteria.getItemType());
			predicates.add(name);
		}
		if (criteria.getRam() != null) {
			final Predicate name = builder.equal(builder.lower(root.<String>get("ram").get("size")), criteria.getRam());
			predicates.add(name);
		}
		if (criteria.getScreen() != null) {
			final Predicate name = builder.equal(builder.lower(root.<String>get("screen").get("size")),
					criteria.getScreen());
			predicates.add(name);
		}
		if (criteria.getStorage() != null) {
			final Predicate name = builder.equal(builder.lower(root.<String>get("storage").get("size")),
					criteria.getStorage());
			predicates.add(name);
		}
		if (criteria.getOwner() != null) {
			final Predicate name = builder.equal(builder.lower(root.<String>get("owner").get("userName")),
					criteria.getOwner());
			predicates.add(name);
		}
		if (criteria.getOrigin() != null) {
			final Predicate name = builder.equal(root.<String>get("origin"), Origin.valueOf(criteria.getOrigin()));
			predicates.add(name);
		}
		if (criteria.getInventoryNumber() != null) {
			final Predicate name = builder.equal(builder.lower(root.<String>get("inventoryNumber")),
					criteria.getInventoryNumber());
			predicates.add(name);
		}
		if (criteria.getSerialNumber() != null) {
			final Predicate name = builder.equal(builder.lower(root.<String>get("serialNumber")),
					criteria.getSerialNumber());
			predicates.add(name);
		}
		if (criteria.getProject() != null) {
			final Predicate name = builder.equal(root.<String>get("project"), Project.valueOf(criteria.getProject()));
			predicates.add(name);
		}
		return builder.and(predicates.toArray(new Predicate[predicates.size()]));
	}

}

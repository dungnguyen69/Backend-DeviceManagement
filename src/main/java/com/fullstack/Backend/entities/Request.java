package com.fullstack.Backend.entities;

import java.util.Date;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Requests")
public class Request extends BaseEntity{
	@Column(nullable = false)
	private String requestId;

	@ManyToOne
	@JoinColumn(name = "requester_Id", nullable = false)
	private User requester;
	
	@ManyToOne
	@JoinColumn(name = "currentKeeper_Id", nullable = false)
	private User currentKeeperRequest;
	
	@ManyToOne
	@JoinColumn(name = "nextKeeper_Id", nullable = false)
	private User nextKeeperRequest;
	
	@ManyToOne
	@JoinColumn(name = "device_Id", nullable = false)
	private Device  devices;
	
	@Column()
	private Date approvalDate;
	
	@Column()
	private Date transferredDate;
	
	@Column()
	private Date returnedDate;
	
	@Column()
	private Date cancelledDate;
}

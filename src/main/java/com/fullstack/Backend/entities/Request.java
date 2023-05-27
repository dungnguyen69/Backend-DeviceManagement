package com.fullstack.Backend.entities;

import java.util.Date;

import com.fullstack.Backend.enums.RequestStatus;

import jakarta.persistence.ForeignKey;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
	@JoinColumn(name = "requester_Id", nullable = false, foreignKey = @ForeignKey(name = "requester_Id_FK"))
	private User requester;
	
	@ManyToOne
	@JoinColumn(name = "currentKeeper_Id", nullable = false, foreignKey = @ForeignKey(name = "currentKeeper_Id_FK"))
	private User currentKeeperRequest;
	
	@ManyToOne
	@JoinColumn(name = "nextKeeper_Id", nullable = false, foreignKey = @ForeignKey(name = "nextKeeper_Id_FK"))
	private User nextKeeperRequest;
	
	@ManyToOne
	@JoinColumn(name = "device_Id", nullable = false, foreignKey = @ForeignKey(name = "device_Id_FK"))
	private Device  devices;

	@Column(nullable = false)
	@Enumerated(EnumType.ORDINAL)
	private RequestStatus requestStatus ;
	
	@Column()
	private Date approvalDate;
	
	@Column()
	private Date transferredDate;
	
	@Column()
	private Date returnedDate;
	
	@Column()
	private Date cancelledDate;
}

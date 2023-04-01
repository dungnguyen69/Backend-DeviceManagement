package devices;

public class DeviceDTO {
	private int Id;
	private int statusId;
	private int itemTypeId;
	private int platFormId;
	private int screenId;
	private int ramId;
	private int storageId;
	private int ownerId;
	private int originId;
	private int projectId;
	private String serialNumber;
	private String inventoryNumber;
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	public int getItemTypeId() {
		return itemTypeId;
	}
	public void setItemTypeId(int itemTypeId) {
		this.itemTypeId = itemTypeId;
	}
	public int getPlatFormId() {
		return platFormId;
	}
	public void setPlatFormId(int platFormId) {
		this.platFormId = platFormId;
	}
	public int getScreenId() {
		return screenId;
	}
	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}
	public int getRamId() {
		return ramId;
	}
	public void setRamId(int ramId) {
		this.ramId = ramId;
	}
	public int getStorageId() {
		return storageId;
	}
	public void setStorageId(int storageId) {
		this.storageId = storageId;
	}
	public int getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	public int getOriginId() {
		return originId;
	}
	public void setOriginId(int originId) {
		this.originId = originId;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getInventoryNumber() {
		return inventoryNumber;
	}
	public void setInventoryNumber(String inventoryNumber) {
		this.inventoryNumber = inventoryNumber;
	}
	
}

CREATE TABLE devices (
  id INT AUTO_INCREMENT NOT NULL,
   created_date datetime NOT NULL,
   updated_date datetime NULL,
   name VARCHAR(255) NOT NULL,
   status INT NOT NULL,
   platform_id INT NULL,
   item_type_id INT NULL,
   ram_id INT NULL,
   screen_id INT NULL,
   storage_id INT NULL,
   owner_id INT NULL,
   inventory_number VARCHAR(255) NOT NULL,
   serial_number VARCHAR(255) NOT NULL,
   origin INT NOT NULL,
   project INT NOT NULL,
   comments VARCHAR(255) NULL,
   CONSTRAINT pk_devices PRIMARY KEY (id)
);

CREATE TABLE item_types (
  id INT AUTO_INCREMENT NOT NULL,
   created_date datetime NOT NULL,
   updated_date datetime NULL,
   name VARCHAR(255) NOT NULL,
   CONSTRAINT pk_item_types PRIMARY KEY (id)
);
CREATE TABLE permissions (
  id INT AUTO_INCREMENT NOT NULL,
   created_date datetime NOT NULL,
   updated_date datetime NULL,
   privilege VARCHAR(255) NOT NULL,
   CONSTRAINT pk_permissions PRIMARY KEY (id)
);
CREATE TABLE rams (
  id INT AUTO_INCREMENT NOT NULL,
   created_date datetime NOT NULL,
   updated_date datetime NULL,
   size BIGINT NOT NULL,
   CONSTRAINT pk_rams PRIMARY KEY (id)
);
CREATE TABLE platform (
  id INT AUTO_INCREMENT NOT NULL,
   created_date datetime NOT NULL,
   updated_date datetime NULL,
   name VARCHAR(255) NOT NULL,
   version VARCHAR(255) NOT NULL,
   CONSTRAINT pk_platform PRIMARY KEY (id)
);
CREATE TABLE requests (
  id INT AUTO_INCREMENT NOT NULL,
   created_date datetime NOT NULL,
   updated_date datetime NULL,
   request_id VARCHAR(255) NOT NULL,
   requester_id INT NOT NULL,
   current_keeper_id INT NOT NULL,
   next_keeper_id INT NOT NULL,
   device_id INT NOT NULL,
   request_status INT NOT NULL,
   approval_date datetime NULL,
   transferred_date datetime NULL,
   returned_date datetime NULL,
   cancelled_date datetime NULL,
   CONSTRAINT pk_requests PRIMARY KEY (id)
);
CREATE TABLE screens (
  id INT AUTO_INCREMENT NOT NULL,
   created_date datetime NOT NULL,
   updated_date datetime NULL,
   size BIGINT NOT NULL,
   CONSTRAINT pk_screens PRIMARY KEY (id)
);
CREATE TABLE system_role_permission (
  id INT AUTO_INCREMENT NOT NULL,
   system_role_id INT NULL,
   permission_id INT NULL,
   CONSTRAINT pk_systemrolepermission PRIMARY KEY (id)
);

CREATE TABLE system_roles (
  id INT AUTO_INCREMENT NOT NULL,
   created_date datetime NOT NULL,
   updated_date datetime NULL,
   name VARCHAR(255) NOT NULL,
   CONSTRAINT pk_systemroles PRIMARY KEY (id)
);
CREATE TABLE users (
  id INT AUTO_INCREMENT NOT NULL,
   created_date datetime NOT NULL,
   updated_date datetime NULL,
   badge_id VARCHAR(255) NOT NULL,
   user_name VARCHAR(255) NOT NULL,
   password VARCHAR(255) NOT NULL,
   first_name VARCHAR(255) NOT NULL,
   last_name VARCHAR(255) NOT NULL,
   email VARCHAR(255) NOT NULL,
   phone_number VARCHAR(255) NOT NULL,
   project VARCHAR(255) NOT NULL,
   system_roles_id INT NOT NULL,
   CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users ADD CONSTRAINT SYSTEMROLES_ID_FK FOREIGN KEY (system_roles_id) REFERENCES system_roles (id);
ALTER TABLE system_role_permission ADD CONSTRAINT PERMISSION_ID_FK FOREIGN KEY (permission_id) REFERENCES permissions (id);

ALTER TABLE system_role_permission ADD CONSTRAINT SYSTEMROLE_ID_FK FOREIGN KEY (system_role_id) REFERENCES system_roles (id);
CREATE TABLE storages (
  id INT AUTO_INCREMENT NOT NULL,
   created_date datetime NOT NULL,
   updated_date datetime NULL,
   size BIGINT NOT NULL,
   CONSTRAINT pk_storages PRIMARY KEY (id)
);
ALTER TABLE requests ADD CONSTRAINT CURRENTKEEPER_ID_FK FOREIGN KEY (current_keeper_id) REFERENCES users (id);

ALTER TABLE requests ADD CONSTRAINT DEVICE_ID_FK FOREIGN KEY (device_id) REFERENCES devices (id);

ALTER TABLE requests ADD CONSTRAINT NEXTKEEPER_ID_FK FOREIGN KEY (next_keeper_id) REFERENCES users (id);

ALTER TABLE requests ADD CONSTRAINT REQUESTER_ID_FK FOREIGN KEY (requester_id) REFERENCES users (id);
ALTER TABLE devices ADD CONSTRAINT serialNumber UNIQUE (serial_number);

ALTER TABLE devices ADD CONSTRAINT ITEM_TYPE_ID_FK FOREIGN KEY (item_type_id) REFERENCES item_types (id);

ALTER TABLE devices ADD CONSTRAINT OWNER_ID_FK FOREIGN KEY (owner_id) REFERENCES users (id);

ALTER TABLE devices ADD CONSTRAINT PLATFORM_ID_FK FOREIGN KEY (platform_id) REFERENCES platform (id);

ALTER TABLE devices ADD CONSTRAINT RAM_ID_FK FOREIGN KEY (ram_id) REFERENCES rams (id);

ALTER TABLE devices ADD CONSTRAINT SCREEN_ID_FK FOREIGN KEY (screen_id) REFERENCES screens (id);

ALTER TABLE devices ADD CONSTRAINT STORAGE_ID_FK FOREIGN KEY (storage_id) REFERENCES storages (id);


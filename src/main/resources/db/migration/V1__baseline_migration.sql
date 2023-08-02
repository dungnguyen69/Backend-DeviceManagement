CREATE TABLE IF NOT EXISTS devices (
  id INT AUTO_INCREMENT NOT NULL,
   created_date datetime NOT NULL,
   updated_date datetime NULL,
   comments VARCHAR(255) NULL,
   inventory_number VARCHAR(255) NOT NULL,
   item_type_id INT NOT NULL,
   name VARCHAR(255) NOT NULL,
   origin SMALLINT NOT NULL,
   owner_id INT NOT NULL,
   platform_id INT NOT NULL,
   project SMALLINT NOT NULL,
   ram_id INT NOT NULL,
   screen_id INT NOT NULL,
   serial_number VARCHAR(255) NOT NULL,
   status SMALLINT NOT NULL,
   storage_id INT NOT NULL,
   booking_date datetime NULL,
   return_date datetime NULL,
   CONSTRAINT PK_DEVICES PRIMARY KEY (id),
   UNIQUE (serial_number)
);

CREATE TABLE IF NOT EXISTS flyway_schema_history (
  installed_rank INT NOT NULL,
   version INT NULL,
   `description` TEXT NULL,
   type TEXT NULL,
   script TEXT NULL,
   checksum TEXT NULL,
   installed_by TEXT NULL,
   installed_on TEXT NULL,
   execution_time INT NULL,
   success INT NULL,
   CONSTRAINT PK_FLYWAY_SCHEMA_HISTORY PRIMARY KEY (installed_rank)
);

CREATE TABLE IF NOT EXISTS item_types (
  id INT AUTO_INCREMENT NOT NULL,
   created_date datetime NOT NULL,
   updated_date datetime NULL,
   name VARCHAR(255) NOT NULL,
   CONSTRAINT PK_ITEM_TYPES PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS keeper_order (
  id INT AUTO_INCREMENT NOT NULL,
   created_date datetime NOT NULL,
   updated_date datetime NULL,
   device_id INT NOT NULL,
   keeper_id INT NOT NULL,
   keeper_no INT NOT NULL,
   is_returned BIT(1) NOT NULL,
   booking_date datetime NOT NULL,
   due_date datetime NOT NULL,
   CONSTRAINT PK_KEEPER_ORDER PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS password_reset_token (
  id BIGINT AUTO_INCREMENT NOT NULL,
   token VARCHAR(255) NULL,
   user_id INT NOT NULL,
   expiry_date datetime NULL,
   CONSTRAINT PK_PASSWORD_RESET_TOKEN PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS permissions (
  id INT AUTO_INCREMENT NOT NULL,
   created_date datetime NOT NULL,
   updated_date datetime NULL,
   privilege VARCHAR(255) NOT NULL,
   CONSTRAINT PK_PERMISSIONS PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS platform (
  id INT AUTO_INCREMENT NOT NULL,
   created_date datetime NOT NULL,
   updated_date datetime NULL,
   name VARCHAR(255) NOT NULL,
   version VARCHAR(255) NOT NULL,
   CONSTRAINT PK_PLATFORM PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS rams (
  id INT AUTO_INCREMENT NOT NULL,
   created_date datetime NOT NULL,
   updated_date datetime NULL,
   size VARCHAR(255) NOT NULL,
   CONSTRAINT PK_RAMS PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS requests (
  id INT AUTO_INCREMENT NOT NULL,
   created_date datetime NOT NULL,
   updated_date datetime NULL,
   approval_date datetime NULL,
   request_id VARCHAR(255) NOT NULL,
   transferred_date datetime NULL,
   current_keeper_id INT NOT NULL,
   device_id INT NOT NULL,
   next_keeper_id INT NOT NULL,
   requester_id INT NOT NULL,
   request_status SMALLINT NOT NULL,
   booking_date datetime NOT NULL,
   return_date datetime NOT NULL,
   accepter_id INT NOT NULL,
   cancelled_date datetime NULL,
   CONSTRAINT PK_REQUESTS PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS screens (
  id INT AUTO_INCREMENT NOT NULL,
   created_date datetime NOT NULL,
   updated_date datetime NULL,
   size VARCHAR(255) NOT NULL,
   CONSTRAINT PK_SCREENS PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS storages (
  id INT AUTO_INCREMENT NOT NULL,
   created_date datetime NOT NULL,
   updated_date datetime NULL,
   size VARCHAR(255) NOT NULL,
   CONSTRAINT PK_STORAGES PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS system_role_permission (
  id INT AUTO_INCREMENT NOT NULL,
   permission_id INT NULL,
   system_role_id INT NULL,
   CONSTRAINT PK_SYSTEM_ROLE_PERMISSION PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS system_roles (
  id INT AUTO_INCREMENT NOT NULL,
   created_date datetime NOT NULL,
   updated_date datetime NULL,
   name VARCHAR(20) NOT NULL,
   CONSTRAINT PK_SYSTEM_ROLES PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users (
  id INT AUTO_INCREMENT NOT NULL,
   created_date datetime NOT NULL,
   updated_date datetime NULL,
   badge_id VARCHAR(255) NOT NULL,
   email VARCHAR(50) NOT NULL,
   first_name VARCHAR(20) NOT NULL,
   last_name VARCHAR(20) NOT NULL,
   password VARCHAR(255) NOT NULL,
   phone_number VARCHAR(255) NOT NULL,
   project VARCHAR(255) NULL,
   user_name VARCHAR(20) NOT NULL,
   enabled BIT(1) NOT NULL,
   CONSTRAINT PK_USERS PRIMARY KEY (id),
   UNIQUE (email),
   UNIQUE (user_name)
);

CREATE TABLE IF NOT EXISTS users_system_roles (
  user_id INT NOT NULL,
   system_roles_id INT NOT NULL,
   CONSTRAINT PK_USERS_SYSTEM_ROLES PRIMARY KEY (user_id, system_roles_id)
);

CREATE TABLE IF NOT EXISTS verification_token (
  id BIGINT AUTO_INCREMENT NOT NULL,
   token VARCHAR(255) NULL,
   user_id INT NOT NULL,
   expiry_date datetime NULL,
   CONSTRAINT PK_VERIFICATION_TOKEN PRIMARY KEY (id)
);

CREATE INDEX ACCEPTER_ID_FK ON requests(accepter_id);

CREATE INDEX DEVICE_ID_FKSSl0LN ON keeper_order(device_id);

CREATE INDEX FK_PASSWORDRESETTOKEN_ON_USER ON password_reset_token(user_id);

CREATE INDEX FK_VERIFICATIONTOKEN_ON_USER ON verification_token(user_id);

CREATE INDEX KEEPER_ID_FK ON keeper_order(keeper_id);

CREATE INDEX currentKeeper_Id_FK ON requests(current_keeper_id);

CREATE INDEX device_Id_FK ON requests(device_id);

CREATE INDEX item_type_Id_FK ON devices(item_type_id);

CREATE INDEX nextKeeper_Id_FK ON requests(next_keeper_id);

CREATE INDEX owner_Id_FK ON devices(owner_id);

CREATE INDEX permission_Id_FK ON system_role_permission(permission_id);

CREATE INDEX platform_Id_FK ON devices(platform_id);

CREATE INDEX ram_Id_FK ON devices(ram_id);

CREATE INDEX requester_Id_FK ON requests(requester_id);

CREATE INDEX screen_Id_FK ON devices(screen_id);

CREATE INDEX storage_Id_FK ON devices(storage_id);

CREATE INDEX systemRoles_Id_FKQMkWSK ON users_system_roles(system_roles_id);

ALTER TABLE requests ADD CONSTRAINT ACCEPTER_ID_FK FOREIGN KEY (accepter_id) REFERENCES users (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE keeper_order ADD CONSTRAINT DEVICE_ID_FKSSl0LN FOREIGN KEY (device_id) REFERENCES devices (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE password_reset_token ADD CONSTRAINT FK_PASSWORDRESETTOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE verification_token ADD CONSTRAINT FK_VERIFICATIONTOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE keeper_order ADD CONSTRAINT KEEPER_ID_FK FOREIGN KEY (keeper_id) REFERENCES users (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE requests ADD CONSTRAINT currentKeeper_Id_FK FOREIGN KEY (current_keeper_id) REFERENCES users (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE requests ADD CONSTRAINT device_Id_FK FOREIGN KEY (device_id) REFERENCES devices (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE devices ADD CONSTRAINT item_type_Id_FK FOREIGN KEY (item_type_id) REFERENCES item_types (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE requests ADD CONSTRAINT nextKeeper_Id_FK FOREIGN KEY (next_keeper_id) REFERENCES users (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE devices ADD CONSTRAINT owner_Id_FK FOREIGN KEY (owner_id) REFERENCES users (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE system_role_permission ADD CONSTRAINT permission_Id_FK FOREIGN KEY (permission_id) REFERENCES permissions (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE devices ADD CONSTRAINT platform_Id_FK FOREIGN KEY (platform_id) REFERENCES platform (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE devices ADD CONSTRAINT ram_Id_FK FOREIGN KEY (ram_id) REFERENCES rams (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE requests ADD CONSTRAINT requester_Id_FK FOREIGN KEY (requester_id) REFERENCES users (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE devices ADD CONSTRAINT screen_Id_FK FOREIGN KEY (screen_id) REFERENCES screens (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE devices ADD CONSTRAINT storage_Id_FK FOREIGN KEY (storage_id) REFERENCES storages (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE users_system_roles ADD CONSTRAINT systemRoles_Id_FK FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE users_system_roles ADD CONSTRAINT systemRoles_Id_FKQMkWSK FOREIGN KEY (system_roles_id) REFERENCES system_roles (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

insert into item_types values(1,CURDATE(),null,'N/A');
REPLACE into item_types values(2,CURDATE(),null,'Laptop');
REPLACE into item_types values(3,CURDATE(),null,'Desktop');
REPLACE into item_types values(4,CURDATE(),null,'Mobile');
REPLACE into item_types values(5,CURDATE(),null,'Tablet');

REPLACE into permissions values(1,CURDATE(),NULL,'Create');
REPLACE into permissions values(2,CURDATE(),NULL,'Update');
REPLACE into permissions values(3,CURDATE(),NULL,'Delete');
REPLACE into permissions values(4,CURDATE(),NULL,'View');
REPLACE into permissions values(5,CURDATE(),NULL,'Import');
REPLACE into permissions values(6,CURDATE(),NULL,'Export');

REPLACE into platform values(1,CURDATE(),NULL,'N/A','N/A');
REPLACE into platform values(2,CURDATE(),NULL,'Android','7.5');
REPLACE into platform values(3,CURDATE(),NULL,'Android','13.2');
REPLACE into platform values(4,CURDATE(),NULL,'Android','11.1');
REPLACE into platform values(5,CURDATE(),NULL,'Android','10.1');
REPLACE into platform values(6,CURDATE(),NULL,'Android','10.2');
REPLACE into platform values(7,CURDATE(),NULL,'iOs','10');
REPLACE into platform values(8,CURDATE(),NULL,'iOs','8.2');
REPLACE into platform values(9,CURDATE(),NULL,'iOs','12.1');
REPLACE into platform values(10,CURDATE(),NULL,'iOs','15.1');

REPLACE into rams values(1,CURDATE(),NULL,3);
REPLACE into rams values(2,CURDATE(),NULL,4);
REPLACE into rams values(3,CURDATE(),NULL,16);
REPLACE into rams values(4,CURDATE(),NULL,32);
REPLACE into rams values(5,CURDATE(),NULL,8);
REPLACE into rams values(6,CURDATE(),NULL,64);
REPLACE into rams values(7,CURDATE(),NULL,128);
REPLACE into rams values(8,CURDATE(),NULL,256);

REPLACE into storages values(1,CURDATE(),NULL,164);
REPLACE into storages values(2,CURDATE(),NULL,256);
REPLACE into storages values(3,CURDATE(),NULL,300);
REPLACE into storages values(4,CURDATE(),NULL,512);
REPLACE into storages values(5,CURDATE(),NULL,128);
REPLACE into storages values(6,CURDATE(),NULL,1024);


REPLACE into screens values(1,CURDATE(),NULL,17);
REPLACE into screens values(2,CURDATE(),NULL,13);
REPLACE into screens values(3,CURDATE(),NULL,14);
REPLACE into screens values(4,CURDATE(),NULL,12);
REPLACE into screens values(5,CURDATE(),NULL,16);
REPLACE into screens values(6,CURDATE(),NULL,18);
REPLACE into screens values(7,CURDATE(),NULL,20);

INSERT INTO system_role_permission VALUES(1, 1, 1);
INSERT INTO system_role_permission VALUES(2, 2, 1);
INSERT INTO system_role_permission VALUES(3, 3, 1);
INSERT INTO system_role_permission VALUES(4, 4, 1);
INSERT INTO system_role_permission VALUES(5, 5, 1);
INSERT INTO system_role_permission VALUES(6, 6, 1);

INSERT INTO system_roles VALUES(1, CURDATE(), CURDATE(), 'ROLE_ADMIN');
INSERT INTO system_roles VALUES(2, CURDATE(), CURDATE(), 'ROLE_MODERATOR');
INSERT INTO system_roles VALUES(3, CURDATE(), CURDATE(), 'ROLE_USER');


INSERT
INTO
  users
  (created_date, badge_id, user_name, password, first_name, last_name, email, phone_number, enabled)
VALUES
  ('2023-07-17 13:10:59.162', '608616-K2SXZ', 'admin', '$2a$10$bo2W2yRKbIw/Lwgq1Cj1Ae3zTWxpHd2fwEgoyW03fcvIAFFC3ZZIC', 'Dung', 'Admin', 'admin@gmail.com', '123456789', 1);

INSERT INTO users_system_roles (user_id,system_roles_id)
SELECT id,1 FROM users
WHERE user_name='admin';


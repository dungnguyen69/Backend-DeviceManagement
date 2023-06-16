CREATE TABLE users_system_roles (
  user_id INT NOT NULL,
   system_roles_id INT NOT NULL,
   CONSTRAINT pk_users_systemroles PRIMARY KEY (user_id, system_roles_id)
);

ALTER TABLE users ADD enabled BIT(1) NULL;

ALTER TABLE users ADD verification_code VARCHAR(64) NULL;

ALTER TABLE users ADD CONSTRAINT uc_43e7f1bfa8e81e4a7da1800c3 UNIQUE (user_name);

ALTER TABLE users ADD CONSTRAINT uc_b3d33bf4b63b1c7a335ee4201 UNIQUE (email);

ALTER TABLE users_system_roles ADD CONSTRAINT systemRoles_Id_FK FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE users_system_roles ADD CONSTRAINT systemRoles_Id_FKQMkWSK FOREIGN KEY (system_roles_id) REFERENCES system_roles (id);

ALTER TABLE users MODIFY email VARCHAR(50);

ALTER TABLE users MODIFY first_name VARCHAR(20);

ALTER TABLE users MODIFY last_name VARCHAR(20);

ALTER TABLE system_roles MODIFY name VARCHAR(20);

ALTER TABLE users MODIFY user_name VARCHAR(20);
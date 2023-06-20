CREATE TABLE password_reset_token (
  id BIGINT AUTO_INCREMENT NOT NULL,
   token VARCHAR(255) NULL,
   user_id INT NOT NULL,
   expiry_date datetime NULL,
   CONSTRAINT pk_passwordresettoken PRIMARY KEY (id)
);

ALTER TABLE password_reset_token ADD CONSTRAINT FK_PASSWORDRESETTOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);
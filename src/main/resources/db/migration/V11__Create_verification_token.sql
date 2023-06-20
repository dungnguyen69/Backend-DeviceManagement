CREATE TABLE verification_token (
  id BIGINT NOT NULL,
   token VARCHAR(255) NULL,
   user_id INT NOT NULL,
   expiry_date datetime NULL,
   CONSTRAINT pk_verificationtoken PRIMARY KEY (id)
);

ALTER TABLE verification_token ADD CONSTRAINT FK_VERIFICATIONTOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);
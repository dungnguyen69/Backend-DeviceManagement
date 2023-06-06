ALTER TABLE requests ADD accepter_id INT NULL;

ALTER TABLE requests MODIFY accepter_id INT NOT NULL;

ALTER TABLE requests ADD CONSTRAINT ACCEPTER_ID_FK FOREIGN KEY (accepter_id) REFERENCES users (id);
CREATE TABLE keeper_order (
  id INT AUTO_INCREMENT NOT NULL,
   created_date datetime NOT NULL,
   updated_date datetime NULL,
   device_id INT NOT NULL,
   keeper_id INT NOT NULL,
   keeper_no INT NOT NULL,
   is_returned BIT(1) NOT NULL,
   booking_date datetime NOT NULL,
   due_date datetime NOT NULL,
   CONSTRAINT pk_keeper_order PRIMARY KEY (id)
);

ALTER TABLE keeper_order ADD CONSTRAINT DEVICE_ID_FKSSl0LN FOREIGN KEY (device_id) REFERENCES devices (id);

ALTER TABLE keeper_order ADD CONSTRAINT KEEPER_ID_FK FOREIGN KEY (keeper_id) REFERENCES users (id);

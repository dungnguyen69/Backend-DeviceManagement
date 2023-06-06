ALTER TABLE devices ADD booking_date datetime NULL;

ALTER TABLE devices ADD return_date datetime NULL;

ALTER TABLE requests ADD booking_date datetime NULL;

ALTER TABLE requests ADD return_date datetime NULL;

ALTER TABLE requests MODIFY booking_date datetime NOT NULL;

ALTER TABLE requests MODIFY return_date datetime NOT NULL;

ALTER TABLE requests DROP COLUMN returned_date;
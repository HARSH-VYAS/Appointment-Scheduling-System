DROP DATABASE IF EXISTS Appointment;
CREATE DATABASE Appointment;

CREATE TABLE `Appointment`.`doctor`(
`doc_id` INT NOT NULL AUTO_INCREMENT,
`name` VARCHAR(50) NOT NULL UNIQUE,
`ph_number` numeric NOT NULL UNIQUE,
PRIMARY KEY (`doc_id`));


CREATE TABLE `Appointment`.`patient`(
`pat_id` INT NOT NULL AUTO_INCREMENT,
`name` VARCHAR(50) NOT NULL UNIQUE,
`ph_number` numeric NOT NULL UNIQUE,
PRIMARY KEY (`pat_id`));

CREATE TABLE `Appointment`.`appointments` (
  `appointment_id` INT NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `time` time NOT NULL,
  `notes` varchar(600),
  `doc_id` INT NOT NULL,
  `pat_id` INT NOT NULL,
  PRIMARY KEY (`appointment_id`),
  constraint fk1 FOREIGN KEY(doc_id) references `Appointment`.`doctor`(doc_id),
  constraint fk2 FOREIGN KEY(pat_id) references `Appointment`.`patient`(pat_id));


INSERT INTO `Appointment`. `doctor` ( name, ph_number) VALUES ('James','6692547426');  
INSERT INTO `Appointment`. `doctor` ( name, ph_number) VALUES ('Jessey','6692547425');
INSERT INTO `Appointment`. `doctor`( name, ph_number) VALUES ('Steve','6692547421');
INSERT INTO `Appointment`. `doctor` ( name, ph_number) VALUES ('Harsh','6692547422');
INSERT INTO `Appointment`. `doctor` ( name, ph_number) VALUES ('Simple','6692547423');
INSERT INTO `Appointment`. `doctor` ( name, ph_number) VALUES ('Sahkira','6692547424');
INSERT INTO `Appointment`. `doctor` ( name, ph_number) VALUES ('Cynthia','6692547428');
INSERT INTO `Appointment`. `doctor` ( name, ph_number) VALUES ('broadly','6692547427');

INSERT INTO `Appointment`.`patient` ( name, ph_number) VALUES ('chan','6692547416');
INSERT INTO `Appointment`.`patient` ( name, ph_number) VALUES ('chu','6692547429');
INSERT INTO `Appointment`.`patient` ( name, ph_number) VALUES ('Sam','6692547436');
INSERT INTO `Appointment`.`patient` ( name, ph_number) VALUES ('Steven','6692547446');
INSERT INTO `Appointment`.`patient` ( name, ph_number) VALUES ('harsha','6692547456');
INSERT INTO `Appointment`.`patient` ( name, ph_number) VALUES ('Harshal','6692547466');
INSERT INTO `Appointment`.`patient` ( name, ph_number) VALUES ('adam','6692547476');
INSERT INTO `Appointment`.`patient` ( name, ph_number) VALUES ('adams','6692547486');
INSERT INTO `Appointment`.`patient` ( name, ph_number) VALUES ('Nathan','6692547496');


INSERT INTO `Appointment`.`appointments` ( date,time,notes,doc_id, pat_id) VALUES ('2016-11-09','2:00:00','Urgent',1,1);
INSERT INTO `Appointment`.`appointments` ( date,time,notes,doc_id, pat_id) VALUES ('2016-11-10','13:00:00','Last Appointment',1,2);
INSERT INTO `Appointment`.`appointments` ( date,time,notes,doc_id, pat_id) VALUES ('2016-11-09','12:00:00','Need a Serious attention',2,3);
INSERT INTO `Appointment`.`appointments` ( date,time,notes,doc_id, pat_id) VALUES ('2016-11-11','14:00:00','really important',2,5);
INSERT INTO `Appointment`.`appointments` ( date,time,notes,doc_id, pat_id) VALUES ('2016-11-12','8:00:00','Good to go',1,2);
INSERT INTO `Appointment`.`appointments` ( date,time,notes,doc_id, pat_id) VALUES ('2016-11-13','10:00:00','Keep it busy',3,4);
INSERT INTO `Appointment`.`appointments` ( date,time,notes,doc_id, pat_id) VALUES ('2016-11-28','11:00:00','Children!!! :)',6,1);
INSERT INTO `Appointment`.`appointments` ( date,time,notes,doc_id, pat_id) VALUES ('2016-11-22','12:00:00','Nice person',4,7);

DROP DATABASE IF EXISTS workflow;
CREATE DATABASE workflow;

CREATE TABLE `workflow`.`organization` (
  `org_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `address` varchar(600) NOT NULL,
  `admin_email` varchar(45) NOT NULL UNIQUE,
  `password` varchar(100) NOT NULL,
  PRIMARY KEY (`org_id`));

CREATE TABLE `workflow`.`role`(
`role_id` INT NOT NULL AUTO_INCREMENT,
`name` VARCHAR(50) NOT NULL UNIQUE,
PRIMARY KEY (`role_id`));

CREATE TABLE `workflow`.`department`(
`dept_id` INT NOT NULL AUTO_INCREMENT,
`name` VARCHAR(50) NOT NULL UNIQUE,
`org_id` INT,
PRIMARY KEY (`dept_id`),
constraint fk1 FOREIGN KEY(org_id) references `workflow`.`organization`(org_id));


CREATE TABLE `workflow`.`user` (
  `email_id` VARCHAR(45) NOT NULL UNIQUE,
  `org_id` INT NOT NULL AUTO_INCREMENT,
  `password` VARCHAR(100) NOT NULL,
  `address` VARCHAR(600) NOT NULL,
  `dept_id` INT,
  `role_id` INT,
PRIMARY KEY(`email_id`,`org_id`),
constraint fk2 FOREIGN KEY (org_id) references `workflow`.`organization`(org_id),
constraint fk3 FOREIGN KEY (role_id) references `workflow`.`role`(role_id),
constraint fk4 FOREIGN KEY (dept_id) references `workflow`.`department`(dept_id));

CREATE TABLE `workflow`.`level` (
  `level_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL UNIQUE,
  `description` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`level_id`));
  
CREATE TABLE `workflow`.`layer` (
  `layer_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL UNIQUE,
  `description` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`layer_id`));
  
CREATE TABLE `workflow`.`status` (
  `status_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `description` varchar(6000),
PRIMARY KEY(`status_id`));

CREATE TABLE `workflow`.`request_type` (
  `request_type_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `email_id` VARCHAR(45) NOT NULL,
  `org_id` INT NOT NULL,
UNIQUE(email_id, org_id, name),
constraint fk5 FOREIGN KEY (email_id,org_id) references `workflow`.`user`(email_id,org_id) ON DELETE CASCADE ON UPDATE CASCADE, 
PRIMARY KEY(`request_type_id`));

CREATE TABLE `workflow`.`workflow_master` (
  `workflow_id` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR (600) NOT NULL,
  `request_type_id` INT,
  `email_id` VARCHAR(45) NOT NULL,
  `org_id` INT NOT NULL,
  `dept_id` INT NOT NULL,
PRIMARY KEY(`workflow_id`),
constraint fk15 FOREIGN KEY (email_id,org_id) references `workflow`.`user`(email_id,org_id) ON DELETE CASCADE ON UPDATE CASCADE,
constraint fk16 FOREIGN KEY (dept_id) references `workflow`.`department`(dept_id),
constraint fk6 FOREIGN KEY (request_type_id) references `workflow`.`request_type`(request_type_id));

CREATE TABLE `workflow`.`workflowtbl` (
  `workflow_id` INT NOT NULL,
  `level_id` INT NOT NULL,
  `layer_id` INT NOT NULL,
  `email_id` VARCHAR(45) NOT NULL,
  `org_id` INT NOT NULL,
  `description` VARCHAR (600) NOT NULL,
constraint fk17 FOREIGN KEY (workflow_id) references `workflow`.`workflow_master`(workflow_id) ON DELETE CASCADE ON UPDATE CASCADE,
constraint fk7 FOREIGN KEY (level_id) references `workflow`.`level`(level_id),
constraint fk8 FOREIGN KEY (email_id,org_id) references `workflow`.`user`(email_id,org_id) ON DELETE CASCADE ON UPDATE CASCADE,
constraint fk9 FOREIGN KEY (layer_id) references `workflow`.`layer`(layer_id));

CREATE TABLE `workflow`.`workflowinstance` (
  `workflow_instance_id` INT NOT NULL,
  `workflow_id` INT NOT NULL,
  `level_id` INT NOT NULL,
  `status_id` INT NOT NULL,
  `layer_id` INT NOT NULL,
  `description` VARCHAR (600) NOT NULL,
  `timestamp` VARCHAR(45),
constraint fk10 FOREIGN KEY (level_id) references `workflow`.`level`(level_id),
constraint fk11 FOREIGN KEY (status_id) references `workflow`.`status`(status_id),
constraint fk12 FOREIGN KEY (layer_id) references `workflow`.`layer`(layer_id),
constraint fk13 FOREIGN KEY (workflow_id) references `workflow`.`workflow_master`(workflow_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `workflow`.`workflowinstancelog` (
  `workflow_instance_id` INT NOT NULL,
  `workflow_id` INT NOT NULL,
  `level_id` INT NOT NULL,
  `status_id` INT NOT NULL,
  `layer_id` INT NOT NULL,
  `description` VARCHAR (600) NOT NULL,
  `timestamp` VARCHAR(45),
constraint fk18 FOREIGN KEY (level_id) references `workflow`.`level`(level_id),
constraint fk19 FOREIGN KEY (status_id) references `workflow`.`status`(status_id),
constraint fk20 FOREIGN KEY (layer_id) references `workflow`.`layer`(layer_id),
constraint fk21 FOREIGN KEY (workflow_id) references `workflow`.`workflow_master`(workflow_id) ON DELETE CASCADE ON UPDATE CASCADE
);
  
CREATE TABLE `workflow`.`phonetbl` (
  `email_id` varchar(45) NOT NULL,
  `org_id` INT NOT NULL,
  `phone` varchar(10) NOT NULL,
  constraint fk14 FOREIGN KEY (email_id,org_id) references `workflow`.`user`(email_id,org_id) ON DELETE CASCADE ON UPDATE CASCADE
  );  
  
USE workflow;

#Triggers

DELIMITER $$
CREATE TRIGGER before_workflowinstance_update 
    BEFORE UPDATE ON workflowinstance
    FOR EACH ROW 
BEGIN
    INSERT INTO workflowinstancelog
    SET
    workflow_instance_id = OLD.workflow_instance_id, 
    workflow_id = OLD.workflow_id,
    level_id = OLD.level_id, 
    layer_id = OLD.layer_id, 
    status_id = OLD.status_id, 
    description = OLD.description, 
    timestamp = OLD.timestamp;
    
END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER before_workflowinstance_update_timestamp 
    BEFORE UPDATE ON workflowinstance
    FOR EACH ROW 
BEGIN
  SET NEW.timestamp = NOW();
END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER `before_workflowinstance_insert` BEFORE INSERT ON `workflowinstance`
FOR EACH ROW BEGIN
  SET NEW.timestamp = NOW();
END$$
DELIMITER ;

#Stored Procedure

DROP procedure IF EXISTS `time_duration`;
DELIMITER $$
USE `workflow`$$
CREATE PROCEDURE `time_duration` (IN input_timestamp VARCHAR(100))
BEGIN

IF input_timestamp != now() THEN ((SELECT TIMESTAMPDIFF(MINUTE, input_timestamp, NOW()) as duration_in_minute));
END IF;

END
$$

DELIMITER ;

#views

CREATE  OR REPLACE VIEW `user_dashboard` AS
select rt1.dept_id, rt1.org_id, rt1.email_id, rt2.workflow_instance_id, rt1.workflow_id, rt1.name, rt1.description as description1, rt2.description as description2, rt2.status_id, rt2.level_id, rt2.layer_id, rt2.timestamp from 
(SELECT t1.workflow_id, t1.email_id, t1.org_id, t1.dept_id, t1.description, t2.name FROM workflow.workflow_master as t1 join workflow.request_type as t2 on t1.request_type_id=t2.request_type_id) as rt1 
join
workflow.workflowinstance as rt2 
on
rt1.workflow_id = rt2.workflow_id;
;

CREATE  OR REPLACE VIEW `workflow_instance_status` AS
select rt1.email_id, rt1.org_id, rt1.workflow_instance_id, rt1.workflow_id, rt1.level_id, rt1.layer_id, rt1.status_id, rt1.timestamp, rt2.name, rt2.description from 
(select t1.email_id, t1.org_id, t2.workflow_instance_id, t2.workflow_id, t2.level_id, t2.layer_id, t2.status_id, t2.timestamp from workflow.workflowtbl as t1 join workflow.workflowinstance as t2 on t1.workflow_id = t2.workflow_id ) as rt1
join 
workflow.status as rt2 on rt1.status_id = rt2.status_id;


INSERT INTO organization ( name, address, admin_email, password ) VALUES ('Microsoft','Seattle','micro@micro.com','21232f297a57a5a743894a0e4a801fc3');  
INSERT INTO department ( name, org_id ) VALUES ('Software','1');
INSERT INTO department ( name, org_id ) VALUES ('Hardware','1');
INSERT INTO role ( name) VALUES ('depart_admin');
INSERT INTO role ( name) VALUES ('user');
INSERT INTO user ( email_id, org_id, password, address, dept_id, role_id ) VALUES ('bill@microsoft.com','1','21232f297a57a5a743894a0e4a801fc3','101 E San','1','1');
INSERT INTO phonetbl ( email_id, org_id, phone) VALUES ('bill@microsoft.com','1','0123456789');
INSERT INTO phonetbl ( email_id, org_id, phone) VALUES ('bill@microsoft.com','1','1234567890');
INSERT INTO user ( email_id, org_id, password, address, dept_id, role_id ) VALUES ('parth@microsoft.com','1','21232f297a57a5a743894a0e4a801fc3','111 E San','1','2');
INSERT INTO phonetbl ( email_id, org_id, phone) VALUES ('parth@microsoft.com','1','0123456789');
INSERT INTO phonetbl ( email_id, org_id, phone) VALUES ('parth@microsoft.com','1','1234567890');
INSERT INTO user ( email_id, org_id, password, address, dept_id, role_id ) VALUES ('chinu@microsoft.com','1','21232f297a57a5a743894a0e4a801fc3','111 E San','1','2');
INSERT INTO phonetbl ( email_id, org_id, phone) VALUES ('chinu@microsoft.com','1','0123456789');
INSERT INTO phonetbl ( email_id, org_id, phone) VALUES ('chinu@microsoft.com','1','1234567890');
INSERT INTO user ( email_id, org_id, password, address, dept_id, role_id ) VALUES ('dharmik@microsoft.com','1','21232f297a57a5a743894a0e4a801fc3','111 E San','1','2');
INSERT INTO phonetbl ( email_id, org_id, phone) VALUES ('dharmik@microsoft.com','1','0123456789');
INSERT INTO phonetbl ( email_id, org_id, phone) VALUES ('dharmik@microsoft.com','1','1234567890');
INSERT INTO request_type ( name, email_id, org_id ) VALUES ('code review','bill@microsoft.com','1');
INSERT INTO level ( name, description ) VALUES ('Level 0','Requester');
INSERT INTO level ( name, description ) VALUES ('Level 1','First level worker');
INSERT INTO level ( name, description ) VALUES ('Level 2','Second level worker');
INSERT INTO level ( name, description ) VALUES ('Level 3','Third level worker');
INSERT INTO level ( name, description ) VALUES ('Level 4','Fourth level worker');
INSERT INTO level ( name, description ) VALUES ('Level 5','Fifth level worker');
INSERT INTO level ( name, description ) VALUES ('Level 6','Sixth level worker');
INSERT INTO level ( name, description ) VALUES ('Level 7','Seventh level worker');
INSERT INTO level ( name, description ) VALUES ('Level 8','Eighth level worker');
INSERT INTO level ( name, description ) VALUES ('Level 9','Nineth level worker');
INSERT INTO level ( name, description ) VALUES ('Level 10','Tenth level worker');
INSERT INTO layer ( name, description ) VALUES ('Layer 0','First Worker');
INSERT INTO layer ( name, description ) VALUES ('Layer 1','First alternate Worker');
INSERT INTO layer ( name, description ) VALUES ('Layer 2','Second alternate Worker');
INSERT INTO layer ( name, description ) VALUES ('Layer 3','Third alternate Worker');
INSERT INTO layer ( name, description ) VALUES ('Layer 4','Fourth alternate Worker');
INSERT INTO layer ( name, description ) VALUES ('Layer 5','Fifth alternate Worker');
INSERT INTO layer ( name, description ) VALUES ('Layer 6','Sixth alternate Worker');
INSERT INTO layer ( name, description ) VALUES ('Layer 7','Seventh alternate Worker');
INSERT INTO layer ( name, description ) VALUES ('Layer 8','Eighth alternate Worker');
INSERT INTO layer ( name, description ) VALUES ('Layer 9','Nineth alternate Worker');
INSERT INTO layer ( name, description ) VALUES ('Layer 10','Tenth alternate Worker');
INSERT INTO status ( name, description ) VALUES ('Requested','Request is in requested state.');
INSERT INTO status ( name, description ) VALUES ('Assigned','Request is in assigned state.');
INSERT INTO status ( name, description ) VALUES ('Unassigned','Request is in unassigned state.');
INSERT INTO status ( name, description ) VALUES ('Pending','Request is in pending state.');
INSERT INTO status ( name, description ) VALUES ('Approve','Request is in approved state.');
INSERT INTO status ( name, description ) VALUES ('Reject','Request is in rejected state.');
INSERT INTO workflow_master ( description, request_type_id, email_id, org_id, dept_id ) VALUES ('Code review request for Bill','1','bill@microsoft.com','1','1');
INSERT INTO workflowtbl ( workflow_id, level_id, email_id, org_id, layer_id, description ) VALUES ('1','1','bill@microsoft.com','1','1','Code review request for Bill');
INSERT INTO workflowtbl ( workflow_id, level_id, email_id, org_id, layer_id, description ) VALUES ('1','2','chinu@microsoft.com','1','1','level 1 for code review workflow');
INSERT INTO workflowtbl ( workflow_id, level_id, email_id, org_id, layer_id, description ) VALUES ('1','2','parth@microsoft.com','1','2','layer 1 for code review workflow for level 1');
INSERT INTO workflowtbl ( workflow_id, level_id, email_id, org_id, layer_id, description ) VALUES ('1','3','dharmik@microsoft.com','1','1','level 2 for code review workflow');
INSERT INTO workflowinstance ( workflow_instance_id, workflow_id, level_id, layer_id, status_id, description ) VALUES ('1','1','1','1','1','Request initiated !');
INSERT INTO workflowinstance ( workflow_instance_id, workflow_id, level_id, layer_id, status_id, description ) VALUES ('1','1','2','1','2','Assigned!  level 1 for code review workflow');
INSERT INTO workflowinstance ( workflow_instance_id, workflow_id, level_id, layer_id, status_id, description ) VALUES ('1','1','2','2','2','Assigned!  layer 1 for code review workflow for level 1');

UPDATE workflowinstance SET status_id='4' , description = 'Pending! Working on request..!' WHERE workflow_instance_id = '1' and  workflow_id = '1' and level_id = '2' and layer_id = '1';
UPDATE workflowinstance SET status_id='3' , description = 'Unassigned! Request is in unassigned state.' WHERE workflow_instance_id = '1' and  workflow_id = '1' and level_id = '2' and layer_id = '2';
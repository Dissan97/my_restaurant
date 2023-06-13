drop schema if exists my_restaurant;

create schema my_restaurant;

CREATE TABLE `my_restaurant`.`Users` (
  `username` VARCHAR(64) NOT NULL,
  `password` VARCHAR(128) NOT NULL,
  `name` VARCHAR(64) NOT NULL,
  `surname` VARCHAR(64) NOT NULL,
  `dateOfBirth` VARCHAR(45) NOT NULL,
  `cityOfBirth` VARCHAR(45) NOT NULL,
  `role` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`username`));
  
  CREATE TABLE `my_restaurant`.`Shifts` (
  `code` VARCHAR(8) NOT NULL,
  `task` VARCHAR(256) NOT NULL,
  `role` VARCHAR(45) NOT NULL,
  `salary` DOUBLE NOT NULL,
  PRIMARY KEY (`code`));

CREATE TABLE `my_restaurant`.`Employees` (
  `employee` VARCHAR(45) NOT NULL,
  `user` VARCHAR(64) NOT NULL,
  PRIMARY KEY (`employee`, `user`),
  INDEX `fk_Employee_1_idx` (`user` ASC) VISIBLE,
  CONSTRAINT `fk_Employee_1`
    FOREIGN KEY (`user`)
    REFERENCES `my_restaurant`.`Users` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
    
    CREATE TABLE `my_restaurant`.`ShiftSchedules` (
  `employee` VARCHAR(45) NOT NULL,
  `shift` VARCHAR(45) NOT NULL,
  `shiftDate` VARCHAR(45) NOT NULL,
  `updateRequest` TINYINT NOT NULL,
  PRIMARY KEY (`employee`, `shift`, `shiftDate`),
  INDEX `fk_ShiftSchedules_2_idx` (`shift` ASC) VISIBLE,
  CONSTRAINT `fk_ShiftSchedules_1`
    FOREIGN KEY (`employee`)
    REFERENCES `my_restaurant`.`Employees` (`employee`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ShiftSchedules_2`
    FOREIGN KEY (`shift`)
    REFERENCES `my_restaurant`.`Shifts` (`code`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

USE `my_restaurant`;
-- STORED PROCEDURES 

-- ----------------------------- --
-- PULL USERS
-- ----------------------------- --

DELIMITER $$
CREATE PROCEDURE `pullUsers` ()
BEGIN
	declare exit handler for sqlexception
		begin 
			rollback;
		end;
	set transaction isolation level read committed;
    start transaction;
		select * from `my_restaurant`.`Users`;
    commit;
END$$
DELIMITER ;

DELIMITER $$
-- ----------------------------- --
-- PUSH USERS
-- ----------------------------- --
DELIMITER $$
CREATE PROCEDURE `pushUser` (in usr varchar(64), in pwd varchar(64), in nm varchar(64), in snm varchar(64), in dob varchar(16), in cob varchar (64), in rl varchar(32))
BEGIN
	declare exit handler for sqlexception
	begin
		rollback;
	end;
	set transaction isolation level read uncommitted;
	start transaction;
        insert into `my_restaurant`.`Users` values (usr, pwd, nm, snm, dob, cob, rl);
	commit;
END$$
DELIMITER ;
-- ----------------------------- --
-- PULL USER_BY_USERNAME
-- ----------------------------- --
CREATE PROCEDURE `pullUserByUsername` (in usr varchar(64))
BEGIN
		declare exit handler for sqlexception
		begin 
			rollback;
		end;
	set transaction isolation level read committed;
    start transaction;
		SELECT * FROM `my_restaurant`.`Users` where username = usr;
    commit;
END$$

DELIMITER ;


-- ----------------------------- --
-- PULL SHIFTS
-- ----------------------------- --
DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `pullShifts`()
BEGIN
	declare exit handler for sqlexception
		begin 
			rollback;
		end;
	set transaction isolation level read committed;
    start transaction;
		SELECT * FROM `my_restaurant`.`Shifts`;
    commit;
END$$

DELIMITER ;

DELIMITER $$

CREATE PROCEDURE `pushShiftSchedule` (in emp varchar(64), in sft varchar(64), in shift_date varchar (45), in up boolean)
BEGIN
	declare exit handler for sqlexception
	begin 
		rollback;
	end;
	set transaction isolation level read uncommitted;
	start transaction;
        INSERT INTO `my_restaurant`.`ShiftSchedules`
		(`employee`,`shift`,`shiftDate`,`updateRequest`)
		VALUES(emp, sft, shift_date, up);
	commit;
END$$
DELIMITER ;

DELIMITER $$

CREATE PROCEDURE `pullSchedules` ()
BEGIN
	declare exit handler for sqlexception
	begin 
		rollback;
	end;
	set transaction isolation level read committed;
	start transaction;
        select * from `my_restaurant`.`ShiftSchedules`
	commit;
END$$



-- USERS --
INSERT INTO `my_restaurant`.`Users`
(`username`,`password`,`name`,`surname`,`dateOfBirth`,`cityOfBirth`,`role`)VALUES
("attendant","993514d22636f383c5e511fbad6a9da0d3a8745553bebe8fa6e3f7269238da96","aName","aSurname","1997-10-09","aCity","ATTENDANT");
INSERT INTO `my_restaurant`.`Users`
(`username`,`password`,`name`,`surname`,`dateOfBirth`,`cityOfBirth`,`role`)VALUES
("manager","993514d22636f383c5e511fbad6a9da0d3a8745553bebe8fa6e3f7269238da96","mName","mSurname","1997-10-09","mCity","MANAGER");
INSERT INTO `my_restaurant`.`Users`
(`username`,`password`,`name`,`surname`,`dateOfBirth`,`cityOfBirth`,`role`)VALUES
("cooker","993514d22636f383c5e511fbad6a9da0d3a8745553bebe8fa6e3f7269238da96","cName","cSurname","1997-10-09","cCity","COOKER");
-- SHIFTS --
INSERT INTO `my_restaurant`.`Shifts`(`code`,`task`,`role`, `salary`)
VALUES("12345", "Attendant task", "ATTENDANT", 800.0);
INSERT INTO `my_restaurant`.`Shifts`(`code`,`task`,`role`, `salary`)
VALUES("12346", "Senior Attendant task", "ATTENDANT", 1200.0);
INSERT INTO `my_restaurant`.`Shifts`(`code`,`task`,`role`, `salary`)
VALUES("12347", "cooker", "COOKER", 1400.0);
INSERT INTO `my_restaurant`.`Shifts`(`code`,`task`,`role`, `salary`)
VALUES("12348", "Chief", "COOKER", 2400.0);


drop user if exists 'MANAGER'@'localhost';
create user 'MANAGER'@'localhost' identified by 'password';
drop user if exists 'ATTENDANT'@'localhost';
create user 'ATTENDANT'@'localhost' identified by 'password';
drop user if exists 'COOKER'@'localhost';
create user 'COOKER'@'localhost' identified by 'password';
drop user if exists 'LOGIN'@'localhost';
create user 'LOGIN'@'localhost' identified by 'password';

grant execute on procedure `my_restaurant`.`pullUsers` to 'LOGIN'@'localhost';
grant execute on procedure `my_restaurant`.`pullUsers` to 'LOGIN'@'localhost';
grant execute on procedure `my_restaurant`.`pushUser` to 'LOGIN'@'localhost';
grant execute on procedure `my_restaurant`.`pullShifts` to 'MANAGER'@'localhost';
grant execute on procedure `my_restaurant`.`pullShifts` to 'ATTENDANT'@'localhost';
grant execute on procedure `my_restaurant`.`pullShifts` to 'COOKER'@'localhost';


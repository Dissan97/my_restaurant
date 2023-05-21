create user `manager`@`localhost` identified by "password";

use restaurant;

CREATE TABLE `restaurant`.`Shift` (
  `shift_code` varchar(16) NOT NULL,
  `task` varchar(256) NOT NULL,
  `role` varchar(45) NOT NULL,
  PRIMARY KEY (`shift_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

delimiter %%


CREATE PROCEDURE `get_shift_list`()
BEGIN
 	set transaction isolation level read committed;
    start transaction;
		select * from `restaurant`.`Shift`;
    commit;
END %%
delimiter ;

grant execute on procedure `restaurant`.`get_shift_list` to `manager`@`localhost`;
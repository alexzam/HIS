ALTER TABLE sysParameters
  ADD CONSTRAINT fk_sysparam_user
  FOREIGN KEY (owner)
  REFERENCES `user` (id)
  ON DELETE CASCADE
  ON UPDATE CASCADE
, ADD INDEX fk_sysparam_user (owner);

INSERT INTO sysParameters (`name`, val) VALUES ('ui.colorScheme', 'C');
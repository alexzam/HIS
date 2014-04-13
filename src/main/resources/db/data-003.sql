 TABLE transaction
  DROP FOREIGN KEY `FK7FA0D2DE2E76915B`, 
  DROP FOREIGN KEY `FK7FA0D2DE374A3CCF`;

ALTER TABLE transaction 
  ADD COLUMN `pair_id` INT(11) NULL DEFAULT NULL, 
  ADD COLUMN `transfer` BIT(1) NOT NULL DEFAULT 0, 
  CHANGE COLUMN `common` `common` BIT(1) NOT NULL DEFAULT 0, 
  CHANGE COLUMN `timestmp` `timestmp` DATETIME NOT NULL, 
  CHANGE COLUMN `account_id` `account_id` INT(11) NOT NULL, 
  CHANGE COLUMN `actor_id` `actor_id` INT(11) NOT NULL, 
  ADD CONSTRAINT `FK7FA0D2DE2E76915B` FOREIGN KEY (`actor_id`) REFERENCES `user`(`id`), 
  ADD CONSTRAINT `FK7FA0D2DE374A3CCF` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
  ADD CONSTRAINT `FK_TRANSACT_PAIR` FOREIGN KEY (`pair_id`) REFERENCES `transaction` (`id`) 
    ON DELETE CASCADE ON UPDATE RESTRICT, 
  ADD INDEX `FK_TRANSACT_PAIR` (`pair_id` ASC);

ALTER TABLE `account`
  ADD COLUMN `owner_id` INT(11) NULL DEFAULT NULL, 
  ADD COLUMN `public` BIT(1) NOT NULL DEFAULT 1,
  ADD COLUMN `hidden` BIT(1) NOT NULL DEFAULT 0,
  ADD CONSTRAINT `FK_ACCOUNT_OWNER` FOREIGN KEY (`owner_id`) REFERENCES `user` (`id`)
    ON DELETE RESTRICT ON UPDATE RESTRICT,
  ADD INDEX `FK_ACCOUNT_OWNER` (`owner_id` ASC);

drop table `personallimit`;

### Update accounts
UPDATE account SET id=1, name='Казна', public = 1;

INSERT INTO account(`name`, value, owner_id, public, hidden)
  SELECT CONCAT(name, ' money'), 0, id, 0, 0
    FROM user;

### Make transfers instead of Donate/Refunds
UPDATE transaction SET transfer=1 where category_id=1;

# Make pair transactions for donate/refund ones
INSERT INTO transaction (amount, comment, common, timestmp, account_id, actor_id, category_id, pair_id, transfer)
  SELECT -amount, `comment`, 0, timestmp, a.id, actor_id, 1, t.id, 1
  FROM transaction AS t
    JOIN account AS a ON a.owner_id = actor_id
  WHERE (category_id = 1 OR category_id = 2) AND a.public = 0;

# Link pair transactions to original ones
UPDATE transaction AS t1
  JOIN transaction AS t2 ON t2.pair_id = t1.id
SET t1.pair_id = t2.id
WHERE (t1.category_id = 1 OR t1.category_id = 2)
      AND t1.pair_id IS NULL;

UPDATE transaction
SET category_id = 1
WHERE category_id = 2;

 UPDATE transcategory
 SET name = 'Transfer'
 WHERE id = 1;

 DELETE FROM transcategory
 WHERE id = 2;

 # Move spending transactions to users
 UPDATE transaction AS t
   JOIN account AS a ON t.actor_id = a.owner_id
 SET account_id = a.id
 WHERE a.public = 0
       AND t.category_id > 2;

 ### Balance user accounts to 0
 INSERT INTO transcategory (id, name, type) VALUES (2, 'Correction', 3);

 INSERT INTO transaction (amount, comment, common, timestmp, account_id, actor_id, category_id, pair_id, transfer)
   SELECT -sum(t.amount), 'Correction to 0 on software update', 0, now(), a.id, a.owner_id, 2, NULL, 0
   FROM transaction AS t
     JOIN account AS a ON t.account_id = a.id
   WHERE a.public = 0
   GROUP BY t.account_id;

 ### Sanitize accounts denormalizations
  UPDATE account AS a
  SET a.value = (
    SELECT
      sum(amount)
    FROM transaction
    WHERE a.id = transaction.account_id
  );

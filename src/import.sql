INSERT INTO user(name) VALUES ('AlexZam');
INSERT INTO user(name) VALUES ('Anitra');

INSERT INTO transcategory(name,type)VALUES('Donate',2);
INSERT INTO transcategory(name,type)VALUES('Refund',2);
INSERT INTO transcategory(name,type)VALUES('test Exp category',0);
INSERT INTO transcategory(name,type)VALUES('Food',0);

INSERT INTO account(name,value) VALUES ('Казна', 12345);

INSERT INTO `transaction`(amount,`comment`,timestmp,account_id,actor_id,category_id,common) VALUES(-65410,'Comment here',now(),1,1,5,1);
INSERT INTO user(name) VALUES ('AlexZam');
INSERT INTO user(name) VALUES ('Anitra');

INSERT INTO transcategory(name,type)VALUES('test Inc category',1);
INSERT INTO transcategory(name,type)VALUES('test Exp category',0);
INSERT INTO transcategory(name,type)VALUES('Food',0);

INSERT INTO account(name,value) VALUES ('Казна', 123);

INSERT INTO `transaction`(amount,`comment`,timestmp,account_id,actor_id,category_id) VALUES(654,'Comment here',now(),1,1,1);
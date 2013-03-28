CREATE TABLE sysParameters (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(64) NOT NULL,
    val VARCHAR(255) NOT NULL,
    owner INT DEFAULT NULL,
    PRIMARY KEY (id)
);

INSERT INTO sysParameters (name,val)
VALUES("db.version","0.1.3.0");

INSERT INTO sysParameters (name,val)
VALUES("db.versionid","1");

DROP DATABASE IF EXISTS twitter_userinfo;
CREATE DATABASE twitter_userinfo;
USE twitter_userinfo;

CREATE TABLE login(
username VARCHAR(50) PRIMARY KEY NOT NULL,
password VARCHAR(50)
);

CREATE TABLE keywords(
username VARCHAR(50),
keyword VARCHAR(50)
);

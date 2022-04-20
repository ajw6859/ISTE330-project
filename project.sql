-- Group 1
-- ISTE 330, Project 
-- Author: Allison Wright

-- Info select for tee file
SELECT 'ISTE-330-01', 'Project 01' AS 'Group 1', CURDATE() AS 'Todays_Date';

-- Create the db
DROP DATABASE IF EXISTS project;
CREATE DATABASE project;
USE project;

-- Create the tables 
-- lookup tables first, associative come last 
CREATE TABLE Lookup_User_Type(
  user_type_ID INT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_type VARCHAR(45) NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT NULL,
  PRIMARY KEY (user_type_ID)
  )ENGINE=InnoDB DEFAULT CHARSET= utf8;
  
  CREATE TABLE Lookup_Keyword(
	keyword_ID INT UNSIGNED NOT NULL AUTO_INCREMENT,
    keyword_type VARCHAR(45) NOT NULL, 
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
	updated_at DATETIME DEFAULT NULL,
    PRIMARY KEY (keyword_ID)
  )ENGINE=INNODB DEFAULT CHARSET = utf8;
  
  CREATE TABLE Lookup_Department(
	department_ID INT UNSIGNED NOT NULL AUTO_INCREMENT,
    department VARCHAR(45) NOT NULL, 
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
	updated_at DATETIME DEFAULT NULL,
    PRIMARY KEY (department_ID)
  )ENGINE=INNODB DEFAULT CHARSET = utf8;

CREATE TABLE User(
	user_ID INT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_type_ID INT UNSIGNED NOT NULL,
    first_name VARCHAR(45) NOT NULL, 
    last_name VARCHAR(45) NOT NULL,
    password VARCHAR(60),
    email VARCHAR(60) NOT NULL,
    cell_phone VARCHAR(45) NOT NULL,
    department_ID INT UNSIGNED NOT NULL,
    major VARCHAR(60) DEFAULT NULL,
    office_number VARCHAR(45) DEFAULT NULL, -- will prob be building, room number
	office_hours varchar(60) DEFAULT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
	updated_at DATETIME DEFAULT NULL,
    PRIMARY KEY (user_ID),
    CONSTRAINT user_type_FK FOREIGN KEY (user_type_ID) 
      REFERENCES Lookup_User_Type(user_type_ID)
            ON DELETE CASCADE
            ON UPDATE CASCADE, 
    CONSTRAINT department_type_FK FOREIGN KEY (department_ID) 
      REFERENCES Lookup_Department(department_ID)
            ON DELETE CASCADE
            ON UPDATE CASCADE         
  )ENGINE=INNODB DEFAULT CHARSET = utf8;
  
  CREATE TABLE Abstract(
	abstract_ID INT UNSIGNED NOT NULL AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL, 
    abstract MEDIUMTEXT NOT NULL,
    keywords MEDIUMTEXT, 
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
	updated_at DATETIME DEFAULT NULL,
    PRIMARY KEY (abstract_ID)
  )ENGINE=INNODB DEFAULT CHARSET = utf8;
  
  CREATE TABLE User_To_Abstract(
    user_ID INT UNSIGNED NOT NULL,
    abstract_ID INT UNSIGNED NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
	updated_at DATETIME DEFAULT NULL,
    PRIMARY KEY (user_ID, abstract_ID),
    CONSTRAINT user_to_abstract_FK FOREIGN KEY (user_ID) 
      REFERENCES User(user_ID)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT abstract_to_user_FK FOREIGN KEY (abstract_ID) 
      REFERENCES Abstract(abstract_ID)
            ON DELETE CASCADE
            ON UPDATE CASCADE        
	
  )ENGINE=INNODB DEFAULT CHARSET = utf8;
  
  CREATE TABLE User_To_Keyword(
    user_ID INT UNSIGNED NOT NULL,
    keyword_ID INT UNSIGNED NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
	updated_at DATETIME DEFAULT NULL,
    PRIMARY KEY (user_ID, keyword_ID),
    CONSTRAINT user_to_user_FK FOREIGN KEY (user_ID) 
      REFERENCES User(user_ID)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
	CONSTRAINT keyword_to_keyword_FK FOREIGN KEY (keyword_ID) 
      REFERENCES Lookup_Keyword(keyword_ID)
            ON DELETE CASCADE
            ON UPDATE CASCADE
  )ENGINE=INNODB DEFAULT CHARSET = utf8;
  
  CREATE TABLE Connection(
	connection_ID INT UNSIGNED NOT NULL AUTO_INCREMENT,
    faculty_ID INT UNSIGNED NOT NULL,
    student_ID INT UNSIGNED NOT NULL,
    keyword_ID INT Unsigned not NULL,
    has_connected BIT not NULL DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
	updated_at DATETIME DEFAULT NULL,
    PRIMARY KEY (connection_ID),
    CONSTRAINT faculty_FK FOREIGN KEY (faculty_ID) 
      REFERENCES User(user_ID)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
	CONSTRAINT student_FK FOREIGN KEY (student_ID) 
      REFERENCES User(user_ID)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
	CONSTRAINT keyword_FK FOREIGN KEY (keyword_ID) 
      REFERENCES Lookup_Keyword(keyword_ID)
            ON DELETE CASCADE
            ON UPDATE CASCADE
  )ENGINE=INNODB DEFAULT CHARSET = utf8;
  


-- populate tables (lookup tables first)

-- USER TYPE
INSERT INTO Lookup_User_Type VALUES (1, 'Professor', NOW(), NOW());
INSERT INTO Lookup_User_Type VALUES (2, 'Student', NOW(), NOW());
INSERT INTO Lookup_User_Type VALUES (3, 'Public', NOW(), NOW());

-- KEYWORDS 
INSERT INTO Lookup_Keyword VALUES (1, 'Cyber Security', NOW(), NOW());
INSERT INTO Lookup_Keyword VALUES (2, 'Software Engingeering', NOW(), NOW());
INSERT INTO Lookup_Keyword VALUES (3, 'Databases', NOW(), NOW());
INSERT INTO Lookup_Keyword VALUES (4, 'Web', NOW(), NOW());
INSERT INTO Lookup_Keyword VALUES (5, 'Mobile', NOW(), NOW());
INSERT INTO Lookup_Keyword VALUES (6, 'Networking', NOW(), NOW());

-- DEPARTMENTS
INSERT INTO Lookup_Department VALUES(1, 'Computer Science', NOW(), NOW());
INSERT INTO Lookup_Department VALUES(2, 'Computing Security', NOW(), NOW());

-- USERS
-- Passwords will be hashed when implemented with java
INSERT INTO User VALUES (1, 1, 'Professor', 'Test', 'test', 'test@rit.edu', '0000000000', 1, NULL, 'GOL 3650', 'M, W 10-12', NOW(), NOW());
INSERT INTO User VALUES (2, 2, 'Moose', 'Wright', 'test', 'mmw@rit.edu', '9782398944', 2, 'Computing Security', NULL, NULL, NOW(), NOW());

-- ABSTRACT
INSERT INTO Abstract VALUES(1, 'Sample title', 'This is some sample text. There may or may not be some words we won\'t allow in here', null, NOW(), NOW());

-- USER_TO_ABSTRACT
INSERT INTO User_To_Abstract VALUES (1, 1, NOW(), NOW());

-- USER_TO_KEYWORD
INSERT INTO User_To_Keyword VALUES (1, 1, 3, NOW(), NOW()); -- test professor to db 
INSERT INTO User_To_Keyword VALUES (2, 1, 2, NOW(), NOW()); -- test professor to se
INSERT INTO User_To_Keyword VALUES (3, 2, 3, NOW(), NOW()); -- Student to db

-- CONNECTIONS
INSERT INTO Connection VALUES (1, 1, 2, 3, 0, NOW(), NOW());

-- Selects to make sure it all worked (sanity check)
SHOW TABLES; 
SELECT * FROM Connection;

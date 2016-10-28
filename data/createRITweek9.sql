DROP DATABASE IF EXISTS RIT;

CREATE DATABASE RIT;

USE RIT;

DROP TABLE IF NOT EXISTS college;

CREATE TABLE college (
	collegeID varchar(5),
	collegeName varchar(60) NOT NULL,
	CONSTRAINT college_PK PRIMARY KEY(collegeID));
	
DESCRIBE college;

DROP TABLE department;

CREATE TABLE department (
	deptNum varchar(4),
	deptName varchar(50) NOT NULL,
	collegeID varchar(5) NOT NULL,
	CONSTRAINT department_PK PRIMARY KEY (deptNum),
	CONSTRAINT department_college_FK FOREIGN KEY (collegeID) REFERENCES college(collegeID));

describe department;

CREATE TABLE deptPhone (
	deptNum varchar(4),
	type varchar(5),
	phoneNum varchar(10) NOT NULL,
	CONSTRAINT deptPhone_PK PRIMARY KEY (deptNum, type),
	CONSTRAINT deptPhone_department_FK FOREIGN KEY (deptNum) REFERENCES department(deptNum));

describe deptPhone;

DROP TABLE course;

CREATE TABLE course(
	deptNum varchar(4),
	courseNum varchar(3),
	courseDesc varchar(300),
	creditHours int,
	CONSTRAINT course_PK PRIMARY KEY (deptNum,courseNum)
	);

describe course;

INSERT INTO `college` VALUES ('COE','Kate Gleason College of Engineering'),('GCCIS','Golisano College of Computing and Information Sciences'),('NTID','National Technical Institute for the Deaf');
INSERT INTO `department` VALUES ('0301','Electrical Engineering','COE'),('0306','Computer Engineering','COE'),('0308','Microsystems Engineering','COE'),('4002','Information Technology','GCCIS'),('4003','Computer Science','GCCIS'),('4010','Software Engineering','GCCIS'),('4050','NSSA','GCCIS');
INSERT INTO `course` VALUES ('0301','219','Intro to Circuits',4),('4002','217','Java Programming For IT I',4),('4002','218','Java Programming For IT II',4),('4002','219','Java Programming for IT III',4),('4002','360','Intro to Databases',4),('4003','317','Data Structures',4),('4010','340','Intro to Networking',4);

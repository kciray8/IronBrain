DROP DATABASE IF EXISTS IB;
CREATE DATABASE IB;
USE IB;
SET foreign_key_checks = 0;

CREATE TABLE Users(
    id          INTEGER   PRIMARY KEY AUTO_INCREMENT,
    login       VARCHAR( 100 )  NOT NULL,
    password    VARCHAR( 100 )  NOT NULL,
    email       VARCHAR( 100 )  NOT NULL,
    root INTEGER,
    FOREIGN KEY ( root ) REFERENCES Sections(id)
) DEFAULT CHARSET=utf8;

INSERT INTO Users VALUES (1,'kciray','1234','kciray8@gmail.com',1);
INSERT INTO Users VALUES (2,'lol','222','lolka@gmail.com',7);

CREATE TABLE Tickets(
    id             INTEGER   PRIMARY KEY AUTO_INCREMENT,
    questions      TEXT,
    answers        TEXT,
    owner       INTEGER   DEFAULT 1,
    FOREIGN KEY ( owner ) REFERENCES Users(id)
) DEFAULT CHARSET=utf8;

INSERT INTO Tickets VALUES (1,'Вопрос1!!!','Ответ1 <b>жирный</b>',1);
INSERT INTO Tickets VALUES (2,'Вопрос2!!!','Ответ2 <b>жирный</b>',1);

CREATE TABLE Sections(
    id          INTEGER   PRIMARY KEY AUTO_INCREMENT,
    parent      INTEGER   DEFAULT NULL,
    label        VARCHAR( 100 )  NOT NULL,
    ticket      INTEGER   DEFAULT NULL,
    owner       INTEGER   DEFAULT 1,
    FOREIGN KEY ( parent ) REFERENCES Sections(id),
    FOREIGN KEY ( ticket ) REFERENCES Tickets(id),
    FOREIGN KEY ( owner ) REFERENCES Users(id)
) DEFAULT CHARSET=utf8;

INSERT INTO Sections VALUES (1,NULL,'Билеты',NULL,1);
INSERT INTO Sections VALUES (2,1,'Java',NULL,1);
INSERT INTO Sections VALUES (3,1,'Правоведение',NULL,1);
INSERT INTO Sections VALUES (4,2,'Шилдт - основы',NULL,1);
INSERT INTO Sections VALUES (5,4,'билет1',1,1);
INSERT INTO Sections VALUES (6,4,'билетN',2,1);
INSERT INTO Sections VALUES (7,NULL,'Билеты',NULL,2);

CREATE TABLE Remind(
    id          INTEGER   PRIMARY KEY AUTO_INCREMENT,
    user       INTEGER,
    ticket      INTEGER   DEFAULT NULL,
    FOREIGN KEY ( user ) REFERENCES Users(id),
    FOREIGN KEY ( ticket ) REFERENCES Tickets(id)
) DEFAULT CHARSET=utf8;

SET foreign_key_checks = 1;
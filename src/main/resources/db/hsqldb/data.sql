-- ADMINISTRADORES (Todos tienen como contraseña "admin") --
INSERT INTO users(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('admin1','$2a$10$DXvKFs64uY6HJKVy.clI3uaJeWNvBtB.r62FrZ1x.gTcTc2jGxb5y','TRUE','Admin FN','Admin LN','admin1@sevenislands.com','2022-10-29','admin','2022-10-29','adminAvatar.png');
INSERT INTO users(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('albperleo','$2a$10$DXvKFs64uY6HJKVy.clI3uaJeWNvBtB.r62FrZ1x.gTcTc2jGxb5y','TRUE','Alberto','Perea Leon','albperleo@sevenislands.com','2022-10-29','admin','2022-10-29','adminAvatar.png');
INSERT INTO users(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('javnunrui','$2a$10$DXvKFs64uY6HJKVy.clI3uaJeWNvBtB.r62FrZ1x.gTcTc2jGxb5y','TRUE','Javier','Nunes Ruiz','javnunrui@sevenislands.com','2022-10-29','admin','2022-10-29','adminAvatar.png');
INSERT INTO users(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('marvicmar','$2a$10$DXvKFs64uY6HJKVy.clI3uaJeWNvBtB.r62FrZ1x.gTcTc2jGxb5y','TRUE','Maria','Vico Martin','marvicmar@sevenislands.com','2022-10-29','admin','2022-10-29','adminAvatar.png');
INSERT INTO users(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('juaramlop2','$2a$10$DXvKFs64uY6HJKVy.clI3uaJeWNvBtB.r62FrZ1x.gTcTc2jGxb5y','TRUE','Juan Carlos','Ramirez Lopez','juaramlop2@sevenislands.com','2022-10-29','admin','2022-10-29','adminAvatar.png');
INSERT INTO users(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('alepervaz','$2a$10$DXvKFs64uY6HJKVy.clI3uaJeWNvBtB.r62FrZ1x.gTcTc2jGxb5y','TRUE','Alejandro','Perez Vazquez','alepervaz@sevenislands.com','2022-10-29','admin','2022-10-29','adminAvatar.png');

-- JUGADORES (Todos tienen como contraseña "player") --
INSERT INTO users(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('player1','$2a$10$Yurjw7YseLzZq/JOixdY/eRmBrOIzHHNooczuuMwtC42R8vRoKVPC','TRUE','Player FN','Player LN','player1@sevenislands.com','2022-10-29','player','2022-10-29','playerAvatar.png');
INSERT INTO users(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('player2','$2a$10$Yurjw7YseLzZq/JOixdY/eRmBrOIzHHNooczuuMwtC42R8vRoKVPC','TRUE','Player FN','Player LN','player2@sevenislands.com','2022-10-29','player','2022-10-29','playerAvatar.png');
INSERT INTO users(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('player3','$2a$10$Yurjw7YseLzZq/JOixdY/eRmBrOIzHHNooczuuMwtC42R8vRoKVPC','TRUE','Player FN','Player LN','player3@sevenislands.com','2022-10-29','player','2022-10-29','playerAvatar.png');

-- LOBBY --
--INSERT INTO lobby(code,active) VALUES('00000000','TRUE');

--INSERT INTO lobby_players(lobby_id,players_id) VALUES (1,8);

-- MATERIALES --
INSERT INTO cards(id,name,multiplicity) VALUES (1,'barril',6);
INSERT INTO cards(id,name,multiplicity) VALUES (2,'mapa_tesoro',4);
INSERT INTO cards(id,name,multiplicity) VALUES (3,'collar',4);
INSERT INTO cards(id,name,multiplicity) VALUES (4,'caliz',3);
INSERT INTO cards(id,name,multiplicity) VALUES (5,'corona',4);
INSERT INTO cards(id,name,multiplicity) VALUES (6,'diamante',3);
INSERT INTO cards(id,name,multiplicity) VALUES (7,'doblon',27);
INSERT INTO cards(id,name,multiplicity) VALUES (8,'revolver',6);
INSERT INTO cards(id,name,multiplicity) VALUES (9,'rubi',3);
INSERT INTO cards(id,name,multiplicity) VALUES (10,'espada',6);
INSERT INTO cards(id,name,multiplicity) VALUES (11,'baraja',1);

-- CREACIÓN DE UNA PARTIDA --


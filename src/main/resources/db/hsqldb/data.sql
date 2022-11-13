-- ADMINISTRADORES --
INSERT INTO users(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('admin1','admin','TRUE','Admin FN','Admin LN','admin1@sevenislands.com','2022-10-29','admin','2022-10-29','adminAvatar.png');
INSERT INTO authorities(user_id,authority) VALUES (1,'admin');
INSERT INTO users(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('albperleo','1234','TRUE','Alberto','Perea Leon','albperleo@sevenislands.com','2022-10-29','admin','2022-10-29','adminAvatar.png');
INSERT INTO authorities(user_id,authority) VALUES (2,'admin');
INSERT INTO users(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('javnunrui','dp1','TRUE','Javier','Nunes Ruiz','javnunrui@sevenislands.com','2022-10-29','admin','2022-10-29','adminAvatar.png');
INSERT INTO authorities(user_id,authority) VALUES (3,'admin');
INSERT INTO users(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('marvicmar','2121','TRUE','Maria','Vico Martin','marvicmar@sevenislands.com','2022-10-29','admin','2022-10-29','adminAvatar.png');
INSERT INTO authorities(user_id,authority) VALUES (4,'admin');
INSERT INTO users(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('juaramlop2','1234','TRUE','Juan Carlos','Ramirez Lopez','juaramlop2@sevenislands.com','2022-10-29','admin','2022-10-29','adminAvatar.png');
INSERT INTO authorities(user_id,authority) VALUES (5,'admin');
INSERT INTO users(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('alepervaz','1234','TRUE','Alejandro','Perez Vazquez','alepervaz@sevenislands.com','2022-10-29','admin','2022-10-29','adminAvatar.png');
INSERT INTO authorities(user_id,authority) VALUES (6,'admin');

-- JUGADORES --
INSERT INTO users(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('player1','player','TRUE','Player FN','Player LN','player1@sevenislands.com','2022-10-29','player','2022-10-29','playerAvatar.png');
INSERT INTO authorities(user_id,authority) VALUES (7,'player');
INSERT INTO users(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('player2','player','TRUE','Player FN','Player LN','player2@sevenislands.com','2022-10-29','player','2022-10-29','playerAvatar.png');
INSERT INTO authorities(user_id,authority) VALUES (8,'player');
INSERT INTO users(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('player3','player','TRUE','Player FN','Player LN','player3@sevenislands.com','2022-10-29','player','2022-10-29','playerAvatar.png');
INSERT INTO authorities(user_id,authority) VALUES (9,'player');

-- LOBBY --
--INSERT INTO lobby(code,active) VALUES('00000000','TRUE');

--INSERT INTO lobby_players(lobby_id,players_id) VALUES (1,8);

-- MATERIALES --
INSERT INTO board(id,background,width,height) VALUES (1,'resources/images/Tablero_recortado.jpg',1903, 2325);

INSERT INTO card_types(id,name,multiplicity) VALUES (1,'baraja',1);
INSERT INTO card_types(id,name,multiplicity) VALUES (2,'barril',3);
INSERT INTO card_types(id,name,multiplicity) VALUES (3,'botella',3);
INSERT INTO card_types(id,name,multiplicity) VALUES (4,'collar',3);
INSERT INTO card_types(id,name,multiplicity) VALUES (5,'copa',3);
INSERT INTO card_types(id,name,multiplicity) VALUES (6,'corona',3);
INSERT INTO card_types(id,name,multiplicity) VALUES (7,'diamante',3);
INSERT INTO card_types(id,name,multiplicity) VALUES (8,'doblon',3);
INSERT INTO card_types(id,name,multiplicity) VALUES (9,'pistola',3);
INSERT INTO card_types(id,name,multiplicity) VALUES (10,'rubi',3);
INSERT INTO card_types(id,name,multiplicity) VALUES (11,'sable',3);
INSERT INTO card_types(id,name,multiplicity) VALUES (12,'backimage',3);

INSERT INTO cards(id,board_id,card_type_id,position) VALUES (1,1,1,7);


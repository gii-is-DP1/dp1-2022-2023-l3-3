-- ADMINISTRADORES --
INSERT INTO users(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('admin1','4dm1n','TRUE','Admin FN','Admin LN','admin1@sevenislands.com','2022-10-29','admin','2022-10-29','avatar1.jpg');
INSERT INTO authorities(user_id,authority) VALUES (1,'admin');
INSERT INTO users(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('albperleo','1234','TRUE','Alberto','Perea León','albperleo@sevenislands.com','2022-10-29','admin','2022-10-29','avatar1.jpg');
INSERT INTO authorities(user_id,authority) VALUES (2,'admin');
INSERT INTO users(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('javnunrui','dp1','TRUE','Javier','Nunes Ruiz','javnunrui@sevenislands.com','2022-10-29','admin','2022-10-29','avatar1.jpg');
INSERT INTO authorities(user_id,authority) VALUES (3,'admin');
INSERT INTO users(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('marvicmar','2121','TRUE','María','Vico Martín','marvicmar@sevenislands.com','2022-10-29','admin','2022-10-29','avatar1.jpg');
INSERT INTO authorities(user_id,authority) VALUES (4,'admin');
INSERT INTO users(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('juaramlop2','1234','TRUE','Juan Carlos','Ramírez López','juaramlop2@sevenislands.com','2022-10-29','admin','2022-10-29','avatar1.jpg');
INSERT INTO authorities(user_id,authority) VALUES (5,'admin');
INSERT INTO users(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('alepervaz','1234','TRUE','Alejandro','Pérez Vázquez','alepervaz@sevenislands.com','2022-10-29','admin','2022-10-29','avatar1.jpg');
INSERT INTO authorities(user_id,authority) VALUES (6,'admin');

-- JUGADORES --
INSERT INTO users(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('player1','player','TRUE','Player FN','Player LN','player1@sevenislands.com','2022-10-29','player','2022-10-29','avatar1.jpg');
INSERT INTO authorities(user_id,authority) VALUES (7,'player');
INSERT INTO users(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('player2','player','TRUE','Player FN','Player LN','player2@sevenislands.com','2022-10-29','player','2022-10-29','avatar1.jpg');
INSERT INTO authorities(user_id,authority) VALUES (8,'player');
INSERT INTO users(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('player3','player','TRUE','Player FN','Player LN','player3@sevenislands.com','2022-10-29','player','2022-10-29','avatar1.jpg');
INSERT INTO authorities(user_id,authority) VALUES (9,'player');

-- LOBBY --
--INSERT INTO lobby(code,active) VALUES('00000000','TRUE');

--INSERT INTO lobby_players(lobby_id,players_id) VALUES (1,8);

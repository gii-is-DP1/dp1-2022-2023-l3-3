-- ADMINISTRADORES --
INSERT INTO users(nickname,password,first_name,last_name,email,type,birth_date,avatar) VALUES ('admin1','4dm1n','Admin FN','Admin LN','admin1@sevenislands.com','admin','2022-10-29','avatar1.jpg');
INSERT INTO authorities(user_id,authority) VALUES (1,'admin');
INSERT INTO users(nickname,password,first_name,last_name,email,type,birth_date,avatar) VALUES ('albperleo','1234','Alberto','Perea León','albperleo@sevenislands.com','admin','2022-10-29','avatar1.jpg');
INSERT INTO authorities(user_id,authority) VALUES (2,'admin');
INSERT INTO users(nickname,password,first_name,last_name,email,type,birth_date,avatar) VALUES ('javnunrui','dp1','Javier','Nunes Ruiz','javnunrui@sevenislands.com','admin','2022-10-29','avatar1.jpg');
INSERT INTO authorities(user_id,authority) VALUES (3,'admin');
INSERT INTO users(nickname,password,first_name,last_name,email,type,birth_date,avatar) VALUES ('marvicmar','2121','María','Vico Martín','marvicmar@sevenislands.com','admin','2022-10-29','avatar1.jpg');
INSERT INTO authorities(user_id,authority) VALUES (4,'admin');
INSERT INTO users(nickname,password,first_name,last_name,email,type,birth_date,avatar) VALUES ('juaramlop2','1234','Juan Carlos','Ramírez López','juaramlop2@sevenislands.com','admin','2022-10-29','avatar1.jpg');
INSERT INTO authorities(user_id,authority) VALUES (5,'admin');
INSERT INTO users(nickname,password,first_name,last_name,email,type,birth_date,avatar) VALUES ('alepervaz','1234','Alejandro','Pérez Vázquez','alepervaz@sevenislands.com','admin','2022-10-29','avatar1.jpg');
INSERT INTO authorities(user_id,authority) VALUES (6,'admin');

-- JUGADORES --
INSERT INTO users(nickname,password,first_name,last_name,email,type,birth_date,avatar) VALUES ('player1','player','Player FN','Player LN','player1@sevenislands.com','player','2022-10-29','avatar1.jpg');
INSERT INTO authorities(user_id,authority) VALUES (7,'player');

-- PLAYER INSERTION --
--INSERT INTO players(nickname,birth_date,avatar) VALUES ('player1','2022-10-29','avatar1.jpg');


--INSERT INTO owner(address,nickname) VALUES ('110 W. Liberty St.', 'admin1');
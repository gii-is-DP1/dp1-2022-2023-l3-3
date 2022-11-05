-- ADMINISTRADORES --
INSERT INTO users(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('admin1','4dm1n','TRUE','Admin FN','Admin LN','admin1@sevenislands.com','2022-10-29','admin','2022-10-29','avatar1.jpg');
INSERT INTO authorities(nickname,authority) VALUES ('admin1','admin');
INSERT INTO users(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('albperleo','1234','TRUE','Alberto','Perea León','albperleo@sevenislands.com','2022-10-29','admin','2022-10-29','avatar1.jpg');
INSERT INTO authorities(nickname,authority) VALUES ('albperleo','admin');
INSERT INTO users(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('javnunrui','dp1','TRUE','Javier','Nunes Ruiz','javnunrui@sevenislands.com','2022-10-29','admin','2022-10-29','avatar1.jpg');
INSERT INTO authorities(nickname,authority) VALUES ('javnunrui','admin');
INSERT INTO users(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('marvicmar','2121','TRUE','María','Vico Martín','marvicmar@sevenislands.com','2022-10-29','admin','2022-10-29','avatar1.jpg');
INSERT INTO authorities(nickname,authority) VALUES ('marvicmar','admin');
INSERT INTO users(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('juaramlop2','1234','TRUE','Juan Carlos','Ramírez López','juaramlop2@sevenislands.com','2022-10-29','admin','2022-10-29','avatar1.jpg');
INSERT INTO authorities(nickname,authority) VALUES ('juaramlop2','admin');
INSERT INTO users(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('alepervaz','1234','TRUE','Alejandro','Pérez Vázquez','alepervaz@sevenislands.com','2022-10-29','admin','2022-10-29','avatar1.jpg');
INSERT INTO authorities(nickname,authority) VALUES ('alepervaz','admin');

-- JUGADORES --
INSERT INTO users(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('player1','player','TRUE','Player FN','Player LN','player1@sevenislands.com','2022-10-29','player','2022-10-29','avatar1.jpg');
INSERT INTO authorities(nickname,authority) VALUES ('player1','player');

-- PLAYER INSERTION --
--INSERT INTO players(nickname,birth_date,avatar) VALUES ('player1','2022-10-29','avatar1.jpg');


--INSERT INTO owner(address,nickname) VALUES ('110 W. Liberty St.', 'admin1');
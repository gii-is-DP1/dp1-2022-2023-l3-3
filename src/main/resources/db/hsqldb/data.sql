-- ADMINISTRADORES (Todos tienen como contraseña "admin") --
INSERT INTO user(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('admin1','$2a$10$DXvKFs64uY6HJKVy.clI3uaJeWNvBtB.r62FrZ1x.gTcTc2jGxb5y','TRUE','Admin FN','Admin LN','admin1@sevenislands.com','2022-10-29','admin','2022-10-29','adminAvatar.png');
INSERT INTO user(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('albperleo','$2a$10$DXvKFs64uY6HJKVy.clI3uaJeWNvBtB.r62FrZ1x.gTcTc2jGxb5y','TRUE','Alberto','Perea Leon','albperleo@sevenislands.com','2022-10-29','admin','2022-10-29','adminAvatar.png');
INSERT INTO user(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('javnunrui','$2a$10$DXvKFs64uY6HJKVy.clI3uaJeWNvBtB.r62FrZ1x.gTcTc2jGxb5y','TRUE','Javier','Nunes Ruiz','javnunrui@sevenislands.com','2022-10-29','admin','2022-10-29','adminAvatar.png');
INSERT INTO user(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('marvicmar','$2a$10$DXvKFs64uY6HJKVy.clI3uaJeWNvBtB.r62FrZ1x.gTcTc2jGxb5y','TRUE','Maria','Vico Martin','marvicmar@sevenislands.com','2022-10-29','admin','2022-10-29','adminAvatar.png');
INSERT INTO user(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('juaramlop2','$2a$10$DXvKFs64uY6HJKVy.clI3uaJeWNvBtB.r62FrZ1x.gTcTc2jGxb5y','TRUE','Juan Carlos','Ramirez Lopez','juaramlop2@sevenislands.com','2022-10-29','admin','2022-10-29','adminAvatar.png');
INSERT INTO user(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('alepervaz','$2a$10$DXvKFs64uY6HJKVy.clI3uaJeWNvBtB.r62FrZ1x.gTcTc2jGxb5y','TRUE','Alejandro','Perez Vazquez','alepervaz@sevenislands.com','2022-10-29','admin','2022-10-29','adminAvatar.png');

-- JUGADORES (Todos tienen como contraseña "player") --
INSERT INTO user(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('player1','$2a$10$Yurjw7YseLzZq/JOixdY/eRmBrOIzHHNooczuuMwtC42R8vRoKVPC','TRUE','Player FN','Player LN','player1@sevenislands.com','2022-10-29','player','2022-10-29','playerAvatar.png');
INSERT INTO user(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('player2','$2a$10$Yurjw7YseLzZq/JOixdY/eRmBrOIzHHNooczuuMwtC42R8vRoKVPC','TRUE','Player FN','Player LN','player2@sevenislands.com','2022-10-29','player','2022-10-29','playerAvatar.png');
INSERT INTO user(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('player3','$2a$10$Yurjw7YseLzZq/JOixdY/eRmBrOIzHHNooczuuMwtC42R8vRoKVPC','TRUE','Player FN','Player LN','player3@sevenislands.com','2022-10-29','player','2022-10-29','playerAvatar.png');
INSERT INTO user(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('player4','$2a$10$Yurjw7YseLzZq/JOixdY/eRmBrOIzHHNooczuuMwtC42R8vRoKVPC','TRUE','Player FN','Player LN','player4@sevenislands.com','2022-10-29','player','2022-10-29','playerAvatar.png');
INSERT INTO user(nickname,password,enabled,first_name,last_name,email,creation_date,type,birth_date,avatar) VALUES ('player5','$2a$10$Yurjw7YseLzZq/JOixdY/eRmBrOIzHHNooczuuMwtC42R8vRoKVPC','TRUE','Player FN','Player LN','player5@sevenislands.com','2022-10-29','player','2022-10-29','playerAvatar.png');

-- LOBBY --
INSERT INTO lobby(code,active) VALUES('aD5f8Lio','TRUE');
INSERT INTO lobby(code,active) VALUES('a10f8Lio','FALSE');

-- LOBBY PLAYERS -- 
INSERT INTO lobby_users(lobby_id,users_id) VALUES (1,7);
INSERT INTO lobby_users(lobby_id,users_id) VALUES (1,9);
INSERT INTO lobby_users(lobby_id,users_id) VALUES (2,8);

-- -- GAME -- 
INSERT INTO game(id, creation_date, ending_date, active, lobby_id) VALUES (1, '2022-11-14', '2022-11-24', 'TRUE' ,1);

-- -- ROUNDS --
INSERT INTO round(game_id) VALUES (1);

-- -- TURNS -- 
INSERT INTO turn(user_id, round_id, start_time) VALUES (8,1, '2022-11-16 02:52:12');
INSERT INTO turn(user_id, round_id, start_time) VALUES (9,1,'2022-11-16 18:52:12');

-- -- -- CARD --
INSERT INTO treasure(name) VALUES ('Doblon'); --27
INSERT INTO treasure(name) VALUES ('Caliz'); --3
INSERT INTO treasure(name) VALUES ('Rubi'); --3
INSERT INTO treasure(name) VALUES ('Diamante'); --3
INSERT INTO treasure(name) VALUES ('Collar'); --4
INSERT INTO treasure(name) VALUES ('MapaTesoro'); --4
INSERT INTO treasure(name) VALUES ('Corona'); --4
INSERT INTO treasure(name) VALUES ('Revolver'); --6
INSERT INTO treasure(name) VALUES ('Espada'); --6
INSERT INTO treasure(name) VALUES ('BarrillRon'); --6

-- -- -- -- ISLA --
INSERT INTO island(island_number, game_id, treasure_id) VALUES (1,1,1);
INSERT INTO island(island_number, game_id, treasure_id) VALUES (2,1,2);
INSERT INTO island(island_number, game_id, treasure_id) VALUES (3,1,3);
INSERT INTO island(island_number, game_id, treasure_id) VALUES (4,1,4);
INSERT INTO island(island_number, game_id, treasure_id) VALUES (5,1,5);
INSERT INTO island(island_number, game_id, treasure_id) VALUES (6,1,6);

-- -- -- CARTA_ISLA --
-- INSERT INTO island_treasure(island_id, treasure_id) VALUES (1,1);
-- INSERT INTO island_treasure(island_id, treasure_id) VALUES (2,1);
-- INSERT INTO island_treasure(island_id, treasure_id) VALUES (3,2);
-- INSERT INTO island_treasure(island_id, treasure_id) VALUES (4,3);


-- -- -- -- -- TURNO_CARTA --
INSERT INTO turn_treasures(turn_id,treasures_id) VALUES (1, 1);
INSERT INTO turn_treasures(turn_id,treasures_id) VALUES (1, 1);
INSERT INTO turn_treasures(turn_id,treasures_id) VALUES (1, 1);

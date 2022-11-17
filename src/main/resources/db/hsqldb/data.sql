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

-- LOBBY PLAYERS -- 
INSERT INTO lobby_users(lobby_id,users_id) VALUES (1,8);
INSERT INTO lobby_users(lobby_id,users_id) VALUES (1,9);

-- -- GAME -- 
INSERT INTO game(id, creation_date, ending_date, lobby_id) VALUES (1, '2022-11-14', '2022-11-24', 1);

-- -- ROUNDS --
INSERT INTO round(game_id) VALUES (1);

-- -- TURNS -- 
INSERT INTO turn(user_id, round_id, start_time) VALUES (8,1, '2022-11-16 02:52:12');
INSERT INTO turn(user_id, round_id, start_time) VALUES (9,1,'2022-11-16 18:52:12');

-- -- -- CARD --
INSERT INTO card(tipo, name, multiplicity) VALUES ('Moneda', 'Doblon', 27);
INSERT INTO card(tipo, name, multiplicity) VALUES ('Tesoro', 'Caliz', 3);
INSERT INTO card(tipo, name, multiplicity) VALUES ('Tesoro', 'Rubi', 3);
INSERT INTO card(tipo, name, multiplicity) VALUES ('Tesoro', 'Diamante', 3);
INSERT INTO card(tipo, name, multiplicity) VALUES ('Tesoro', 'Collar', 4);
INSERT INTO card(tipo, name, multiplicity) VALUES ('Tesoro', 'MapaTesoro', 4);
INSERT INTO card(tipo, name, multiplicity) VALUES ('Tesoro', 'Corona', 4);
INSERT INTO card(tipo, name, multiplicity) VALUES ('Tesoro', 'Revolver', 6);
INSERT INTO card(tipo, name, multiplicity) VALUES ('Tesoro', 'Espada', 6);
INSERT INTO card(tipo, name, multiplicity) VALUES ('Tesoro', 'BarrillRon', 6);






-- -- -- -- ISLA --
INSERT INTO island(island_number) VALUES (1);
INSERT INTO island(island_number,game_id) VALUES (2,1);
INSERT INTO island(island_number, game_id) VALUES (3,1);
INSERT INTO island(island_number, game_id) VALUES (4,1);
INSERT INTO island(island_number, game_id) VALUES (5,1);
INSERT INTO island(island_number, game_id) VALUES (6,1);

-- -- -- CARTA_ISLA --
INSERT INTO card_islands(card_id,islands_id) VALUES (1,1);
INSERT INTO card_islands(card_id,islands_id) VALUES (1,2);
INSERT INTO card_islands(card_id,islands_id) VALUES (2,3);
INSERT INTO card_islands(card_id,islands_id) VALUES (3,4);


-- -- -- -- -- TURNO_CARTA --
INSERT INTO turn_cards(turn_id,cards_id) VALUES (1, 1);
INSERT INTO turn_cards(turn_id,cards_id) VALUES (1, 1);
INSERT INTO turn_cards(turn_id,cards_id) VALUES (1, 1);

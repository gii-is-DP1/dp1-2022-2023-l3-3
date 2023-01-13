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

-- FRIENDS --
INSERT INTO friend(user1_id, user2_id, status) VALUES (7,8, 'ACCEPTED');

-- LOBBY --
INSERT INTO lobby(code,active) VALUES('aD5f8Lio','FALSE');
INSERT INTO lobby(code,active) VALUES('a10f8Lio','FALSE');

-- LOBBY JUGADORES -- 
INSERT INTO lobby_user(lobby_id, user_id, mode) VALUES (1,7, 'PLAYER');
INSERT INTO lobby_user(lobby_id, user_id, mode) VALUES (1,8, 'PLAYER');
INSERT INTO lobby_user(lobby_id, user_id, mode) VALUES (1,9, 'PLAYER');
INSERT INTO lobby_user(lobby_id, user_id, mode) VALUES (2,10, 'PLAYER');
INSERT INTO lobby_user(lobby_id, user_id, mode) VALUES (2,7, 'PLAYER');

-- JUEGOS -- 
INSERT INTO game(id, creation_date, ending_date, active, lobby_id, winner_id, tie_break) VALUES (1, '2022-11-14 01:01:00.00000', '2022-11-14 01:20:00.00000', 'FALSE', 1, 7, 'FALSE');
INSERT INTO game(id, creation_date, ending_date, active, lobby_id, winner_id, tie_break) VALUES (2, '2022-11-09 01:01:00.00000', '2022-11-09 01:20:00.00000', 'FALSE', 2, 7, 'FALSE');

-- RONDAS --
INSERT INTO round(game_id) VALUES (1);

-- TURNOS -- 
INSERT INTO turn(user_id, round_id, start_time) VALUES (8,1, '2022-11-16 02:52:12');
INSERT INTO turn(user_id, round_id, start_time) VALUES (9,1,'2022-11-16 18:52:12');

-- CARTA --
INSERT INTO card(tipo,multiplicity,game_id) VALUES('Doblon',5,1);

-- ISLA --
INSERT INTO island(island_number, game_id, card_id) VALUES (1,1,1);
-- INSERT INTO island(island_number, game_id, card_id) VALUES (2,1,2);
-- INSERT INTO island(island_number, game_id, card_id) VALUES (3,1,3);
-- INSERT INTO island(island_number, game_id, card_id) VALUES (4,1,4);
-- INSERT INTO island(island_number, game_id, card_id) VALUES (5,1,5);
-- INSERT INTO island(island_number, game_id, card_id) VALUES (6,1,6);

-- TURNO_CARTA --
INSERT INTO turn_cards(turn_id,cards_id) VALUES (1, 1);
INSERT INTO turn_cards(turn_id,cards_id) VALUES (1, 1);
INSERT INTO turn_cards(turn_id,cards_id) VALUES (1, 1);

-- PUNTUACION --
INSERT INTO details(punctuation, game_id, user_id) VALUES (100, 1, 7);
INSERT INTO details(punctuation, game_id, user_id) VALUES (50, 1, 8);
INSERT INTO details(punctuation, game_id, user_id) VALUES (30, 1, 9);
INSERT INTO details(punctuation, game_id, user_id) VALUES (50, 2, 7);
INSERT INTO details(punctuation, game_id, user_id) VALUES (50, 2, 10);

-- LOGROS --
INSERT INTO achievement(name, description, achievement_type, threshold, badge_image) VALUES ('Inicio', 'Gana LIMIT partida', 'Victories', 1, 'logroJugarGames.png');
INSERT INTO achievement(name, description, achievement_type, threshold, badge_image) VALUES ('Veterano', 'Juega mas de LIMIT partidas', 'Games', 50, 'logroJugarGames.png');
INSERT INTO achievement(name, description, achievement_type, threshold, badge_image) VALUES ('Veterano', 'Juega mas de LIMIT partidas', 'Games', 1, 'logroJugarGames.png');
-- INSERT INTO achievement(name, description, achievement_type, threshold, badge_image) VALUES ('Poco a poco', 'Gana <LIMITE> partida', 'Victories', 1, '/resources/images/grafics/logroVictoria.png');
-- INSERT INTO achievement(name, description, achievement_type, threshold, badge_image) VALUES ('Imparable', 'Gana un total de <LIMITE> partidas', 'Victories', 20, '/resources/images/grafics/logroVictoria.png');
-- INSERT INTO achievement(name, description, achievement_type, threshold, badge_image) VALUES ('Valioso', 'Consigue el set de tesoros distintos mas grande (<LIMITE>)', 'Punctuation', 9, '/resources/images/grafics/logroPuntuacion.png');
-- INSERT INTO achievement(name, description, achievement_type, threshold, badge_image) VALUES ('Reluciente', 'Consigue todos los diamantes (<LIMITE>) en una partida', 'Punctuation', 3, '/resources/images/grafics/logroPuntuacion.png');
-- INSERT INTO achievement(name, description, achievement_type, threshold, badge_image) VALUES ('Duelo', 'Gana <LIMITE> partida por desempate por tener mas doblones que tu rival', 'Punctuation', 1, '/resources/images/grafics/logroPuntuacion.png');
-- INSERT INTO achievement(name, description, achievement_type, threshold, badge_image) VALUES ('Comerciante', 'Contrata a barcos piratas para moverte a otra isla <LIMITE> veces', '', 30, '/resources/images/grafics/logroSaltarIslas.png');
-- INSERT INTO achievement(name, description, achievement_type, threshold, badge_image) VALUES ('Afortunado', 'Obten <LIMITE> seis consecutivos', 'DiceResults', 3, '/resources/images/grafics/logroResultadosDado.png');

INSERT INTO register(acquisition_date, achievement_id, user_id) VALUES ('2022-12-29', 1, 7);
INSERT INTO register(acquisition_date, achievement_id, user_id) VALUES ('2022-12-28', 2, 7);
INSERT INTO register(acquisition_date, achievement_id, user_id) VALUES ('2022-12-28', 2, 8);
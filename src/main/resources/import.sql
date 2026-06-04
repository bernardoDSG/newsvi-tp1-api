-- ============================================
-- SEED DATA - NewsVI API (DESENVOLVIMENTO)
-- ============================================
-- Dados essenciais: gêneros, cinemas, salas, poltronas
-- Filmes, sessões e relacionamentos serão criados via API

-- 🏠 ENDEREÇOS (2 cinemas apenas)
INSERT INTO endereco (id, logradouro, numero, complemento, bairro, cidade, estado, cep) VALUES
(1, 'Avenida Teotônio Segurado', '500', 'Shopping Palmas', 'Plano Diretor Centro', 'Palmas', 'TO', '77001080'),
(2, 'Avenida Tocantins', '1200', 'Centro Comercial', 'Plano Diretor Sul', 'Palmas', 'TO', '77015500');

-- 🎬 GÊNEROS (10 gêneros principais)
INSERT INTO genero (id, nome) VALUES
(1, 'Ação'),
(2, 'Ficção Científica'),
(3, 'Drama'),
(4, 'Comédia'),
(5, 'Terror'),
(6, 'Aventura'),
(7, 'Romance'),
(8, 'Suspense'),
(9, 'Animação'),
(10, 'Documentário');

-- 🏢 CINEMAS (2 cinemas)
INSERT INTO cinema (id, nome, cnpj, telefone, endereco_id) VALUES
(1, 'Cinépolis Palmas', '12.345.678/0001-90', '(63) 3232-3232', 1),
(2, 'Cine Plaza Palmas', '98.765.432/0001-01', '(63) 3228-8888', 2);

-- 🎪 SALAS (4 salas)
INSERT INTO sala (id, numero, capacidade, cinema_id) VALUES
(1, 1, 180, 1),
(2, 2, 150, 1),
(3, 1, 200, 2),
(4, 2, 160, 2);

-- 💺 POLTRONAS
INSERT INTO poltrona (id, codigo, linha, coluna, disponibilidade, sala_id) VALUES
(1, 'A1', 'A', 1, 1, 1), (2, 'A2', 'A', 2, 1, 1), (3, 'A3', 'A', 3, 1, 1), (4, 'A4', 'A', 4, 1, 1), (5, 'A5', 'A', 5, 1, 1),
(6, 'B1', 'B', 1, 1, 1), (7, 'B2', 'B', 2, 1, 1), (8, 'B3', 'B', 3, 1, 1), (9, 'B4', 'B', 4, 1, 1), (10, 'B5', 'B', 5, 1, 1),
(11, 'C1', 'C', 1, 1, 1), (12, 'C2', 'C', 2, 1, 1), (13, 'C3', 'C', 3, 2, 1), (14, 'C4', 'C', 4, 1, 1), (15, 'C5', 'C', 5, 1, 1),
(16, 'A1', 'A', 1, 1, 2), (17, 'A2', 'A', 2, 1, 2), (18, 'A3', 'A', 3, 1, 2), (19, 'A4', 'A', 4, 1, 2), (20, 'A5', 'A', 5, 1, 2),
(21, 'B1', 'B', 1, 1, 2), (22, 'B2', 'B', 2, 1, 2), (23, 'B3', 'B', 3, 1, 2), (24, 'B4', 'B', 4, 1, 2), (25, 'B5', 'B', 5, 2, 2),
(26, 'A1', 'A', 1, 1, 3), (27, 'A2', 'A', 2, 1, 3), (28, 'A3', 'A', 3, 1, 3), (29, 'A4', 'A', 4, 1, 3), (30, 'A5', 'A', 5, 1, 3),
(31, 'B1', 'B', 1, 1, 3), (32, 'B2', 'B', 2, 1, 3), (33, 'B3', 'B', 3, 2, 3), (34, 'B4', 'B', 4, 1, 3), (35, 'B5', 'B', 5, 1, 3),
(36, 'A1', 'A', 1, 1, 4), (37, 'A2', 'A', 2, 1, 4), (38, 'A3', 'A', 3, 1, 4), (39, 'A4', 'A', 4, 1, 4), (40, 'A5', 'A', 5, 1, 4),
(41, 'B1', 'B', 1, 1, 4), (42, 'B2', 'B', 2, 1, 4), (43, 'B3', 'B', 3, 1, 4), (44, 'B4', 'B', 4, 1, 4), (45, 'B5', 'B', 5, 1, 4);

-- ============================================
-- SEQUÊNCIAS
-- ============================================
ALTER SEQUENCE endereco_id_seq RESTART WITH 3;
ALTER SEQUENCE genero_id_seq RESTART WITH 11;
ALTER SEQUENCE cinema_id_seq RESTART WITH 3;
ALTER SEQUENCE sala_id_seq RESTART WITH 5;
ALTER SEQUENCE poltrona_id_seq RESTART WITH 46;

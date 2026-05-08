-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;

-- Usuario admin de teste (senha: admin123)
insert into usuario (login, senha_hash, perfil)
values ('admin', '$2a$10$TkB2wCdCdgmiy.Z/q3GSIuOT7QEiSk4kzKYxvzk8UC2CO2TgF3CMe', 'ADMIN');

-- Usuario cliente de teste (senha: cliente123)
insert into usuario (nome, email, login, senha_hash, perfil)
values ('Cliente Teste', 'cliente@newsvi.com', 'cliente', '$2a$10$yOt8fYaD3.fGdWs2KZ1a7eB8qTZ.5JgHRB2xroUoPzocHiBewZpQu', 'CLIENTE');


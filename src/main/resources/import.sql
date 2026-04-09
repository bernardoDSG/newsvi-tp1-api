-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;

insert into premio (nome,ano,categoria) values('Oscar',2014,'Melhor Filme');
insert into premio (nome,ano,categoria) values('British Academy Film Awards',2014,'Melhor Ator');
insert into premio (nome,ano,categoria) values('Oscar',2020,'Melhor Ator Coadjuvante');

insert into genero (nome) values('Drama');
insert into genero (nome) values('Histórico');


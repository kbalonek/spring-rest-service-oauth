insert into user(id, name, login, password) values (1,'User and Admin','user_and_admin','spring');
insert into user(id, name, login, password) values (2,'User','user','spring');
insert into user(id, name, login, password) values (3,'Admin','admin','spring');
 
insert into role(id, name) values (1,'ROLE_USER');
insert into role(id, name) values (2,'ROLE_ADMIN');

insert into user_role(user_id, role_id) values (1,1);
insert into user_role(user_id, role_id) values (1,2);
insert into user_role(user_id, role_id) values (2,1);
insert into user_role(user_id, role_id) values (3,2);

create database gamePlay;

use gamePlay;

create table user (
	id int primary key auto_increment,
    username varchar(50),
    name varchar(255)
);

create table user_game (
	id int primary key auto_increment,
    user_id int,
    game_type varchar(255),
    total_game int,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

create table random_name (
	id int primary key auto_increment,
    name varchar(255)
);


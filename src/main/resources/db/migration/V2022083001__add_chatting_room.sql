create table chatting_room
(
    chatting_room_id   bigint not null auto_increment,
    dtype              varchar(255) not null,
    created_date       datetime(6),
    last_modified_date datetime(6),
    primary key (chatting_room_id)
) engine=InnoDB;
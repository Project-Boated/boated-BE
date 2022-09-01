create table project_chatting_room
(
    chatting_room_id bigint not null,
    project_id       bigint not null,
    primary key (chatting_room_id),
    constraint fk_project_chatting_room_chatting_room foreign key (chatting_room_id)
        references chatting_room (chatting_room_id),
    constraint fk_project_chatting_room_project foreign key (project_id)
        references project (project_id)
) engine=InnoDB;
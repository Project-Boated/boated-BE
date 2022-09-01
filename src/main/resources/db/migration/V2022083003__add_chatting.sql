create table chatting
(
    chatting_id              bigint not null auto_increment,
    project_chatting_room_id bigint not null,
    account_id               bigint not null,
    created_date             datetime(6),
    last_modified_date       datetime(6),
    body                     varchar(255),
    primary key (chatting_id),
    constraint fk_chatting_project_chatting_room foreign key (project_chatting_room_id)
        references project_chatting_room (chatting_room_id),
    constraint fk_chatting_account foreign key (account_id)
        references account (account_id)
) engine=InnoDB;
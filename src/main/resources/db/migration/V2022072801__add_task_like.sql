create table task_like
(
    task_like_id       bigint not null auto_increment,
    account_id         bigint,
    task_id            bigint,
    created_date       datetime(6),
    last_modified_date datetime(6),
    primary key (task_like_id),
    constraint fk_task_like_account foreign key (account_id)
        references account (account_id),
    constraint fk_task_like_task foreign key (task_id)
        references task (task_id)
) engine=InnoDB;
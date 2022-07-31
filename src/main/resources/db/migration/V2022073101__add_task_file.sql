create table task_file
(
    task_file_id       bigint not null auto_increment,
    task_id            bigint,
    upload_file_id     bigint,
    created_date       datetime(6),
    last_modified_date datetime(6),
    primary key (task_file_id),
    constraint fk_task_file_task foreign key (task_id)
        references task (task_id),
    constraint fk_task_file_upload_file foreign key (upload_file_id)
        references upload_file (upload_file_id)
) engine=InnoDB;
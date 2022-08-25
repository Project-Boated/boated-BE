create table project_video
(
    project_video_id   bigint not null auto_increment,
    project_id         bigint,
    upload_file_id     bigint,
    created_date       datetime(6),
    last_modified_date datetime(6),
    primary key (project_video_id),
    constraint fk_project_video_project foreign key (project_id)
        references project (project_id),
    constraint fk_project_video_upload_file foreign key (upload_file_id)
        references upload_file (upload_file_id)
) engine=InnoDB;
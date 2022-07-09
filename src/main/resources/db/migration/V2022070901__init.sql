create table upload_file
(
    upload_file_id     bigint not null auto_increment,
    original_file_name varchar(255),
    save_file_name     varchar(255),
    ext                varchar(255),
    media_type         varchar(255),
    primary key (upload_file_id)
) engine=InnoDB;

create table profile_image
(
    profile_image_id bigint       not null auto_increment,
    upload_file_id   bigint,
    url              varchar(255),
    image_type       varchar(255) not null,
    primary key (profile_image_id),
    constraint fk_profile_image_upload_file foreign key (upload_file_id)
        references upload_file (upload_file_id)
) engine=InnoDB;

create table account
(
    oauth_site         varchar(255),
    account_id         bigint not null auto_increment,
    nickname           varchar(255),
    password           varchar(255),
    username           varchar(255),
    oauth_id           bigint,
    profile_image_id   bigint,
    created_date       datetime(6),
    last_modified_date datetime(6),
    primary key (account_id),
    constraint fk_account_profile_image foreign key (profile_image_id)
        references profile_image (profile_image_id),
    constraint uk_account_nickname unique (nickname),
    constraint uk_account_username unique (username),
    constraint uk_account_oauth_id unique (oauth_id)
) engine=InnoDB;

create table account_role
(
    account_id bigint not null,
    roles      varchar(255),
    constraint fk_account_role_account foreign key (account_id)
        references account (account_id)
) engine=InnoDB;

create table project
(
    project_id         bigint not null auto_increment,
    account_id_captain bigint,
    name               varchar(255),
    description        varchar(255),
    deadline           datetime(6),
    is_terminated      bit    not null,
    created_date       datetime(6),
    last_modified_date datetime(6),
    primary key (project_id),
    constraint fk_project_account foreign key (account_id_captain)
        references account (account_id)
) engine=InnoDB;

create table account_project
(
    account_project_id bigint not null auto_increment,
    account_id         bigint,
    project_id         bigint,
    created_date       datetime(6),
    last_modified_date datetime(6),
    primary key (account_project_id),
    constraint fk_account_project_account foreign key (account_id)
        references account (account_id),
    constraint fk_account_project_project foreign key (project_id)
        references project (project_id)
) engine=InnoDB;

create table invitation
(
    invitation_id      bigint not null auto_increment,
    account_id         bigint,
    project_id         bigint,
    created_date       datetime(6),
    last_modified_date datetime(6),
    primary key (invitation_id),
    constraint fk_invitation_account foreign key (account_id)
        references account (account_id),
    constraint fk_invitation_project foreign key (project_id)
        references project (project_id)
) engine=InnoDB;

create table kanban
(
    kanban_id          bigint not null auto_increment,
    project_id         bigint,
    created_date       datetime(6),
    last_modified_date datetime(6),
    primary key (kanban_id),
    constraint fk_kanban_project foreign key (project_id)
        references project (project_id)
) engine=InnoDB;

create table kanban_lane
(
    kanban_lane_id     bigint       not null auto_increment,
    kanban_id          bigint,
    project_id         bigint,
    name               varchar(255),
    type               varchar(255) not null,
    created_date       datetime(6),
    last_modified_date datetime(6),
    primary key (kanban_lane_id),
    constraint fk_kanban_lane_kanban foreign key (kanban_id)
        references kanban (kanban_id),
    constraint fk_kanban_lane_project foreign key (project_id)
        references project (project_id)
) engine=InnoDB;

create table task
(
    task_id            bigint not null auto_increment,
    kanban_lane_id     bigint,
    project_id         bigint,
    name               varchar(255),
    description        varchar(255),
    deadline           datetime(6),
    created_date       datetime(6),
    last_modified_date datetime(6),
    primary key (task_id),
    constraint fk_task_kanban_lane foreign key (kanban_lane_id)
        references kanban_lane (kanban_lane_id),
    constraint fk_task_project foreign key (project_id)
        references project (project_id)
) engine=InnoDB;

create table account_task
(
    account_task_id    bigint not null auto_increment,
    account_id         bigint,
    task_id            bigint,
    created_date       datetime(6),
    last_modified_date datetime(6),
    primary key (account_task_id),
    constraint fk_account_task_account foreign key (account_id)
        references account (account_id),
    constraint fk_account_task_task foreign key (task_id)
        references task (task_id)
) engine=InnoDB;
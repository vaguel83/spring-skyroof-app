    create table project (
       project_id bigint not null auto_increment,
        creation_date datetime not null,
        description varchar(2000) not null,
        title varchar(150) not null,
        primary key (project_id)
    ) engine=MyISAM;

    create table task (
       task_id bigint not null auto_increment,
        creation_date datetime not null,
        completed_date date not null,
        description varchar(2000) not null,
        project_id bigint not null,
        start_date date not null,
        state varchar(11) not null,
        title varchar(150) not null,
        primary key (task_id)
    ) engine=MyISAM;

    alter table task 
       add constraint project_id_secondary_key
       foreign key (project_id) 
       references project (project_id);

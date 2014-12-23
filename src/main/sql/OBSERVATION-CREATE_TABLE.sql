    alter table ginga.OBS_LACDUMP_BG 
        drop 
        foreign key FK_lika0a47g0bf6ebvp47p94ylp;

    alter table ginga.OBS_LACDUMP_DATA 
        drop 
        foreign key FK_d0a7ya3s4xip4pw7pjboil72u;

    drop table if exists ginga.OBSERVATION;

    drop table if exists ginga.OBS_LACDUMP_BG;

    drop table if exists ginga.OBS_LACDUMP_DATA;

    create table ginga.OBSERVATION (
        OBSERVATION_ID bigint not null auto_increment,
        AVE_ALPHA float,
        AVE_DELTA float,
        DISTANCE float,
        ENDTIME datetime,
        FLAG varchar(4),
        MAX_ALPHA float,
        MAX_DELTA float,
        MIN_ALPHA float,
        MIN_DELTA float,
        NUM_PASSES integer,
        PASS_NAMES longtext,
        POINTING float,
        SEQ_NO integer,
        STARTTIME datetime,
        TARGET_NAME varchar(80),
        primary key (OBSERVATION_ID)
    ) ENGINE=InnoDB;

    create table ginga.OBS_LACDUMP_BG (
        ID bigint not null auto_increment,
        LACDUMP_FILE varchar(7) not null,
        OBSERVATION_ID bigint not null,
        primary key (ID)
    ) ENGINE=InnoDB;

    create table ginga.OBS_LACDUMP_DATA (
        ID bigint not null auto_increment,
        LACDUMP_FILE varchar(7) not null,
        OBSERVATION_ID bigint not null,
        primary key (ID)
    ) ENGINE=InnoDB;

    alter table ginga.OBS_LACDUMP_BG 
        add constraint FK_lika0a47g0bf6ebvp47p94ylp 
        foreign key (OBSERVATION_ID) 
        references ginga.OBSERVATION (OBSERVATION_ID);

    alter table ginga.OBS_LACDUMP_DATA 
        add constraint FK_d0a7ya3s4xip4pw7pjboil72u 
        foreign key (OBSERVATION_ID) 
        references ginga.OBSERVATION (OBSERVATION_ID);
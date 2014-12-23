    alter table ginga.OBSERVATION_BG 
        drop 
        foreign key FK_d5x74c0k75wlf4cwj8e3qsr07;

    alter table ginga.OBSERVATION_DATA 
        drop 
        foreign key FK_ddyoxdmn7liq4vlour0ie4k9x;

    drop table if exists ginga.OBSERVATION;

    drop table if exists ginga.OBSERVATION_BG;

    drop table if exists ginga.OBSERVATION_DATA;

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

    create table ginga.OBSERVATION_BG (
        ID bigint not null auto_increment,
        LACDUMP_FILE varchar(7) not null,
        OBSERVATION_ID bigint not null,
        primary key (ID)
    ) ENGINE=InnoDB;

    create table ginga.OBSERVATION_DATA (
        ID bigint not null auto_increment,
        LACDUMP_FILE varchar(7) not null,
        OBSERVATION_ID bigint not null,
        primary key (ID)
    ) ENGINE=InnoDB;

    alter table ginga.OBSERVATION_BG 
        add constraint FK_d5x74c0k75wlf4cwj8e3qsr07 
        foreign key (OBSERVATION_ID) 
        references ginga.OBSERVATION (OBSERVATION_ID);

    alter table ginga.OBSERVATION_DATA 
        add constraint FK_ddyoxdmn7liq4vlour0ie4k9x 
        foreign key (OBSERVATION_ID) 
        references ginga.OBSERVATION (OBSERVATION_ID);
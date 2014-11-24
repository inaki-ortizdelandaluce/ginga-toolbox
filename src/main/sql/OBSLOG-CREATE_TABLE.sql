   drop table if exists ginga.OBSLOG;

    create table ginga.OBSLOG (
        ID bigint not null auto_increment,
        AVE_ALPHA float not null,
        AVE_DELTA float not null,
        DISTANCE float not null,
        ENDTIME datetime not null,
        FLAG varchar(4) not null,
        MAX_ALPHA float not null,
        MAX_DELTA float not null,
        MIN_ALPHA float not null,
        MIN_DELTA float not null,
        NUM_PASSES integer not null,
        PASS_NAMES varchar(100),
        POINTING float,
        SEQ_NO integer not null,
        STARTTIME datetime not null,
        TARGET_NAME varchar(80) not null,
        primary key (ID)
    ) ENGINE=InnoDB;
    drop table if exists ginga.OBSLOG;

    create table ginga.OBSLOG (
        ID bigint not null auto_increment,
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
        primary key (ID)
    ) ENGINE=InnoDB;
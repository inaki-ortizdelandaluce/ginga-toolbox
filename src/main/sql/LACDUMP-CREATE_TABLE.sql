    drop table if exists ginga.LACDUMP;

    create table ginga.LACDUMP (
        ID bigint not null auto_increment,
        PIMN double precision,
        SUD double precision,
        ACM enum('NML', 'SL+','SL-','S36','MAN'),
        BR enum('H', 'M', 'L'),
        RIG double precision,
        DATE datetime not null,
        DEC_DEG_B1950 double precision,
        S_E enum('SKY','NTE','DYE'),
        EELV double precision,
        GMU varchar(3),
        LAC_H double precision,
        LAC_L double precision,
        MODE enum('MPC1', 'MPC2', 'MPC3', 'ACS', 'PCHK'),
        RA_DEG_B1950 double precision,
        SEQ_NO integer not null,
        SPIN_AXIS_DEC_DEG double precision,
        SPIN_AXIS_RA_DEG double precision,
        SF varchar(12) not null,
        TARGET varchar(255),
        TRANSMISSION double precision,
        primary key (ID),
        KEY `idx_LACDUMP_DATE` (`DATE`),
        KEY `idx_LACDUMP_TARGET` (`TARGET`)
    ) ENGINE=InnoDB;
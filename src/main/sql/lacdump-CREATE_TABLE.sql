   drop table if exists ginga.lacdump;

    create table ginga.lacdump (
        id bigint not null auto_increment,
        pimn double precision,
        suf double precision,
        acm varchar(3),
        br varchar(1),
        rig double precision,
        date datetime not null,
        dec_deg_b1950 double precision,
        s_e varchar(3),
        eelv double precision,
        gmu varchar(3),
        lac_h double precision,
        lac_l double precision,
        mode varchar(4),
        ra_deg_b1950 double precision,
        seq_no integer not null,
        spin_axis_dec_deg double precision,
        spin_axis_ra_deg double precision,
        sf varchar(12) not null,
        target varchar(255),
        transmission double precision,
        primary key (id)
    ) ENGINE=InnoDB;
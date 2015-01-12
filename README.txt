1. Build software
mvn clean package jar:jar appassembler:assemble assembly:assembly

2. Install software

3. Build database

3.1 Create database schema

> DatabaseSchemaGeneratorCmd

3.2 Populate observations table

mysql -u root -p
Enter password: 
mysql> LOAD DATA LOCAL INFILE 'ginga_obs.dump'
    -> INTO TABLE ginga.OBSERVATION
    -> FIELDS TERMINATED BY '|'
    -> LINES TERMINATED BY '\n'
    -> IGNORE 15 LINES
    -> (SEQ_NO, TARGET_NAME, AVE_ALPHA, AVE_DELTA, MAX_ALPHA, MAX_DELTA, MIN_ALPHA, MIN_DELTA, RIKAKU, @start_time_variable, @end_time_variable, POINTING)   
    -> SET STARTTIME=STR_TO_DATE(@start_time_variable,'%d-%b-%Y'), ENDTIME=STR_TO_DATE(@end_time_variable,'%d-%b-%Y');
Query OK, 7318 rows affected, 53364 warnings (0.10 sec)

3.3 Populate LACDUMP table

> LacdumpDatabaseIngestorCmd

3.4 Populate suggested background observations table

> ObservationBgDatabaseIngestor

3.5. Create database indexes

LACDUMP indexes: idx_LACDUMP_DATE and idx_LACDUMP_TARGET (BTREE, Non-unique)

4. Examples

./print_target_list.sh --file /tmp/ginga_targets.txt
./lookup_target.sh --target "VELA X-1"
./resolve_target_name.sh --target GS2000+25

./print_observation_list.sh --target GS2000+25 --spectral-modes-only --file /tmp/GS2000+25_observations_spec.txt --systematic
./print_observation_list.sh -t GS2000+25 -f /tmp/GS2000+25_observations_spec.txt -a -s
./print_observation_list.sh -t GS2000+25 -f /tmp/GS2000+25_observations_spec.txt -a -i

[Edit ginga-toolbox.properties]

./extract_spectrum.sh --systematic --target "VELA X-1" --mode MPC2 --start-time 1988-02-22T14:31:07 --end-time 1988-02-23T03:51:35 --observation-id 1274 --background-method HAYASHIDA

./extract_spectrum.sh --systematic --target "VELA X-1" --mode MPC2 --start-time 1988-02-22T14:31:07 --end-time 1988-02-23T03:51:35 --observation-id 1274 --background-method SIMPLE

./extract_spectrum.sh --systematic --target "VELA X-1" --mode MPC2 --start-time 1988-02-22T14:31:07 --end-time 1988-02-23T03:51:35 --observation-id 1274 --background-method SUD_SORT

./extract_spectra.sh --target GS2000+25 --background-subtraction HAYASHIDA

./extract_spectra.sh -t GS2000+25 -b SUD_SORT

./extract_timing_mode_1.sh --systematic --target "VELA X-1" --mode MPC3 --start-time 1988-02-23T14:37:43  --end-time 1988-02-23T16:19:11 --observation-id 1275 --background-method HAYASHIDA

./extract_timing_mode_1.sh --systematic --target "VELA X-1" --mode MPC3 --start-time 1988-02-23T14:37:43  --end-time 1988-02-23T16:19:11 --observation-id 1275 --background-method SIMPLE

./extract_timing_mode_1.sh --systematic --target "VELA X-1" --mode MPC3 --start-time 1988-02-23T14:37:43  --end-time 1988-02-23T16:19:11 --observation-id 1275 --background-method SUD_SORT

./extract_timing_mode_2.sh --systematic --target 4U1636-53 --observation-id 657 --start-time 1987-08-20T04:55:41 --end-time  1987-08-20T11:44:57

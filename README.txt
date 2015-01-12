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





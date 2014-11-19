LOAD DATA LOCAL INFILE 'ginga_obs.dump'  
INTO TABLE ginga.ginga_obs 
FIELDS TERMINATED BY '|' 
LINES TERMINATED BY '\n'  
IGNORE 15 LINES 
(SEQ_NO, TARGET_NAME, AVE_ALPHA, AVE_DELTA, MAX_ALPHA, MAX_DELTA, MIN_ALPHA, MIN_DELTA, RIKAKU, @start_time_variable, @end_time_variable, POINTING)   
SET STARTTIME=STR_TO_DATE(@start_time_variable,'%d-%b-%Y'), ENDTIME=STR_TO_DATE(@end_time_variable,'%d-%b-%Y');

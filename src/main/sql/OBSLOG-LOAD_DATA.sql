LOAD DATA LOCAL INFILE 'OBSLOG'  
INTO TABLE ginga.OBSLOG
FIELDS TERMINATED BY '|' 
LINES TERMINATED BY '\n'  
IGNORE 1 LINES 
(SEQ_NO, NUM_PASSES, @target_name_variable, AVE_ALPHA, AVE_DELTA, MAX_ALPHA, MAX_DELTA, MIN_ALPHA, MIN_DELTA, @flag_variable, DISTANCE, @start_date_variable, @start_time_variable, @end_date_variable, @end_time_variable, POINTING, @pass_names_variable)   
SET TARGET_NAME=TRIM(@target_name_variable), FLAG=TRIM(@flag_variable), STARTTIME=STR_TO_DATE(CONCAT_WS(' ', @start_date_variable, @start_time_variable),'%y%m%d %H:%i:%s'), ENDTIME=STR_TO_DATE(CONCAT_WS(' ', @end_date_variable, @end_time_variable),'%y%m%d %H:%i:%s'), PASS_NAMES=TRIM(@pass_names_variable);

-- recalcul de index
exec dbms_stats.gather_schema_stats(ownname => 'EPG',cascade => TRUE,method_opt => 'FOR ALL COLUMNS SIZE AUTO' );
exec dbms_stats.gather_table_stats(ownname=>'EPG',tabname=>'HIERARCHY',estimate_percent => 100,cascade=>true,method_opt=>'FOR ALL COLUMNS size skewonly');

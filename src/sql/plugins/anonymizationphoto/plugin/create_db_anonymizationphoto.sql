-- liquibase formatted sql
-- changeset anonymizationphoto:create_db_anonymizationphoto.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM anonymizationphoto_stats WHERE 1 = 0
DROP TABLE IF EXISTS anonymizationphoto_stats;

-- Creation table anonymizationphoto_stats
CREATE TABLE IF NOT EXISTS anonymizationphoto_stats (
  id_stats int8 NOT NULL,
  id_resource_anonymized int8,
  date_anonymization timestamp,
  elapsed_time_in_seconds varchar(50),
  faces_detected int,
  PRIMARY KEY (id_stats)
);

-- changeset anonymizationphoto:create_db_anonymizationphoto-rev1.sql dbms:mariadb,mysql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = database() AND table_name = 'anonymizationphoto_stats' AND column_name = 'elapsed_time_in_seconds' AND character_maximum_length < 50
ALTER TABLE anonymizationphoto_stats MODIFY elapsed_time_in_seconds varchar(50);

-- changeset anonymizationphoto:create_db_anonymizationphoto-rev2.sql dbms:postgresql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = current_schema() AND table_name = 'anonymizationphoto_stats' AND column_name = 'elapsed_time_in_seconds' AND character_maximum_length < 50
ALTER TABLE anonymizationphoto_stats ALTER COLUMN elapsed_time_in_seconds TYPE varchar(50);

-- liquibase formatted sql
-- changeset anonymizationphoto:update_db_anonymizationphoto-1.0.0-2.0.0.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM core_admin_right WHERE id_right = 'STAT_ANONYMIZATION_MANAGEMENT' AND icon_url IS NULL
UPDATE core_admin_right SET icon_url = 'ti ti-face-id' WHERE id_right = 'STAT_ANONYMIZATION_MANAGEMENT';

-- changeset anonymizationphoto:update_db_anonymizationphoto-1.0.0-2.0.0-rev1.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM core_admin_right WHERE id_right = 'STAT_ANONYMIZATION_MANAGEMENT' AND id_feature_group IS NULL
UPDATE core_admin_right SET id_feature_group = 'APPLICATIONS' WHERE id_right = 'STAT_ANONYMIZATION_MANAGEMENT';

-- changeset anonymizationphoto:update_db_anonymizationphoto-1.0.0-2.0.0-rev2.sql dbms:mariadb,mysql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = database() AND table_name = 'anonymizationphoto_stats' AND column_name = 'elapsed_time_in_seconds' AND character_maximum_length < 50
ALTER TABLE anonymizationphoto_stats MODIFY elapsed_time_in_seconds varchar(50);

-- changeset anonymizationphoto:update_db_anonymizationphoto-1.0.0-2.0.0-rev3.sql dbms:postgresql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = current_schema() AND table_name = 'anonymizationphoto_stats' AND column_name = 'elapsed_time_in_seconds' AND character_maximum_length < 50
ALTER TABLE anonymizationphoto_stats ALTER COLUMN elapsed_time_in_seconds TYPE varchar(50);

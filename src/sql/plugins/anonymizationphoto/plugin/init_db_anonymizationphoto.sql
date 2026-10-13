-- liquibase formatted sql
-- changeset anonymizationphoto:init_db_anonymizationphoto.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM core_admin_right WHERE id_right = 'STAT_ANONYMIZATION_MANAGEMENT'
-- activation du plugin anonymizationphoto
INSERT INTO core_datastore (entity_key, entity_value) VALUES('core.plugins.status.anonymizationphoto.installed', 'true');
INSERT INTO core_datastore (entity_key, entity_value) VALUES('core.plugins.status.anonymizationphoto.pool', 'portal');

--
-- Data for table core_admin_right
--
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('STAT_ANONYMIZATION_MANAGEMENT','anonymizationphoto.adminFeature.stat.anonymizationphoto.name',2,'jsp/admin/plugins/anonymizationphoto/StatAnonymizationPhoto.jsp','anonymizationphoto.adminFeature.stat.anonymizationphoto.description',0,'anonymizationphoto','APPLICATIONS','ti ti-face-id',NULL,4);

--
-- Data for table core_user_right
--
INSERT INTO core_user_right (id_right,id_user) VALUES ('STAT_ANONYMIZATION_MANAGEMENT',1);

-- changeset anonymizationphoto:init_db_anonymizationphoto-rev1.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM core_admin_right WHERE id_right = 'STAT_ANONYMIZATION_MANAGEMENT' AND ( icon_url IS NULL OR id_feature_group IS NULL )
UPDATE core_admin_right SET icon_url = COALESCE( icon_url, 'ti ti-face-id' ), id_feature_group = COALESCE( id_feature_group, 'APPLICATIONS' ) WHERE id_right = 'STAT_ANONYMIZATION_MANAGEMENT';

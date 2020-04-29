-- activation du plugin anonymizationphoto
INSERT INTO core_datastore (entity_key, entity_value) VALUES('core.plugins.status.anonymizationphoto.installed', 'true');
INSERT INTO core_datastore (entity_key, entity_value) VALUES('core.plugins.status.anonymizationphoto.pool', 'portal');

--
-- Data for table core_admin_right
--
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('STAT_ANONYMIZATION_MANAGEMENT','anonymizationphoto.adminFeature.stat.anonymizationphoto.name',2,'jsp/admin/plugins/anonymizationphoto/StatAnonymizationPhoto.jsp','anonymizationphoto.adminFeature.stat.anonymizationphoto.description',0,'anonymizationphoto',NULL,NULL,NULL,4);

--
-- Data for table core_user_right
--
INSERT INTO core_user_right (id_right,id_user) VALUES ('STAT_ANONYMIZATION_MANAGEMENT',1);
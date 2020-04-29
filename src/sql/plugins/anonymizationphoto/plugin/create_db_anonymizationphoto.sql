DROP TABLE IF EXISTS anonymizationphoto_stats;

-- Creation table anonymizationphoto_stats
CREATE SEQUENCE seq_anonymizationphoto_stats_id;
CREATE TABLE IF NOT EXISTS anonymizationphoto_stats (
  id_stats int8 NOT NULL,
  id_resource_anonymized int8,
  date_anonymization timestamp,
  elapsed_time_in_seconds varchar(10),
  faces_detected int,
  PRIMARY KEY (id_stats)
);
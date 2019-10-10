CREATE KEYSPACE time_series_raw_data WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 3};
CREATE TABLE time_series_raw_data.data (
	data_name text,
	data_value decimal,
	device_id bigint,
	data_release_date TIMESTAMP,
	data_release_year int,
	PRIMARY KEY((device_id, data_name, data_release_year), data_release_date) 
) WITH CLUSTERING ORDER BY (data_release_date DESC);

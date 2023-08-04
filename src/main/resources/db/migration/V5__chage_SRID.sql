ALTER TABLE store
    ADD location_new_srid POINT SRID 5179 NOT NULL;

UPDATE store
SET location_new_srid = ST_PointFromText(ST_AsText(location), 5179);

ALTER TABLE store
    DROP location;

ALTER TABLE store
    CHANGE location_new_srid location POINT SRID 5179 NOT NULL;


ALTER TABLE crew
    ADD location_new_srid POINT SRID 5179 NOT NULL;

UPDATE crew
SET location_new_srid = ST_PointFromText(ST_AsText(location), 5179);

ALTER TABLE crew
    DROP location;

ALTER TABLE crew
    CHANGE location_new_srid location POINT SRID 5179 NOT NULL;
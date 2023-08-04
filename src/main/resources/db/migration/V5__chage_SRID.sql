ALTER TABLE store
    ADD location_srid POINT SRID 5179 NULL;

UPDATE store
SET location_srid = ST_PointFromText(ST_AsText(location), 5179);

ALTER TABLE store
    DROP location;

ALTER TABLE store
    CHANGE location_srid location POINT SRID 5179 NOT NULL;


ALTER TABLE crew
    ADD location_srid POINT SRID 5179 NULL;

UPDATE crew
SET location_srid = ST_PointFromText(ST_AsText(location), 5179);

ALTER TABLE crew
    DROP location;

ALTER TABLE crew
    CHANGE location_srid location POINT SRID 5179 NOT NULL;
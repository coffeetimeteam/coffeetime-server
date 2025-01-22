ALTER TABLE image
    MODIFY COLUMN status enum ('USABLE', 'DELETED', 'TEMPORARY') not null;
ALTER TABLE image
    MODIFY coffee_id bigint null;
ALTER TABLE image
    MODIFY deleted_at datetime(6) null;
ALTER TABLE image
    CHANGE COLUMN name url varchar(255) not null unique;
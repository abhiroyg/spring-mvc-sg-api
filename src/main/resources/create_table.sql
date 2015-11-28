\W
USE societegenerale;
SET NAMES 'utf8';
DROP TABLE IF EXISTS SGMembers;
CREATE TABLE SGMembers (
    id INT NOT NULL UNIQUE,
    status VARCHAR(250),
    race VARCHAR(50),
    weight NUMERIC,
    height NUMERIC,
    is_veg TINYINT
);
\w
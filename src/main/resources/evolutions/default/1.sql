# First database schema

# --- !Ups
CREATE TABLE users(
  user_id      BIGINT       UNSIGNED AUTO_INCREMENT NOT NULL,
  username     VARCHAR(255) COLLATE utf8_bin NOT NULL,
  email        VARCHAR(255) COLLATE utf8_bin NOT NULL,
  password     VARCHAR(45)  COLLATE utf8_bin NOT NULL,
  salt         VARCHAR(45)  COLLATE utf8_bin DEFAULT NULL,
  is_inactive   BOOL         DEFAULT FALSE,
  PRIMARY KEY (user_id),
  UNIQUE KEY email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

# --- !Downs
DROP TABLE users;
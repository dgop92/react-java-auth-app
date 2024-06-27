CREATE SEQUENCE  IF NOT EXISTS app_user_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE app_user (
  id BIGINT NOT NULL,
   auth_user_id VARCHAR(100) NOT NULL,
   email VARCHAR(320) NOT NULL,
   first_name VARCHAR(50) NOT NULL,
   last_name VARCHAR(80) NOT NULL,
   created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   deleted_at TIMESTAMP WITHOUT TIME ZONE,
   CONSTRAINT pk_app_user PRIMARY KEY (id)
);

ALTER TABLE app_user ADD CONSTRAINT auth_user_id_unique UNIQUE (auth_user_id);

ALTER TABLE app_user ADD CONSTRAINT email_unique UNIQUE (email, deleted_at);
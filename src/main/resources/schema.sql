CREATE TABLE address (
  id  BIGINT NOT NULL,
  suburb VARCHAR2(255 CHAR) NOT NULL,
  state VARCHAR2(255 CHAR) NOT NULL,
  postcode INT NOT NULL,
  latitude DOUBLE,
  longitude DOUBLE,
  created_on TIMESTAMP(6) NOT NULL,
  updated_on TIMESTAMP(6),
  CONSTRAINT address_pk PRIMARY KEY (id),
  CONSTRAINT idx_suburb INDEX (suburb),
  CONSTRAINT idx_postcode INDEX (postcode)
);
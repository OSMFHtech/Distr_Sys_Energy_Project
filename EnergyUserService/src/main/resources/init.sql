CREATE TABLE user_profiles (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255),
  email VARCHAR(255)
);
CREATE TABLE consumption_record (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT,
  kwh DOUBLE,
  datetime TIMESTAMP
);

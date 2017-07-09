
INSERT INTO address (postcode, suburb, state, latitude, longitude, created_on) VALUES
  ('200', 'AUSTRALIAN NATIONAL UNIVERSITY', 'ACT', -35.277272, 149.117136, CURRENT_TIMESTAMP),
  ('221', 'BARTON', 'ACT', -35.201372, 149.095065, CURRENT_TIMESTAMP),
  ('800', 'DARWIN', 'NT', -12.801028, 130.955789, CURRENT_TIMESTAMP)
  --('', '', '', , , CURRENT_TIMESTAMP),
  ;


INSERT INTO user (username, password, role, created_on) VALUES
  ('user', '$2a$10$ggOZSoLpYVNpmL0M9xVtp.e.cvk2copyXcYEZO4P8zhMOTrYqIGMO', 'user', CURRENT_TIMESTAMP),
  ('admin', '$2a$10$ggOZSoLpYVNpmL0M9xVtp.e.cvk2copyXcYEZO4P8zhMOTrYqIGMO', 'admin', CURRENT_TIMESTAMP)
  ;



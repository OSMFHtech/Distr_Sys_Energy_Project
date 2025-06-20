INSERT INTO production_record (producer_id, kwh, datetime) VALUES
                                                               (1, 0.005, CURRENT_TIMESTAMP),
                                                               (2, 0.007, DATEADD('MINUTE', -1, CURRENT_TIMESTAMP));
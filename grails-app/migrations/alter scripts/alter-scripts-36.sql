INSERT INTO reason (version, description, identifier) VALUES (0, "default", "Default");

UPDATE credit_memo cm, reason r SET cm.reason_id = r.id WHERE r.identifier = "Default";

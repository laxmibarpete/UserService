ALTER TABLE userservice.`role`
    ADD is_deleted BIT(1) DEFAULT 0 NULL;

ALTER TABLE userservice.token
    ADD is_deleted BIT(1) DEFAULT 0 NULL;

ALTER TABLE userservice.user
    ADD is_deleted BIT(1) DEFAULT 0 NULL;
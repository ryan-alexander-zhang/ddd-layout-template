CREATE TABLE IF NOT EXISTS demo
(
    id
               BINARY(16) PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    created_at DATETIME(6)  NULL
);

CREATE TABLE IF NOT EXISTS outbox_event
(
    id              BINARY(16) PRIMARY KEY,
    type            VARCHAR(200) NOT NULL,
    payload         JSON         NOT NULL,
    occurred_at     TIMESTAMP(6) NOT NULL,

    status          VARCHAR(20)  NOT NULL,
    attempts        INT          NOT NULL,
    next_attempt_at TIMESTAMP(6) NOT NULL,
    last_error      VARCHAR(500),

    locked_by       VARCHAR(64),
    locked_at       TIMESTAMP(6),

    INDEX idx_outbox_due (status, next_attempt_at, occurred_at),
    INDEX idx_outbox_lock (locked_by, locked_at)
);


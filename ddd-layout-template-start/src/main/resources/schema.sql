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

CREATE TABLE IF NOT EXISTS inbox_message
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    message_id    VARCHAR(64)   NOT NULL,
    consumer      VARCHAR(128)  NOT NULL,
    status        VARCHAR(16)   NOT NULL, -- PROCESSING / DONE / FAILED
    payload_hash  CHAR(64)      NULL,
    first_seen_at DATETIME(3)   NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    last_seen_at  DATETIME(3)   NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    processed_at  DATETIME(3)   NULL,
    retry_count   INT           NOT NULL DEFAULT 0,
    last_error    VARCHAR(1024) NULL,
    UNIQUE KEY uk_inbox (message_id, consumer),
    KEY idx_inbox_status (consumer, status, last_seen_at)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS workflow_execution
(
    execution_id VARCHAR(64) PRIMARY KEY,
    workflow_name VARCHAR(128) NOT NULL,
    status VARCHAR(32) NOT NULL,
    started_at TIMESTAMP(6) NOT NULL,
    finished_at TIMESTAMP(6) NULL
);

CREATE TABLE IF NOT EXISTS workflow_node_execution
(
    execution_id VARCHAR(64) NOT NULL,
    node_id VARCHAR(64) NOT NULL,
    status VARCHAR(32) NOT NULL,
    message VARCHAR(255) NULL,
    PRIMARY KEY (execution_id, node_id)
);


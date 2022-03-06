CREATE TABLE momento (
    id BIGINT NOT NULL,
    data_hora DATETIME NOT NULL,
    registro_de_trabalho_id BIGINT NOT NULL,
    PRIMARY KEY (id)
);
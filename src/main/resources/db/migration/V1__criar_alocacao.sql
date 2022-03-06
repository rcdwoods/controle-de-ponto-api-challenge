CREATE TABLE alocacao (
    id BIGINT NOT NULL,
    dia DATE NOT NULL,
    tempo VARCHAR(20) NOT NULL,
    nome_projeto VARCHAR(60) NOT NULL,
    PRIMARY KEY (id)
);
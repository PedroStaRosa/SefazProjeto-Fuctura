-- Scripts para cria��o do projeto --

CREATE TABLE pessoa (
   nome VARCHAR(100) NOT NULL,
   cpf VARCHAR(100) NOT NULL,
   sexo VARCHAR(100) NOT NULL,
    idade INTEGER NOT NULL,
    senha varchar(100) NOT NULL,
   PRIMARY KEY (cpf) 
);

CREATE TABLE livro (
   id INT NOT NULL,
   ano_lancamento INT NOT NULL,
   nome VARCHAR(100) NOT NULL,
   autor VARCHAR(100) NOT NULL,
   cpf_pessoa VARCHAR(100) NOT NULL
);

ALTER TABLE livro
ADD FOREIGN KEY (cpf_pessoa) 
REFERENCES pessoa(cpf);

CREATE SEQUENCE S_LIVRO
	MINVALUE 1 MAXVALUE 999999 INCREMENT BY 1 START WITH 1 ;
	
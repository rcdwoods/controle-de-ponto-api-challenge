# API de Controle de Ponto dos funcionários

O projeto foi feito para auxiliar no controle de ponto dos funcionários de uma organização, de forma que forneça funcionalidades para bater o ponto de funcionários, alocar as horas trabalhadas em projetos e gerar um relatório final contendo todas informações processadas de um determinado mês.

> *Objetivo: garantir o controle das horas trabalhadas de um funcionário dentro de uma organização.*



## Como inicializar o projeto

Para iniciar o projeto, é necessário seguir os seguintes passos:
- Clonar esse repositório para a sua máquina;
- Criar as variáveis de ambientes referentes às credenciais do banco de dados;
- Inicializar a imagem Docker do banco de dados;
- Inicializar a imagem Docker da API.



## Clonar o projeto

- `git clone https://github.com/rcdwoods/controle-de-ponto`
- `cd controle-de-ponto`



## Criação das variáveis de ambiente

Há três variáveis de ambiente, e todas referentes ao acesso do banco de dados, sendo elas:

- `DB_HOST`. Você deve colocar o IP da sua máquina ou localhost. Não deve ser localhost caso escolha executar a API através de um container Docker, porque localhost irá apontar para o próprio container. Nesse caso, você deve colocar o IP da sua máquina, como 192.168.0.x
- `DB_USERNAME`. Por padrão, o username do banco de dados é controle-de-ponto
- `DB_PASSWORD`. Por padrão, a senha do banco de dados é controle-de-ponto



## Criação das imagens Docker

- `docker-compose up -d db`
  Cria a imagem do banco de dados do projeto.

- `docker-compose up -d controle-de-ponto-api`
  Cria a imagem da API do projeto. Vale ressaltar que você já precisa ter criado as variáveis de ambiente DB_HOST, DB_USERNAME e DB_PASSWORD, referentes às credenciais do banco de dados, no seu sistema.



## Limpar, compilar, executar testes de unidade e cobertura, qualidade de código

- `./gradlew clean`
  Limpa os dados gerados pelo build do projeto, como o jar da aplicação.

- `./gradlew build -x test`
  Roda o build do projeto. O "-x test" é para não rodar os testes durante o build.

- `./gradlew test`
  Executa todos os testes do projeto.
  
  - `./gradlew sonarqube`
  Executa a análise da qualidade do código, e a mesma ficará disponível no SonarCloud. Obs: é necessário as credendiais, mas o projeto possui essa step incluída na pipeline, então qualquer interação com o repositório será analisada pelo Sonar.

  

## Requisições da API

- `/controle-de-ponto/v1/batidas`
- `/controle-de-ponto/v1/alocacoes`
- `/controle-de-ponto/v1/folhas-de-ponto/{mês}`



## Visualização do Swagger

Para acessar o Swagger UI e ter acesso aos detalhes das requisições, basta executar a API e acessar o endereço `localhost:8080/controle-de-ponto/swagger-ui/index.html`. 
Outra forma de visualizar os detalhes das requisições é acessando o arquivo controle-de-ponto-api_v1.yml, contido no diretório resources/swagger.



## De quais ferramentas/tecnologias o projeto faz uso?

- Spring
- Swagger
- Flyway Migration
- RestAssured
- SonarQube
- Linter
- Mapstruct
- Pipeline para build, testes e análise da qualidade do código.

# mira-code-challenge

Projeto desenvolvido para avaliação da equipe técnica da MIRA para processo seletivo, de modo que seja possível ter uma
ideia de minhas próprias competências técnicas.

## Sobre a implementação

Optei por desenvolver o projeto utilizando Spring Boot, versão 2. Também optei por trabalhar com persistência de dados,
aqui a camada persistente é gerenciada pelo Spring Data, apoiado em uma base de dados H2 embarcada.

A linguagem utilizada foi a própria linguagem Java, em sua versão 10. Lancei mão de expressões lambda onde julguei fazer
sentido.

O item extra do desafio, isto é, a busca de pessoas por Nome, Sobrenome e/ou CPF foi implementada com a Specifications
API do JPA.

Decidi não colocar regras de negócio, pois entendo que não é esse o objetivo do teste. Coloquei apenas o Nome como campo
obrigatório, mas honestamente acredito que algumas regras poderiam ser implementadas para elevar o nível de sofisticação
de negócio, como CPF único, não possibilitar o cadastro do mesmo telefone e/ou e-mail para a mesma pessoa (embora seja
plenamente possível que o mesmo telefone possa existir entre pessoas diferentes).

Um detalhe importante aqui é o campo data de nascimento, que foi utilizado o tipo `LocalDate` do `java.time`. Para isso,
foi preciso ajustar configurações, do tipo incluir dependência da lib Jackson que habilita trabalhar com a JSR 310, como
também uma configuração de aplicação. Esta, optei por fazer utilizando arquivo YAML ao invés de properties, por 
preferência pessoal.

Utilizei também o Lombok para geração de código, evitando _boilerplate_. Aqui, vale ressaltar que eu mesmo poderia ter
lançado mão da IDE (utilizei IntelliJ IDEA) para gerar métodos acessores, `toString()`, etc. Mas mesmo assim, por 
clareza de código, optei por manter o Lombok.

Fiz uma abordagem de micro serviços, isto é, este projeto é um micro serviço de pessoas. Aqui, o cenário seria haver um
`api-gateway` responsável por direcionar a chamada para este e, nesse gateway, haveria uma rota `/people` que, abaixo
dela, as solicitações seriam direcionadas para este micro serviço. Assim sendo, as rotas expostas são:

*  GET /
*  GET /:id
*  GET /search _(recebendo parâmetros `givenName`, `familyName` e `cpf`)_
*  POST /
*  POST /bulk
*  PUT /:id
*  DELETE /:id

## Sobre os testes

Os testes foram escritos na linguagem Groovy, utilizando a lib Spock. Acredito que o código fica mais legível e limpo,
ficando clara a separação de cenário, condição e consistências do teste. O Spock possui ainda facilidades para mocking,
motivo pelo qual optei por esta lib ao invés do JUnit.

A maior concentração de testes é do tipo _End to End_, mas também há testes unitários definidos contra a classe
`PersonService`.

Preocupo-me muito com cobertura de testes, por isso mesmo inclui o relatório no versionamento do projeto (em condições
normais, o diretório teria sido incluído no `.gitignore`).

## Como executar

Na linha de comando, com Maven instalado, para executar:
```
$ mvn clean compile spring-boot:run
```

Na linha de comando, também com Maven instalado, para testar:
```
$ mvn clean compile test
```

Foi feito deploy na plataforma Heroku, sob o endereço: https://people-code-challenge.herokuapp.com.

Há um mock do objeto conforme domínio esperado em `test/resources`.

## Sobre o desafio

Penso que ficou um pouco aberto se deveria haver persistência. Acho uma competência importante de se solicitar, também
acho que teria ficado mais simples se implementado com um MongoDB (pensei depois) por conta das anotações de ORM do JPA.


# Aplicação de Votação - Documentação

## Visão Geral
Este projeto implementa uma API RESTful para gerenciamento de votações em pautas, com controle de sessões, votos, e integração externa para validação de CPF. Foi desenvolvido com foco em simplicidade e eficiência, evitando overengineering.

---

## Tecnologias Utilizadas
- Java 17+
- Spring Boot
- Spring MVC (REST API)
- Spring MockMvc para testes
- Swagger/OpenAPI para documentação da API
- JUnit 5 para testes unitários
- Mockito para mocks nos testes
- Ferramentas de performance (a definir)
- H2 Database (modo persistente - os dados são salvos em arquivo local para manter o estado entre reinicializações)

---

## Estrutura da API e Versionamento
- API base: `/api/v1`
- Versionamento via URL, garantindo compatibilidade e evolução futura.

---

## Funcionalidades Principais
- Cadastro e abertura de sessões de votação para pautas.
- Registro e contagem de votos com validação de permissão via serviço externo.
- Consulta dos resultados de votação.
- Tratamento adequado de erros e respostas HTTP claras.

---

## Como Rodar a Aplicação

### Pré-requisitos
- Java JDK 17 ou superior instalado
- Maven ou Gradle para build e instalação das dependências necessárias.
- Git para clone em ambiente local.

### Passos para execução

# Instruções para Rodar e Testar a Aplicação de Votação

## Passos para Rodar a Aplicação

1. Clone o repositório:
   ```bash
   git clone https://github.com/darj-1914/desafio-votacao-sicredi-resolucao.git
   cd desafio-votacao-sicredi-resolucao
   ```

2. Compile e rode a aplicação:
   - Com Maven:
     ```bash
     mvn clean spring-boot:run || ./mvnw spring-boot:run
     ```
   - Ou com Gradle:
     ```bash
     ./gradlew bootRun
     ```
   - Ou executando a classe principal manualmente:
     ```bash
     br.com.votacao.VotacaoApplication
     ```

3. A aplicação estará disponível em:
   ```
   http://localhost:8080
   ```

---

## Como Testar a API

### Usando Swagger UI

A documentação interativa da API está disponível em:
```
http://localhost:8080/swagger-ui.html
```
ou, dependendo da configuração,
```
http://localhost:8080/swagger-ui/index.html
```

Lá você pode consultar todos os endpoints e testar requisições diretamente pelo navegador.

### Endpoints disponíveis

## Votos

### `POST /api/v1/votos`
Registra um voto.  
**Body JSON esperado:**
```json
{
  "cpf": "12345678901",
  "associadoId": 1,
  "pautaId": 1,
  "opcao": "SIM" // ou "NAO"
}
```
**Retorno:** voto registrado ou erro (CPF inválido, voto duplicado, etc).

---

### `GET /api/v1/votos/resultado/{pautaId}`
Retorna o resultado da votação de uma pauta específica.

**Resposta JSON:**
```json
{
  "pautaId": 1,
  "sim": 10,
  "nao": 3,
  "resultado": "APROVADO" // ou "REJEITADO", "EMPATE"
}
```

---

## Pautas

### `POST /api/v1/pautas`
Cria uma nova pauta.

**Body JSON:**
```json
{
  "nome": "Título da pauta"
}
```

**Retorno:** objeto `Pauta` criado.

---

### `GET /api/v1/pautas`
Lista todas as pautas cadastradas.

---

### `GET /api/v1/pautas/{id}`
Retorna os dados de uma pauta específica pelo ID.

---

## Sessões de Votação

### `POST /api/v1/sessoes/abrir/{pautaId}?duracaoMinutos=10`
Abre uma nova sessão de votação para a pauta informada.  
**Parâmetro opcional:** `duracaoMinutos` (default se não informado).

**Exemplo:**
```bash
POST /api/v1/sessoes/abrir/1?duracaoMinutos=5
```

**Retorno:** dados da sessão criada.

### Deixarei uma collection disponível para donwload caso deseje testar pelo postman.

Estará na raiz do projeto para facilitar o encontro(Votacao API.postman_collection.json)

### Testes Unitários

Para executar os testes unitários automatizados:

- Com Maven:
  ```bash
  mvn test
  ```
- Com Gradle:
  ```bash
  ./gradlew test
  ```

Os testes cobrem cenários de sucesso, falha, validação de dados e integração com mocks dos serviços externos.

### Testes de Performance

- Atualmente, testes de performance podem ser realizados usando ferramentas externas como JMeter ou Gatling.
- Criado cenário de teste unitário simulando alto envio de consumo de votos.
- Foi posto configurações no próprio properties possibilitanto também visualização da performance no próprio console.

---

## Integração com Sistemas Externos

- O sistema consulta o serviço externo de CPF (`CpfClient`) para validar se o associado pode votar.
- Tratamento de exceções específicas para CPF inválido ou impossibilidade de votar.

---

## Boas Práticas e Considerações

- Código simples, claro e modularizado.
- Tratamento padronizado de erros e uso correto dos códigos HTTP.
- Versionamento explícito da API para facilitar manutenção e futuras atualizações.
- Testes que garantem a estabilidade da aplicação.
- Documentação automática via Swagger para facilitar consumo da API por terceiros.

---

## Contato e Suporte

Caso haja algum adendo ou dúvida sobre o projeto desenvolvido
entrar em contato pelo mesmo email ao qual foi enviado(daniel2018arj@gmail.com ou daniel.arj.profissional@gmail.com).

## Demonstração de algumas partes da implementação por meio de fotos

Segue link para visualização -> https://drive.google.com/drive/folders/1OVzqZSmXGPJ_VahbgAOp6LfPkvBLMNIC?usp=sharing

---

**Obrigado pela oportunidade, aguardo retorno sobre o desafio!**
# CRM Backend (Spring Boot)

Projeto backend em Java Spring Boot para um CRM — scaffolding inicial.

Como executar (PowerShell):

1. Compilar:
```
mvn clean package
```

2. Rodar via Maven (desenvolvimento):
```
mvn spring-boot:run
```

3. Rodar JAR gerado:
```
java -jar target\crm-backend-0.0.1-SNAPSHOT.jar
```

4. Rodar em porta alternativa:
```
java -jar target\crm-backend-0.0.1-SNAPSHOT.jar --server.port=8081
```

Observações:
- A aplicação usa MongoDB (config em `src/main/resources/application.properties`).
- O `DataSeeder` popula a coleção `users` na primeira execução, se vazia.

**Rotas / Endpoints**

As rotas abaixo são as APIs REST iniciais disponíveis no backend.

- **Users** (Gerenciamento de usuários em memória / MongoDB)
	- `GET /api/users` : retorna a lista de usuários.
		- Response: `200 OK` com um array de usuários.
	- `POST /api/users` : cria um novo usuário.
		- Body (JSON):
			```json
			{
				"firstName": "Nome",
				"lastName": "Sobrenome",
				"email": "email@example.com"
			}
			```
		- Response: `200 OK` com o usuário criado (contendo `id`).
	- `GET /api/users/{id}` : busca usuário por `id`.
		- Response: `200 OK` com o usuário ou `404 Not Found`.
	- `PUT /api/users/{id}` : atualiza o usuário com os dados fornecidos.
		- Body: mesmo formato do POST. Retorna `200 OK` com usuário atualizado ou `404`.
	- `DELETE /api/users/{id}` : remove o usuário (apaga do banco/memória).

- **Companies** (Coleção `companies` em MongoDB)
	- `GET /api/companies` : lista todas as empresas.
		- Suporta filtros simples por query (ex.: `?status=ATIVO`).
	- `POST /api/companies` : cria uma nova empresa.
		- Body (JSON) exemplo:
			```json
			{
				"name": "Acme Tecnologia LTDA",
				"cnpj": "12345678000195",
				"phone": "+55 (11) 99999-0000",
				"website": "https://www.acmetecnologia.com.br",
				"address": {
					"street": "Av. Paulista",
					"number": "1000",
					"city": "São Paulo",
					"state": "SP",
					"postalCode": "01310-100"
				},
				"status": "ATIVO",
				"tags": ["saas","enterprise"]
			}
			```
		- Observações: o `cnpj` é normalizado removendo caracteres não numéricos antes de salvar.
		- Response: `200 OK` com o documento salvo.
	- `GET /api/companies/{id}` : retorna a empresa por `id` (`200 OK` ou `404`).
	- `PUT /api/companies/{id}` : atualiza uma empresa existente. Retorna `200 OK` ou `404`.
	- `DELETE /api/companies/{id}` : exclui a empresa (hard delete atualmente).

**Notas importantes**
- A porta padrão do servidor é `8080`. Para rodar em outra porta, passe `--server.port=PORT` ao iniciar.
- `DataSeeder` roda na inicialização e só popula `users` se a coleção estiver vazia.
- Validação e regras (ex.: verificação de CNPJ) devem ser aprimoradas em produção. Atualmente o `cnpj` é apenas normalizado (somente dígitos) e não validado com algoritmo.

Se quiser, posso adicionar exemplos de `curl` ou testes automatizados para cada rota.

	**Contacts (Contatos)**

	Endpoints para gerenciar contatos vinculados a empresas.

	- `GET /api/contacts` : lista todos os contatos. Aceita filtros por `companyId`.
		- Exemplo: `GET /api/contacts?companyId={companyId}`
		- Response: `200 OK` com array de contatos.

	- `POST /api/contacts` : cria um novo contato.
		- Body (JSON) exemplo:
			```json
			{
				"firstName": "João",
				"lastName": "Silva",
				"email": "joao.silva@example.com",
				"jobTitle": "Gerente de Vendas",
				"companyId": "653a0b...",
				"linkedinProfile": "https://www.linkedin.com/in/joaosilva"
			}
			```
		- Response: `200 OK` com o contato criado (contendo `id`, `createdAt`, `updatedAt`).

	- `GET /api/contacts/{id}` : retorna o contato por `id` (`200 OK` ou `404`).

	- `PUT /api/contacts/{id}` : atualiza o contato existente. Body igual ao `POST`.

	- `DELETE /api/contacts/{id}` : exclui o contato.

	Exemplos `curl` rápidos (assumindo servidor em `localhost:8080`):

	Criar contato:
	```bash
	curl -X POST http://localhost:8080/api/contacts \
		-H "Content-Type: application/json" \
		-d '{"firstName":"João","lastName":"Silva","email":"joao.silva@example.com","jobTitle":"Rep","companyId":"<companyId>","linkedinProfile":"https://www.linkedin.com/in/joaosilva"}'
	```

	Listar contatos:
	```bash
	curl http://localhost:8080/api/contacts
	```

	Buscar por empresa:
	```bash
	curl "http://localhost:8080/api/contacts?companyId=<companyId>"
	```

	Notas:
	- `companyId` deve ser o `_id` de um documento em `companies` (armazenado como string no campo `companyId`).
	- Campos de auditoria `createdAt` e `updatedAt` são preenchidos automaticamente pelo Spring Data MongoDB (auditing).


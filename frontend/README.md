# CRM Frontend (Vue 3 + Vite)

Boilerplate inicial do frontend do CRM.

Requisitos: `node` (>=16) e `npm` ou `yarn`.

Instalar dependências:

```powershell
cd frontend
npm install
```

Rodar em desenvolvimento (Vite):

```powershell
npm run dev
```

Configuração da API
- O frontend usa a variável `VITE_API_BASE_URL` para montar as chamadas (arquivo `.env.development` já aponta para `http://localhost:8081/api`).
- Se o backend estiver em outra porta, atualize `.env.development` ou exporte `VITE_API_BASE_URL` antes de iniciar.

Exemplos de execução rápida

```powershell
# Iniciar backend (exemplo na porta 8081)
cd D:\2025dev\CRM
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=8081"

# Em outra janela, iniciar frontend
cd D:\2025dev\CRM\frontend
npm run dev
```

Mapeamento dos endpoints (backend) e como acessá-los do frontend

OBS: os serviços frontend já criados ficam em `src/services`:
- `api.js` — wrapper axios com `baseURL` configurado via `VITE_API_BASE_URL`.
- `users.js`, `companies.js`, `contacts.js`, `deals.js` — métodos que correspondem aos endpoints REST.

1) Users
- GET `/api/users` — lista todos os usuários
	- curl: `curl http://localhost:8081/api/users`
	- axios: `await usersService.getUsers()`
- POST `/api/users` — cria usuário
	- body exemplo:
		```json
		{"firstName":"Igor","lastName":"Marinho","email":"igor@example.com"}
		```
	- curl:
		```powershell
		curl -X POST http://localhost:8081/api/users -H "Content-Type: application/json" -d '{"firstName":"Igor","lastName":"M","email":"igor@example.com"}'
		```
	- axios: `await usersService.createUser(payload)`
- GET `/api/users/{id}` — buscar por id
	- axios: `await usersService.getUser(id)`
- PUT `/api/users/{id}` — atualizar
	- axios: `await usersService.updateUser(id, payload)`
- DELETE `/api/users/{id}` — deletar
	- axios: `await usersService.deleteUser(id)`

2) Companies
- GET `/api/companies` — lista empresas (aceita filtros via query params)
	- exemplo: `GET /api/companies?status=ATIVO`
	- curl: `curl "http://localhost:8081/api/companies"`
	- axios: `await companiesService.getCompanies()` ou `await companiesService.getCompanies({ status: 'ATIVO' })`
- POST `/api/companies` — criar
	- body exemplo (JSON):
		```json
		{
			"name":"Acme Tecnologia LTDA",
			"cnpj":"12345678000195",
			"phone":"+55 (11) 99999-0000",
			"website":"https://www.acme.com",
			"address": { "street":"Av. Paulista","number":"1000","city":"São Paulo","state":"SP","postalCode":"01310-100" },
			"status":"ATIVO",
			"tags":["saas","enterprise"]
		}
		```
	- axios: `await companiesService.createCompany(payload)`
- GET `/api/companies/{id}` — buscar por id
	- axios: `await companiesService.getCompany(id)`
- PUT `/api/companies/{id}` — atualizar
	- axios: `await companiesService.updateCompany(id, payload)`
- DELETE `/api/companies/{id}` — deletar
	- axios: `await companiesService.deleteCompany(id)`

3) Contacts
- GET `/api/contacts` — lista contatos; filtrar por `companyId` via query param
	- exemplo: `GET /api/contacts?companyId={companyId}`
	- axios: `await contactsService.getContacts({ companyId })`
- POST `/api/contacts` — criar contato
	- body exemplo:
		```json
		{
			"firstName":"João",
			"lastName":"Silva",
			"email":"joao.silva@example.com",
			"jobTitle":"Gerente de Vendas",
			"companyId":"<companyId>",
			"linkedinProfile":"https://www.linkedin.com/in/joaosilva"
		}
		```
	- axios: `await contactsService.createContact(payload)`
- GET `/api/contacts/{id}` — buscar contato
	- axios: `await contactsService.getContact(id)`
- PUT `/api/contacts/{id}` — atualizar
	- axios: `await contactsService.updateContact(id, payload)`
- DELETE `/api/contacts/{id}` — deletar
	- axios: `await contactsService.deleteContact(id)`

4) Deals (oportunidades)
- GET `/api/deals` — lista deals; aceita filtros: `companyId`, `contactId`, `pipelineStage`
	- exemplo: `GET /api/deals?companyId=<id>` ou `GET /api/deals?pipelineStage=NEGOCIACAO`
	- axios: `await dealsService.getDeals({ companyId })`
- POST `/api/deals` — criar deal
	- body exemplo:
		```json
		{
			"title":"Proposta SaaS",
			"companyId":"<companyId>",
			"contactId":"<contactId>",
			"dealValue":10000.00,
			"currency":"BRL",
			"pipelineStage":"PROSPECCAO",
			"probability":0.2,
			"expectedCloseDate":"2025-12-31",
			"notes":"Primeira proposta enviada"
		}
		```
	- axios: `await dealsService.createDeal(payload)`
- GET `/api/deals/{id}` — buscar por id
	- axios: `await dealsService.getDeal(id)`
- PUT `/api/deals/{id}` — atualizar todo o deal
	- axios: `await dealsService.updateDeal(id, payload)`
- PATCH `/api/deals/{id}/stage` — atualizar estágio parcial (pipelineStage, probability, expectedCloseDate)
	- body exemplo: `{ "pipelineStage": "NEGOCIACAO", "probability": 0.5 }`
	- axios: `await dealsService.changeStage(id, { pipelineStage: 'NEGOCIACAO', probability: 0.5 })`
- DELETE `/api/deals/{id}` — deletar
	- axios: `await dealsService.deleteDeal(id)`

Dicas rápidas de uso no frontend
- Exemplo usando async/await em um componente:
```js
import companiesService from './services/companies'

async function load() {
	const res = await companiesService.getCompanies()
	console.log(res.data)
}
```

- Se receber erros CORS, verifique se o backend está rodando e que a configuração de CORS (`src/main/java/com/crm/config/CorsConfig.java`) permite a origem do Vite (`http://localhost:5173`).

Se quiser, eu posso:
- Gerar exemplos de payloads finais (JSON Schema) para cada rota e adicioná-los às anotações OpenAPI; ou
- Criar páginas básicas de CRUD (com Vue Router + Pinia) para cada entidade.

---
Arquivo de referência dos serviços frontend: `src/services/*.js` — chame os métodos mostrados acima para integrar as telas.
Endpoints de exemplo usados pelo componente:
- `GET /api/companies` — lista empresas.

Para alterar a base da API, atualize `.env.development` ou defina `VITE_API_BASE_URL`.

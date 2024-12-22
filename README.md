# Tolerância a Falhas - Projeto Ecommerce Parte 1

Este projeto implementa um sistema de e-commerce com componentes projetados para serem tolerantes a falhas. A solução é composta por vários serviços em containers Docker que se comunicam em uma rede interna. 

# Requisitos

- Docker 20.10+

- Docker Compose 1.29+

- Java 17+

- Maven 3.8+

# Configuração e Execução

Para construir os containers, no diretório raiz do projeto execute o comando:

`docker compose build`

Para subir os containers:

`docker compose up`

Todos os serviços serão iniciados e conectados à rede ecommerce. 

# Para testar:
Utilize a ferramenta CURL, os códigos podem ser importados no Postman ou Insomia para enviar as requisições aos endpoints.

## Requisição 0
O fluxo inteiro pode ser executado a partir da chamada do ecommerce para o endpoint buy, a partir dele são feitas de forma consecutiva as chamadas para os endpoints de store, exchange e fidelity. Como está configurado no docker compose, o acesso ao serviço ecommerce é feito via porta 8080. 

Segue o CURL:

`curl --request POST \
  --url http://localhost:8080/ecommerce/buy \
  --header 'Content-Type: application/json' \
  --header 'User-Agent: insomnia/9.3.0' \
  --data '{
    "productId":"1",
    "userId": "2",
    "ft": true
}'`       

## É possível também testar localmente de forma individual as requisições com base nas portas configuradas no docker compose 

Segue os CURLs:

## Requisição 1
`curl --request GET \
  --url http://localhost:8081/store/product/1 \
  --header 'User-Agent: insomnia/9.3.0'`

## Requisição 2
Réplica 1:
`curl --request GET \
  --url http://localhost:8083/exchange \
  --header 'User-Agent: insomnia/9.3.0'`

Réplica 2:
`curl --request GET \
  --url http://localhost:8083/exchange \
  --header 'User-Agent: insomnia/9.3.0'`

## Requisição 3

`curl --request POST \
  --url http://localhost:8081/store/sell \
  --header 'Content-Type: application/json' \
  --header 'User-Agent: insomnia/9.3.0' \
  --data '{
	"id": "1"
}'`       

## Requisição 4
`curl --request POST \
  --url http://localhost:8082/fidelity/bonus \
  --header 'Content-Type: application/json' \
  --header 'User-Agent: insomnia/9.3.0' \
  --data '{
	"user": "2",
	"bonus": 140
}'`


# Para desligar os containers:
`docker compose down`

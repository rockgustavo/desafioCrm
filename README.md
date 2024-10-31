# Desafio CRM

## Este projeto traz como idéia um monólito que gerencia pedidos

### Ele traz dados do cliente e produtos. Gerencia o estoque disponível deste produto e se tiver a quantidade solicitada ao realizar um pedido ele irá registrar este pedido e controlar o saldo deste estoque.

### O modelo está em fase de evolução e aborda como fundamento apresentar tecnologias.

## Back-End - Java Spring Boot

## Base de dados - MySQL

### Documentação do projeto

Após rodar o container utilize esta **URL**:

```
http://localhost:8081/swagger-ui/index.html#/

```

## Orquestração de Containers com Docker Compose

Ao realizar o download do projeto abra o **CMD** ou o **Terminal** em seu sistema operacional e execute:

```
docker-compose up --build -d
```

## Preparativos para testar o projeto em containers

### 1. Instalar o Docker

Antes de começar, certifique-se de ter o Docker instalado. Você pode baixar e instalar o Docker a partir do site oficial: [Docker Download](https://www.docker.com/get-started).

Após a instalação, abra o **CMD** ou o **Terminal** em seu sistema operacional.

#### Verifique a Instalação do Docker

Para garantir que o Docker está funcionando corretamente, execute o seguinte comando:

```
docker --version
```

### 2. Crie um rede docker

Vamos criar uma network para que os containers se comuniquem..

#### No CMD/Terminal, execute o seguinte comando:

```
docker network create my-network
```

### 2. Executando o Banco de Dados MySQL

O primeiro passo é iniciar o container do MySQL. Este container será responsável por armazenar os dados da aplicação.

#### No CMD/Terminal, execute o seguinte comando para iniciar o MySQL:

```
docker run -d --name mysql-container --network my-network -e MYSQL_ROOT_PASSWORD=root -e MYSQL_USER=admin -e MYSQL_PASSWORD=adm123 -e MYSQL_DATABASE=desafiocrm -p 3307:3306 mysql:8
```

### 3. Executando o Backend

Ao realizar o download do projeto abra o CMD ou o Terminal em seu sistema operacional e execute:
Com o MySQL em execução, agora podemos iniciar o backend da aplicação que irá carregar dados iniciais na base de dados.

#### No CMD/Terminal, execute o seguinte comando para montar a imagem do backend:

```
docker build -t desafiocrm:1.0 .
```

#### No CMD/Terminal, execute o seguinte comando para criar o container e iniciar:

```
docker run -d --name backend --network my-network -p 8081:8081 desafiocrm:1.0
```

### 4. Testando os endpoints com a ferramenta de testes de API Postman

Na raiz do projeto tem o arquivo **'Desafio CRM.postman_collection.json'** que ao abrir o Postman basta clicar em **import** arrastar este arquivo e confirmar a criação desta coleção.

### 5. Parando e Removendo os Contêineres

#### Parar os Containeres:

```
docker stop backend mysql-container
```

#### Remover os Containeres:

```
docker rm backend mysql-container
```

### 7. Limpando Volumes

#### Se quiser limpar os volumes criados durante a execução dos containeres, use o comando:

```
docker volume rm mysql_data
```

## Detalhes relevantes do projeto:

#### O application.yml está dividido em 3 partes.

**application.yml** - Faz o apontamento de qual application-...yml será lido pelo projeto.

**application-test.yml** - Está configurado para rodar localmente com o Banco de dados em memória H2. Ideal para rodar os testes de integração da camada Repository.

**application-dev.yml** - Está configurado para rodar em containers se comunicando através da network. Se for rodar localmente em uma IDE precisa ser descomentado o caminho da url local url: jdbc:mysql://localhost:3307/desafiocrm...

**application-prod.yml** - Está configurado para rodar com o docker compose.

## Se um container não subir pode estar ocorrendo um conflito de porta:

#### Para evitar conflitos de portas nas aplicações:

**Confira se a porta 8081** está sendo utilizada em sua máquina:
No Windows, você pode usar:

```
netstat -ano | findstr :8081
```

Em sistemas baseados em Linux ou Mac, use o comando:

```
sudo lsof -i :8081
```

**Você pode encerrar o processo que está utilizando a porta**, copiando o PID(númeração lado direito)

No Windows, você pode usar:

```
taskkill /PID numeroPID /F
```

Em sistemas baseados em Linux ou Mac, use o comando:

```
sudo kill -9 numeroPID
```

**Confira se a porta 3307** está sendo utilizada em sua máquina:
No Windows, você pode usar:

```
netstat -ano | findstr :3307
```

Em sistemas baseados em Linux ou Mac, use o comando:

```
sudo lsof -i :3307
```

**Você pode encerrar o processo que está utilizando a porta**, copiando o PID(númeração lado direito)

No Windows, você pode usar:

```
taskkill /PID numeroPID /F
```

Em sistemas baseados em Linux ou Mac, use o comando:

```
sudo kill -9 numeroPID
```

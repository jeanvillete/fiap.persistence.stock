# 1 -intro

- o projeto corrente tem por responsabilidade manter registros dos produtos disponíveis **(cadastrados no fiap.stock.mgnt)**, de endereços e pedidos dos clientes, que acabam vendo os produtos e solicitando seus pedidos através deste projeto corrente.
- quando um cliente seleciona produtos e conclui um pedido, este ultimo é disparado para o projeto **fiap.stock.mgnt** para que lá o estoquista tenha a possibilidade de confirmar a preseça ou não do produto, lançando neste uma confirmação ou rejeição do pedido.
- o banco de dados utilizado é o **MongoDB** e na camada de Service apenas para o domínio de Produto é utilizado o **Spring Cache com Redis** para otimizar seu encontro rápido por código, que é necessário quando chega um pedido.
- os casos de uso abaixo determinam quais os serviços RESTful são expostos

---

#### 1.1 - execução

as definições de propriedades e configurações de porta ***(atual 8383)*** do servidor da aplicação corrente, do banco de dados **MySQL** e **Redis** estão no artefato ```application.yml```

**MongoDB**

    host: localhost
    port: 27017
    database: fiapSampleLogin

**Redis**

    host: localhost
    port: 6379

---

# 2 - casos de uso e seus endpoints [use case]

abaixo segue a lista de casos de uso e exemplos de requisições e respostas;  

#### 2.1 - [use case: estoquista adiciona/atualiza um produto ao portal]
- o payload postado pelo estoquista através de uma ação no projeto **fiap.stock.mgnt** deve ser carregado no **fiap.stock.portal** com os mesmos dados, numa operação de ***UPSERT***, ou seja, se ainda não existe este produto no **fiap.stock.portal**, então este deve ser inserido, e caso exista, deve ser totalmente substituído/sobrescrito.
    - para fazer a validação de ***UPSERT***, é necessário logicamente procurar por um produto com o mesmo ***code***, então inferir se deve ser inserido ou sobrescrito.
- as informações esperadas para um domínio Produto no **fiap.stock.portal** são;
    - ***code***
    - ***description***
    - ***price***
    - ***quantity***
- a informação ***loginId*** deverá ser recebida via path variable, e refere-se a identificação do estoquista (UserType stock), o que quer dizer que o valor de um login válido efetuado via módulo ***fiap.sample.login*** deve ter sido obtido
    - ***loginId***
        - [validar] deve ser verificado se o ***loginId*** é de fato válido para o tipo (UserType) 'stock'
- caso registro armazenado com sucesso, devolver resposta com corpo completo e status ***201 Created***

```$ curl localhost:8383/portal/users/5ef958b02994931e98c15366/products -d '{ "code": "PRD-9876543", "description": "sample product description", "price": 253.63, "quantity": 24 }' -H 'Content-Type: application/json' ```

```
[request]
POST portal/users/5ef958b02994931e98c15366/products
{
    "code": "PRD-9876543",
    "description": "sample product description",
    "price": 253.63,
    "quantity": 24
}

[response]
201 Created
5ef958b02994931e98c15366
```

---

#### 2.2 - [use case: lista produtos no portal, acessível a estoquista ou cliente]
- carregar toda a lista de produtos registrada no **fiap.stock.portal**
- os dados carregados no payload para o domínio Produto devem ser;
    - ***code***
    - ***description***
    - ***price***

```$ curl localhost:8383/portal/users/5ef958b02994931e98c15366/products -H 'Content-Type: application/json' ```

```
[request]
GET portal/users/5ef958b02994931e98c15366/products

[response]
200 Ok
{
    "products": [
        {
            "code": "PRD-9876543",
            "description": "sample product description",
            "price": 253.63
        },
        {
            "code": "PRD-123456",
            "description": "another product",
            "price": 14.01
        }
    ]
}
```

---

#### 2.3 - [use case: carrega determinado produto baseado no seu code, acessível a estoquista ou cliente]
- carregar todos os dados de um domínio Produto, baseado no seu code
- dados do produto;
    - ***code***
    - ***description***
    - ***price***
    - ***quantity***
    
```$ curl localhost:8383/portal/users/5ef958b02994931e98c15366/products/PRD-9876543 -H 'Content-Type: application/json' ```

```
[request]
GET portal/users/5ef958b02994931e98c15366/products/PRD-9876543

[response]
200 Ok
{
    "code": "PRD-9876543",
    "description": "sample product description",
    "price": 253.63,
    "quantity": 24
}
```

---

#### 2.4 - [use case: adiciona um registro de endereço para o cliente]
- adiciona um registro de endereço para um cliente
- payload com os dados;
    - ***zipCode***
    - ***complement***
    - ***city***
    - ***state***
    - ***country***
- a informação ***loginId*** deverá ser recebida via path variable, e refere-se a identificação do cliente (UserType customer), o que quer dizer que o valor de um login válido efetuado via módulo ***fiap.sample.login*** deve ter sido obtido
    - ***loginId***
        - [validar] deve ser verificado se o ***loginId*** é de fato válido para o tipo (UserType) 'customer'
        
```$ curl localhost:8383/portal/users/5ef9589c2994931e98c15365/addresses -d '{ "zip_code": "123456-789", "complement": "Cond Azul, Bl A Apt 123", "city": "São Paulo", "state": "São Paulo", "country": "Brasil" }' -H 'Content-Type: application/json' ```

```
[request]
POST portal/users/5ef958b02994931e98c15366/addresses
{
    "zip_code": "123456-789",
    "complement": "Cond Azul, Bl A Apt 123",
    "city": "São Paulo",
    "state": "São Paulo",
    "country": "Brasil"
}

[response]
201 Created
5ff958bGH994931e98c15364
```

---

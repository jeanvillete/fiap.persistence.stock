# 1 -intro

- o projeto corrente tem por responsabilidade manter registros de catálogo e seus correspondentes produtos dentro do estoque, além disto, quando chegar uma requisição de um pedido, o estoquista deve confirmar o pedido, indicando que o item foi encontrado no estoque e que o fluxo da compra pode continuar, onde neste caso de exemplo, seria dada como compra concluída e produto entregue.
- o banco de dados utilizado é o **MySQL (RDMBS/transacional)** e na camada de Service apenas para o domínio de Produto é utilizado o **Spring Cache com Redis** para otimizar seu encontro rápido por código, que é necessário quando chega um pedido.
- os casos de uso abaixo determinam quais os serviços RESTful são expostos

---

#### 1.1 - execução

as definições de propriedades e configurações de porta ***(atual 8282)*** do servidor da aplicação corrente, do banco de dados **MySQL** e **Redis** estão no artefato ```application.yml```

**MySQL**

    url: jdbc:mysql://localhost:3306/stock
    username: root
    password: fiap-persistencepwd
    driver-class-name: com.mysql.jdbc.Driver
    platform: org.hibernate.dialect.MySQL5InnoDBDialect

**Redis**

    host: localhost
    port: 6379

---

# 2 - casos de uso e seus endpoints [use case]

abaixo segue a lista de casos de uso e exemplos de requisições e respostas;  

#### 2.1 - [use case: estoquista adiciona um item ao catálogo]
- o catálogo é o que retém a descrição dos produtos, logo tem uma relação **um para muitos (1,N)** com o domínio de produtos
- a informação básica de um item do catálogo, para fins de mostrar relacionamento porém com simplicidade, é a sua descrição, logo esta informação deverá ser recuperada no corpo do payload
    - ***description*** (campo varchar com no máximo 50 caracteres), campo mandatório
- a informação ***loginId*** deverá ser recebida via path variable, e refere-se a identificação do estoquista (UserType stock), o que quer dizer que o valor de um login válido efetuado via módulo ***fiap.sample.login*** deve ter sido obtido
    - ***loginId*** (deve conter no máximo 25 caracteres, necessário para o tamaho de um _id do MongoDB, que é de onde vem esta informação), campo mandatório
        - [validar] deve ser verificado se o ***loginId*** é de fato válido para o tipo (UserType) 'stock'
- caso já tenha um catálogo com a descrição fornecida, devolver mensagem de erro explicando do problema e status code ***409 Conflict***
    - aplicar ***trim()*** na descrição fornecida assim que recebida na requisição
- caso registro armazenado com sucesso, devolver resposta com corpo completo e status ***201 Created***

```$ curl localhost:8282/stock/users/5ef958b02994931e98c15366/catalogs -d '{"description": "sample product description"}' -H 'Content-Type: application/json' ```

```
[request]
PUT stock/users/5ef958b02994931e98c15366/catalogs
{
    "description": "sample product description"
}

[response]
201 Created
{
    "id": 123,
    "description": "sample product description",
    "login_id": "5ef958b02994931e98c15366"
}
```

---

#### 2.2 - [use case: estoquista adiciona um produto]
- um produto tem um relancionamento **muitos para um (N,1)** com o domínio de catálogo
- o payload de um produto sendo adicionado deve conter os dados
    - ***catalog*** (campo inteiro, identificador do domínio de catálogo, logo deve ter sido obtido antes), campo mandatório
    - ***price*** (campo com valor real, ou seja com ponto flutuante), campo mandatório
    - ***quantity*** (campo inteiro) campo mandatório
- a informação ***loginId*** deverá ser recebida via path variable, e refere-se a identificação do estoquista (UserType stock), o que quer dizer que o valor de um login válido efetuado via módulo ***fiap.sample.login*** deve ter sido obtido
    - ***loginId*** (deve conter no máximo 25 caracteres, necessário para o tamaho de um _id do MongoDB, que é de onde vem esta informação), campo mandatório
        - [validar] deve ser verificado se o ***loginId*** é de fato válido para o tipo (UserType) 'stock'
- o resultado deve ter o payload com os dados adicionais, e devolvido com status ***201 Created***
    - ***code*** o campo é calculado pelo aplicação quando inserir um produto, é formado pelo prefixo **"PRD-"** e 7 digitos (apenas números)
    - ***entry_date*** campo que afirma a hora de registro do item
- [IMPORTANTE] assim que for adicionado um produto na base local **MySQL**, o objeto completo retornado através da sua propriedade ***code*** deve ser utilizado para passar para o serviço que sumariza o estado atual deste produto, e com esta informação sumarizada dispara um evento para módulo ***fiap.stock.portal*** atulizar na sua base a sua própria versão do ***domínio Product***

```$ curl localhost:8282/stock/users/5ef958b02994931e98c15366/products -d '{"catalog": 123, "price": 253.63, "quantity": 24}' -H 'Content-Type: application/json' ```

```
[request]
PUT stock/users/5ef958b02994931e98c15366/products
{
    "catalog": 123,
    "price": 253.63,
    "quantity": 24
}

[response]
201 Created
{
    "code": "PRD-9876543",
    "catalog": 123,
    "price": 253.63,
    "quantity": 24,
    "entry_date": "2019-01-29T05:18:56"
}
```

---

#### 2.3 - [use case: cliente adiciona um pedido]
- a adição de um pedido vem de uma requisição que chega do módulo ***fiap.stock.portal***
- o domínio de pedido tem um relacionamento **muitos para muitos (N,N)** com produtos
- como a requisição que vem do módulo ***fiap.stock.portal*** tem uma lista dos códigos dos produtos, devemos então buscar pelos produtos correspondentes na base local **MySQL** antes de efetivar a persistência do objeto deserializado
- o payload na requisição é composto dos campos
    - ***products[].code*** (campo mandatório) um array de objetos com campo ***code***, que deve corresponder ao ***code*** do domínio ***Product*** da base local **MySQL** do módulo corrente
        - [validar] se não for encontrado o produto com o código fornecido, devolver mensagem informando o ocorrido com status ***400 Bad Request***
    - ***products[].quantity*** (campo mandatório) ainda no array supracitado, a quantidade desejada na compra/pedido, que deve ser menor ou igual a quantidade disponível
        - [validar] se a quantidade solicitada é menor ou igual a quantidade corrente, caso contrário, devolver mensagem informando o ocorrido com status ***400 Bad Request***
- a informação ***loginId*** deverá ser recebida via path variable, e refere-se a identificação do cliente (UserType customer), o que quer dizer que o valor de um login válido efetuado via módulo ***fiap.sample.login*** deve ter sido obtido
    - ***loginId*** (deve conter no máximo 25 caracteres, necessário para o tamaho de um _id do MongoDB, que é de onde vem esta informação), campo mandatório
        - [validar] deve ser verificado se o ***loginId*** é de fato válido para o tipo (UserType) 'customer'
- o resultado deve ter o payload com os dados adicionais, e devolvido com status ***201 Created***
    - ***code*** o campo é calculado pelo aplicação quando inserir um pedido, é formado pelo prefixo **"ORD-"** e 7 digitos (apenas números)

```$ curl localhost:8282/stock/users/5ef9589c2994931e98c15365/orders -d '{ "products: [ { "code": "PRD-9876543", "quantity": "10" } ] }' -H 'Content-Type: application/json' ```

```
[request]
PUT stock/users/5ef9589c2994931e98c15365/orders
{
    "products: [
        {
            "code": "PRD-9876543",
            "quantity": "10"
        }
    ]
}

[response]
201 Created
{
    "code": "ORD-4569877",
    "products: [
        {
            "code": "PRD-9876543",
            "quantity": "10"
        }
    ],
    "status": "WAITING_FOR_ANSWER"
}
```

---

#### 2.4 - [use case: estoquista lista pedidos]
- estoquista requisita listagem de pedidos persistidos
- a informação ***loginId*** deverá ser recebida via path variable, e refere-se a identificação do estoquista (UserType stock), o que quer dizer que o valor de um login válido efetuado via módulo ***fiap.sample.login*** deve ter sido obtido
    - [validar] deve ser verificado se o ***loginId*** é de fato válido para o tipo (UserType) 'stock'
- o resultado deve ter o payload a lista completa de todos os pedidos, e devolvido com status ***200 Ok***

```$ curl localhost:8282/stock/users/5ef958b02994931e98c15366/orders```

```
[request]
GET stock/users/5ef958b02994931e98c15366/orders

[response]
200 Ok
{
    "orders: [
        {
            "code": "ORD-4569877",
            "products: [
                {
                    "code": "PRD-9876543",
                    "quantity": "10"
                }
            ],
            "status": "WAITING_FOR_ANSWER"
        }
    ]
}
```

---

#### 2.5 - [use case: estoquista confirma um pedido]
- o estoquista seleciona um item da lista de pedidos, e **confirma/aprova** a presença deste no estoque, indicando que a compra/pedido deve continuar seu fluxo
    - [validar] o estado (status) do pedido deve estar no valor ***"WAITING_FOR_ANSWER"***, de maneira contrária, devolver mensagem informando do problema com status ***412 Precondition Failed***
    - [validar] os produtos e suas quantidades presentes no pedido devem continuar integros na base de dados local **MySQL**, logo é necessário confirmar que todos os produtos de fato tem tais quantidades disponíveis
- a informação ***loginId*** deverá ser recebida via path variable, e refere-se a identificação do estoquista (UserType stock), o que quer dizer que o valor de um login válido efetuado via módulo ***fiap.sample.login*** deve ter sido obtido
    - [validar] deve ser verificado se o ***loginId*** é de fato válido para o tipo (UserType) 'stock'
- o resultado deve ter o payload com os dados adicionais, e devolvido com status ***200 Ok***
- [IMPORTANTE] uma vez validado e aceito/confirmado o pedido na base local **MySQL**;
    - é necessário atualizar os dados sumarizados deste pedido na base de dados do módulo ***fiap.stock.portal***, o objeto completo (agora atualizado na base local) através da sua propriedade ***code*** deve ser utilizado para passar para o serviço que sumariza o estado atual deste produto, e com esta informação sumarizada dispara um evento para módulo ***fiap.stock.portal*** atulizar na sua base a sua própria versão do ***domínio Product***
    - é necessário atualizar o ***domínio Order*** no módulo ***fiap.stock.portal***, então devemos obter o objeto resultante, e disparar um evento para módulo ***fiap.stock.portal*** atulizar na sua base a sua própria versão do ***domínio Order***, agora com a informação do **status=APPROVED**

```$ curl stock/users/5ef958b02994931e98c15366/orders/ORD-4569877/aprove -X PUT -H 'Content-Type: application/json' ```

```
[request]
PUT stock/users/5ef958b02994931e98c15366/orders/ORD-4569877/aprove

[response]
200 Ok
{
    "code": "ORD-4569877",
    "products: [
        {
            "code": "PRD-9876543",
            "quantity": "10"
        }
    ],
    "status": "APPROVED"
}
```

---

#### 2.6 - [use case: estoquista rejeita um pedido]
- o estoquista seleciona um item da lista de pedidos, e **rejeita** a presença deste no estoque, indicando que a compra/pedido deve ser cancelada
    - [validar] o estado (status) do pedido deve estar no valor ***"WAITING_FOR_ANSWER"***, de maneira contrária, devolver mensagem informando do problema com status ***412 Precondition Failed***
- a informação ***loginId*** deverá ser recebida via path variable, e refere-se a identificação do estoquista (UserType stock), o que quer dizer que o valor de um login válido efetuado via módulo ***fiap.sample.login*** deve ter sido obtido
    - [validar] deve ser verificado se o ***loginId*** é de fato válido para o tipo (UserType) 'stock'
- o resultado deve ter o payload com os dados adicionais, e devolvido com status ***200 Ok***
- [IMPORTANTE] uma vez rejeitado o pedido na base local **MySQL**, é necessário atualizar o ***domínio Order*** no módulo ***fiap.stock.portal***, então devemos obter o objeto resultante, e disparar um evento para módulo ***fiap.stock.portal*** atulizar na sua base a sua própria versão do ***domínio Order***, agora com a informação do **status=REJECTED**

```$ curl stock/users/5ef958b02994931e98c15366/orders/ORD-4569877/reject -X PUT -H 'Content-Type: application/json' ```

```
[request]
PUT stock/users/5ef958b02994931e98c15366/orders/ORD-4569877/reject

[response]
200 Ok
{
    "code": "ORD-4569877",
    "products: [
        {
            "code": "PRD-9876543",
            "quantity": "10"
        }
    ],
    "status": "REJECTED"
}
```

---

#utf-8
#language: pt

Funcionalidade: Testes Fornecedor

Cenário: Cadastrando fornecedor com sucesso.
Dado ao entrar no cadastro do fornecedor
Quando preencher os dados e pressionar para cadastrar
Então valida o cadastro

Cenário: Fornecedor com cnpj já cadastrado
Dado ao entrar no cadastro do fornecedor
Quando tenta cadastrar cnpj ja cadastrado
Então invalida cadastro fornecedor cnpj já cadastrado

Cenário: Fornecedor com cnpj inválido
Dado ao entrar no cadastro do fornecedor
Quando tenta cadastrar cnpj invalido
Então invalida cadastro cnpj invalido

Cenário: Fornecedor com dados vazios
Dado ao entrar no cadastro do fornecedor
Então botao de cadatro desabilitado

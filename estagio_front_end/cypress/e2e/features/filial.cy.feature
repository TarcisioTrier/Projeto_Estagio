#utf-8
#language: pt

Funcionalidade: Testes Filial

Cenário: Cadastrar Filial com sucesso
Dado ao entrar no cadastro da filial
Quando preencher os dados e pressionar para cadastrar
Então valida o cadastro

Cenário: Erro ao cadastrar Filial com cnpj já cadastrado
Dado ao entrar no cadastro da filial
Quando tenta cadastrar cnpj ja cadastrado
Então lanca mensagem de erro CNPJ já cadastrado em outra empresa

Cenário: Filial não pode ser cadastrada com cnpj invalido
Dado ao entrar no cadastro da filial
Quando tenta cadastrar cnpj invalido
Então invalida cadastro cnpj invalido

Cenário: Filial com campos vazios
Dado ao entrar no cadastro da filial
Então botao de cadatro desabilitado

Cenário: Entrando em uma filial
Dado ao entrar em uma filial
Então aparece a filial que entrou

Cenário: Saindo de uma filial
Dado ao entrar em uma filial
Quando clicar para sair
Então deve voltar para o cadastro

Cenário: verificando opcoes de filial
Dado ao entrar em uma filial
Quando clica no menu de opcoes
Então deve aparecer as opcoes

Cenário: Tentando entrar em filial inexistente
Dado entra na pagina inicial
Quando tenta entrar em uma filial inexistente
Então deve retornar para a pagina inicial

Cenário: Encontrando cnpj na API
Dado ao entrar no cadastro da filial
Quando colocar um cnpj
Então deve receber um request de 200OK
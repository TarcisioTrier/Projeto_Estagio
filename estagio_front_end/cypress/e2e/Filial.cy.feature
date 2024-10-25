#utf-8
#language: pt

Funcionalidade: Cadastro de Filial

Cenário: Cadastrar Filial com sucesso
Dado ao entrar no cadastro da filial
Quando cadastra uma nova filial
Então valida o cadastro da filial

Cenário: Erro ao cadastrar Filial com cnpj já cadastrado
Dado ao entrar no cadastro da filial
Quando tenta cadastrar uma nova filial com cnpj já cadastrado
Então lanca mensagem de erro CNPJ já cadastrado em outra empresa

Cenário: Filial não pode ser cadastrada com cnpj invalido
Dado ao entrar no cadastro da filial
Quando tenta cadastrar uma nova filial com cnpj invalido
Então lança mensagem de erro de CNJP inválido

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
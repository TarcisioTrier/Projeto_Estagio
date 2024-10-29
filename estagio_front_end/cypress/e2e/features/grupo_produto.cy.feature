#utf-8
#language: pt

Funcionalidade: Testes Grupo Produto

Cenário: Cadastrando Grupo Produto com sucesso.
Dado ao entrar no cadastro do grupo de produto
Quando preenche os dados de grupo produto
Então valida o cadastro

Cenário: Grupo de Produto com campos vazios
Dado ao entrar no cadastro do grupo de produto
Então botao de cadatro desabilitado

Cenário: Tentativa de por uma margem maior que 100%
Dado ao entrar no cadastro do grupo de produto
Quando preenche a margem invalida
Então a margem se corrige para o valor maximo
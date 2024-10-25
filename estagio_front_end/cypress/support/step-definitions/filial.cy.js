import { Given, When, Then } from 'cypress-cucumber-preprocessor/steps';

Given("ao entrar no cadastro da filial", ()=>{
    cy.acessarCadastro()
})
When("cadastra uma nova filial",()=>{
    cy.preencheFilial()
    cy.cadastraFilial()
})
Then("valida o cadastro da filial", ()=>{
    cy.get('.p-toast-message-content').should('contain', 'Sucesso')
})

When("tenta cadastrar uma nova filial com cnpj já cadastrado", () =>{
    cy.preencheFilialCnpjCadastrado()
    cy.cadastraFilial()
})
Then("lanca mensagem de erro CNPJ já cadastrado em outra empresa", ()=>{
    cy.get('.p-toast-message-content').should('contain', 'CNPJ já cadastrado em outra empresa.')
})

When("tenta cadastrar uma nova filial com cnpj invalido", () =>{
    cy.preencheFilialCnpjInvalido()
    cy.cadastraFilial()
})
Then("lança mensagem de erro de CNJP inválido", ()=>{
    cy.get('.p-toast-message-content').should('contain', 'CNPJ inválido')
})

Then("botao de cadatro desabilitado", ()=>{
    cy.get('p-button[label="Cadastrar"]').should('have.css', 'pointer-events', 'none')
})

Given("ao entrar em uma filial", ()=>{
    cy.acessarFilial()
})
Then("aparece a filial que entrou", ()=>{
    cy.get('p.ng-star-inserted').should("be.visible")
})

Given("ao entrar em uma filial", ()=>{
    cy.acessarFilial()
})
When("clicar para sair",()=>{
    cy.get('.pi-user').click()
    cy.get('.p-element.ng-star-inserted > .p-ripple').click()
})
Then("deve voltar para o cadastro",()=>{
    cy.url().should('eq', 'http://localhost:4200/filial/cadastro')
})

Given("ao entrar em uma filial", ()=>{
    cy.acessarFilial()
    
})
When("clica no menu de opcoes",()=>{
    cy.get('.p-toolbar-group-left > .flex > .pi').click()
})
Then("deve aparecer as opcoes", ()=>{
    cy.get('#pn_id_6_1_header > .p-panelmenu-header-content > .p-panelmenu-header-action').should('be.visible')
    cy.get('#pn_id_6_2_header > .p-panelmenu-header-content > .p-panelmenu-header-action').should('be.visible')
    cy.get('#pn_id_6_3_header > .p-panelmenu-header-content > .p-panelmenu-header-action').should('be.visible')
})

Given('entra na pagina inicial',()=>{
    cy.visit('/')
})

When('tenta entrar em uma filial inexistente',()=>{
    cy.acessarFilialInexistente()
})

Then('deve retornar para a pagina inicial',() =>{
    cy.get('p.ng-star-inserted').should('not.exist')
})
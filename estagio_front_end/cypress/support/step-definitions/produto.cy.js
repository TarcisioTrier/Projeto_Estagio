import { Given, When, Then } from "cypress-cucumber-preprocessor/steps";

Given("ao entrar no cadastro de produto", ()=>{
    cy.entraCadastroProduto()
})

When("preenche os dados de produto",()=>{
    cy.preencheDadosProduto()
    cy.botaoCadastro()
})
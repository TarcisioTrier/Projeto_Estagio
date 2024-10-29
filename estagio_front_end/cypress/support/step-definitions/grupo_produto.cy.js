import { Given, When, Then } from "cypress-cucumber-preprocessor/steps";

Given("ao entrar no cadastro do grupo de produto", () => {
  cy.entraCadastroGrupoProduto();
});

When("preenche os dados de grupo produto", () => {
  cy.preencheGrupoProduto();
  cy.botaoCadastro();
});

When("preenche a margem invalida",()=>{
    cy.preencheMargemInvalida();
})

Then("a margem se corrige para o valor maximo",()=>{
    cy.get("#minmaxfraction").blur().should('have.value', "100,00%")
})

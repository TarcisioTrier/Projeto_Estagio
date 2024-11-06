import { Given, When, Then } from "cypress-cucumber-preprocessor/steps";

Given("ao entrar no cadastro do fornecedor", () => {
  cy.entraCadastroFornecedor();
});

Then("invalida cadastro fornecedor cnpj já cadastrado", () => {
  cy.get(".p-toast-message-content").should(
    "contain",
    "CNPJ de fornecedor já cadastrado nesta empresa."
  );
});

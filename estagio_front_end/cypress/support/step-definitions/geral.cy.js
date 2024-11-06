Given("entra na pagina inicial", () => {
  cy.visit("/");
});

When("preencher os dados e pressionar para cadastrar", () => {
  cy.preencheDados();
  cy.botaoCadastro();
});

When("tenta cadastrar cnpj ja cadastrado", () => {
  cy.preencheCnpjCadastrado();
  cy.botaoCadastro();
});

When("tenta cadastrar cnpj invalido", () => {
  cy.preencheCnpjInvalido();
  cy.botaoCadastro();
});

Then("valida o cadastro", () => {
  cy.get(".p-toast-message-content").should("contain", "Sucesso");
});

Then("invalida cadastro cnpj invalido", () => {
  cy.get(".p-toast-message-content").should("contain", "CNPJ inválido");
});

Then("botao de cadatro desabilitado", () => {
  cy.get('p-button[label="Cadastrar"]').should(
    "have.css",
    "pointer-events",
    "none"
  );
});

Cypress.Commands.add("entraCadastroFornecedor", () => {
  cy.acessarFilial();
  cy.get(".p-toolbar-group-left > .flex > .pi").click();
  cy.contains(".p-menuitem-text", "Fornecedor").click();
  cy.contains(
    ".p-menuitem-content > .p-menuitem-link",
    "Cadastro de fornecedor"
  ).click();
});

Cypress.Commands.add("entraCadastroFornecedor", () => {
  cy.acessarFilial();
  cy.get(".p-toolbar-group-left > .flex > .pi").click();
  cy.contains(".p-panelmenu-header-content > .p-panelmenu-header-action", "Fornecedor").click();
  cy.get('.p-component-overlay').click()
});

import { faker } from "@faker-js/faker";

//TESTES DE CADASTRO DE FILIAL

const nomeFantasia = faker.company.name();
const razaoSocial = faker.company.buzzPhrase();
const telefone = faker.phone.number("national");
const email = faker.internet.email();

Cypress.Commands.add("acessarCadastro", () => {
  cy.visit("/");
  cy.get(".p-toolbar-group-left > .flex > .pi").click();
  cy.contains(
    ".p-panelmenu-header-content > .p-panelmenu-header-action", "Filiais"
  ).click();
  cy.visit("/filial");
});

Cypress.Commands.add("testaApiCnpj", () => {
  cy.intercept("GET", "**/services/http/**", (req) => {
    req.continue((res) => {
      expect(res.statusCode).to.eq(200);
    });
  }).as("cnpjRequest");

  cy.get("@cnpjRequest").then(() => {
    cy.log("Request de CNPJ interceptado com sucesso e verificado.");
  });
});

//TESTES PARA ENTRAR EM FILIAL

Cypress.Commands.add("acessarFilial", () => {
  cy.visit("/");
  cy.get(".pi-user").click();
  cy.get(".m-1 > .p-ripple").click();
  cy.get(".p-autocomplete > .p-ripple").click();
  cy.get(".p-autocomplete-panel").scrollTo(0, 200);
  cy.get("#pn_id_4_5").click();
  cy.get(".justify-content-end > p-button.p-element > .p-ripple").click();
});

Cypress.Commands.add("acessarFilialInexistente", () => {
  cy.get(".pi-user").click();
  cy.get(".m-1 > .p-ripple").click();
  cy.get(".p-autocomplete-input").type("filial inválida");
  cy.get(
    ".justify-content-end > p-button.p-element > .p-ripple > .p-button-label"
  ).click();
});

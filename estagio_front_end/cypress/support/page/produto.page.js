import { faker } from "@faker-js/faker";

const nomeProduto = faker.commerce.productName();
const descricao = faker.commerce.productDescription();
const codigoBarra = faker.commerce.isbn({
  variant: 13,
  separator: "",
});
const margemLucro = faker.commerce.price({
  min: 0,
  max: 100,
  dec: 0,
});
const valorProduto = faker.commerce.price({
  min: 0,
});

Cypress.Commands.add("entraCadastroProduto", () => {
  cy.acessarFilial();
  cy.get(".p-toolbar-group-left > .flex > .pi").click();
  cy.contains(".p-menuitem-text", /^Produto$/).click();
  cy.get('.p-component-overlay').click()
});

Cypress.Commands.add("preencheDadosProduto", () => {
  cy.intercept("GET", "**").as("allRequests");
  cy.get("#codigoBarras > .p-inputtext").type(codigoBarra);
  cy.get("#nome").type(nomeProduto);
  cy.get("#descricao").type(descricao);
  cy.get(".p-autocomplete-dropdown").click();
  cy.get(".p-autocomplete-panel li")
    .should("have.length.greaterThan", 0)
    .then((options) => {
        cy.buscaAleatoria(options)
    });
  cy.contains(".p-dropdown-label > .ng-star-inserted", "Apresentação").click();
  cy.get(".p-dropdown-item")
    .should("have.length.greaterThan", 0)
    .then((options) => {
        cy.buscaAleatoria(options)
    });

    cy.contains(".p-dropdown-label > .ng-star-inserted", "Tipo de Produto").click();
  cy.get(".p-dropdown-item")
    .should("have.length.greaterThan", 0)
    .then((options) => {
      cy.buscaAleatoria(options)
    });

    cy.get("#margemLucro").type(margemLucro);
    cy.get('#valorProduto').type(valorProduto);
});

Cypress.Commands.add("buscaAleatoria", (options) => {
  const randomIndex = Math.floor(Math.random() * options.length);
  const randomOption = options[randomIndex];
  cy.wrap(randomOption).click();
});

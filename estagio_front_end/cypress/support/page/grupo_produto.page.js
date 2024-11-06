import { faker } from "@faker-js/faker";

const nomeGrupo = faker.commerce.department();
const margemLucro = faker.commerce.price({
  min: 0,
  max: 100,
  dec: 0,
});

const margemLucroInvalida = faker.commerce.price({
  min: 100,
});

Cypress.Commands.add("entraCadastroGrupoProduto", () => {
  cy.acessarFilial();
  cy.get(".p-toolbar-group-left > .flex > .pi").click();
  cy.contains(".p-menuitem-text", "Grupo de Produto").click();
  cy.contains(
    ".p-menuitem-content > .p-menuitem-link",
    "Cadastro de grupo de produto"
  ).click();
});

Cypress.Commands.add("preencheGrupoProduto", () => {
  cy.get("#nomeGrupo").type(nomeGrupo);
  cy.get("#minmaxfraction").type(margemLucro);
  cy.get(".p-dropdown-label").click();
  cy.get(".p-dropdown-item").contains("Outros").click();
  cy.get(".p-checkbox-box").click();
});

Cypress.Commands.add("preencheMargemInvalida", () =>{
    cy.get("#minmaxfraction").type(margemLucroInvalida);
})

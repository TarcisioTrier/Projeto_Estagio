const INPUT_EMAIL = '#email';
const INPUT_SENHA = '#passwd';
const BTN_LOGIN = '#SubmitLogin';

Cypress.Commands.add('realizarLogin', () => {
  cy.get(INPUT_EMAIL).type('pipipipopopo@pimail.com');
  cy.get(INPUT_SENHA).type('pipipipopopo');
  cy.get(BTN_LOGIN).click();
})

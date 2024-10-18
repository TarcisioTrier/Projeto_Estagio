const BTN_ACESSA_LOGIN = '.login'
const BNT_CONTA = '.account'

Cypress.Commands.add('acessarLogin', () => {
  cy.get(BTN_ACESSA_LOGIN).click()
})

Cypress.Commands.add('validarLogin', () => {
 cy.get(BNT_CONTA).should('be.visible')
})

Given('que acesso o portal my shop',()=>{
  cy.visit('/')
 })

When('realizo login no portal',()=>{
  cy.acessarLogin()
  cy.realizarLogin()
})
Then('valido que estou logado no portal',()=>{
  cy.validarLogin()
})

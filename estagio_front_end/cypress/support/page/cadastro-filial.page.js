import { faker } from '@faker-js/faker';


//TESTES DE CADASTRO DE FILIAL

const nomeFantasia = faker.company.name();
const razaoSocial = faker.company.buzzPhrase();
const telefone = faker.phone.number('national');
const email = faker.internet.email();


Cypress.Commands.add('acessarCadastro', () => {
    cy.visit("/");
    cy.get(".p-toolbar-group-left > .flex > .pi").click();
    cy.get('#pn_id_5_1_header > .p-panelmenu-header-content > .p-panelmenu-header-action').click();
    cy.get('#pn_id_5_1_0 > .p-menuitem-content > .p-menuitem-link').click();
    cy.visit("/filial/cadastro");
  })

Cypress.Commands.add("preencheFilial",()=>{
    cy.intercept('GET', '**').as('allRequests');
    cy.gerarCNPJValido().then((cnpj) => {
      cy.get('#cnpj').type(cnpj);
    cy.wait(1000);
    cy.wait('@allRequests', { timeout: 10000 }).then((interception) => {
      cy.get('body').then(($body) => {
        if ($body.find('.p-toast-message-text').length > 0) {
          cy.get('.p-toast-message-text').should('contain', 'Erro');
          cy.verificaPreencheCampos(nomeFantasia, razaoSocial, telefone, email);
        } else {
          cy.verificaPreencheCampos(nomeFantasia, razaoSocial, telefone, email);
        }
      });
    });
  })
});

Cypress.Commands.add("preencheFilialCnpjCadastrado",() =>{
  cy.get('#cnpj').type("00.678.661/3250-45");
  cy.verificaPreencheCampos(nomeFantasia, razaoSocial, telefone, email);
})

Cypress.Commands.add("preencheFilialCnpjInvalido",() =>{
  cy.get('#cnpj').type("00.678.661/3250-41");
  cy.verificaPreencheCampos(nomeFantasia, razaoSocial, telefone, email);
})

    Cypress.Commands.add("cadastraFilial", () =>{
        cy.get('p-button[label="Cadastrar"]').should('not.be.disabled');
        cy.get('p-button[label="Cadastrar"]').click();
    });

    Cypress.Commands.add('verificaPreencheCampos', (nomeFantasia, razaoSocial, telefone, email) => {
      cy.get('#nomeFantasia').then(($input) => {
        if (!$input.val()) {
          cy.get('#nomeFantasia').type(nomeFantasia); 
        }
      });
    
      cy.get('#razaoSocial').then(($input) => {
        if (!$input.val()) {
          cy.get('#razaoSocial').type(razaoSocial); 
        }
      });
    
      cy.get('#telefone > .p-inputtext').then(($input) => {
        if (!$input.val()) {
          cy.get('#telefone > .p-inputtext').type(telefone); 
        }
      });
    
      cy.get('#email').then(($input) => {
        if (!$input.val()) {
          cy.get('#email').type(email); 
        }
      });

      cy.get('.p-dropdown-label').click();
      cy.get('.p-dropdown-item').contains("Ativo").click();
    });

    Cypress.Commands.add('gerarCNPJValido', () => {
      const calcularDigitoVerificador = (cnpjBase) => {
        const calcularDigito = (str, peso) => {
          let soma = 0;
          for (let i = 0; i < str.length; i++) {
            soma += parseInt(str[i], 10) * peso[i];
          }
          const resto = soma % 11;
          return resto < 2 ? 0 : 11 - resto;
        };
    
        const primeiroDigito = calcularDigito(cnpjBase, [5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2]);
        const segundoDigito = calcularDigito(cnpjBase + primeiroDigito, [6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2]);
        
        return `${primeiroDigito}${segundoDigito}`;
      };
    
      const cnpjBase = String(Math.floor(Math.random() * 999999999999)).padStart(12, '0');
      const digitosVerificadores = calcularDigitoVerificador(cnpjBase);
      const cnpjCompleto = `${cnpjBase}${digitosVerificadores}`;

      return cnpjCompleto.replace(/(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})/, '$1.$2.$3/$4-$5');
    });

    //TESTES PARA ENTRAR EM FILIAL

    Cypress.Commands.add("acessarFilial", () =>{
      cy.visit('/')
      cy.get('.pi-user').click()
      cy.get('.m-1 > .p-ripple').click()
      cy.get('.p-autocomplete > .p-ripple').click()
      cy.get('.p-autocomplete-panel')
      .scrollTo(0,200);
      cy.get('#pn_id_4_5').click()
      cy.get('.justify-content-end > p-button.p-element > .p-ripple').click()
    })

    Cypress.Commands.add('acessarFilialInexistente',()=>{
      cy.get('.pi-user').click()
      cy.get('.m-1 > .p-ripple').click()
      cy.get('.p-autocomplete-input').type("filial inválida")
      cy.get('.justify-content-end > p-button.p-element > .p-ripple > .p-button-label').click()
    })
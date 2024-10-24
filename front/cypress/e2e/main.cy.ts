/// <reference types="cypress" />

describe('Home Page Load', () => {
  beforeEach(() => {
    cy.visit('/');
  });

  it('should display the application title', () => {
    cy.get('.mat-typography').should('contain', 'Yoga app');
  });

  it('should display the toolbar with Login and Register buttons', () => {
    cy.get('.mat-toolbar').should('be.visible');

    // Vérifiez que les boutons Login et Register sont présents
    cy.get('span').contains('Login').should('be.visible');
    cy.get('span').contains('Register').should('be.visible');
  });
});

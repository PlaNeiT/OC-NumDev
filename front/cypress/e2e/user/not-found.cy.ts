/// <reference types="cypress" />

describe('NotFoundComponent', () => {
  beforeEach(() => {
    // Visiter la page 404
    cy.visit('/404');
  });

  it('should load the Not Found page with a heading', () => {
    // Vérifie que le h1 avec le texte "Page not found !" est visible
    cy.get('h1').contains('Page not found !').should('be.visible');
  });
});

/// <reference types="cypress" />

describe('UnauthGuard - Public Route', () => {
  beforeEach(() => {
    cy.intercept('GET', '/api/session', (req) => {
      req.reply({
        statusCode: 200,
        body: {
          isLogged: false,
        },
      });
    });
  });

  it('should allow access to public route when not logged in', () => {
    cy.visit('/login');

    cy.url().should('include', '/login');
  });

  it('should redirect to home page when logged in', () => {
    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: {
        isLogged: true,
      },
    });

    cy.visit('/login');

    cy.url().should('include', '/');
  });
});

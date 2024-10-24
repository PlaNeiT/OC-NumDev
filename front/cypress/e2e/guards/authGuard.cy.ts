/// <reference types="cypress" />

describe('AuthGuard', () => {
  beforeEach(() => {
    cy.intercept('GET', '/api/session', {
      statusCode: 401,
    });

    cy.visit('/sessions');
  });

  it('should redirect to login page if user is not logged in', () => {
    cy.url().should('include', '/login');
  });
});

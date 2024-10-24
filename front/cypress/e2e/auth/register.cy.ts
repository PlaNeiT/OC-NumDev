/// <reference types="cypress" />

describe('User Registration', () => {
  beforeEach(() => {
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 201,
      body: { message: 'User registered successfully!' },
    }).as('registerUser');

    cy.visit('/register');
  });

  it('should navigate to the Register page', () => {
    cy.url().should('include', '/register');
  });

  it('should remain disabled when attempting to register with missing fields', () => {
    cy.get('button[type="submit"]').should('be.disabled');
    cy.get('button[type="submit"]').click({ force: true });
    cy.get('button[type="submit"]').should('be.disabled');
  });

  it('should register a user with valid inputs', () => {
    cy.get('input[formControlName=email]').type('will.smith@gmail.com');
    cy.get('input[formControlName=firstName]').type('Will');
    cy.get('input[formControlName=lastName]').type('Smith');
    cy.get('input[formControlName=password]').type('test123');

    cy.get('button[type="submit"]').should('not.be.disabled').click({ force: true });

    cy.wait('@registerUser').its('response.statusCode').should('eq', 201);
    cy.url().should('include', '/login');
  });
});

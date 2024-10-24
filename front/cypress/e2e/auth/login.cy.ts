/// <reference types="cypress" />

describe('Login page tests', () => {
  beforeEach(() => {
    // Interception pour une tentative de login réussie
    cy.intercept('POST', '/api/auth/login', (req) => {
      const { email, password } = req.body;
      if (email === 'yoga@studio.com' && password === 'test!1234') {
        req.reply({
          statusCode: 200,
          body: {
            token: 'mu17ip455',
            type: 'Bearer',
            id: 1,
            username: 'yoga@studio.com',
            firstName: 'Yoga',
            lastName: 'Studio',
          },
        });
      } else {
        req.reply({
          statusCode: 401, // Echec de la connexion
        });
      }
    }).as('loginRequest');

    // Visite de la page de login avant chaque test
    cy.visit('/login');
  });

  it('should navigate to sessions page on successful login', () => {
    // Saisie des informations de login valides
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type('test!1234');
    cy.get('button[type="submit"]').click();

    // Attendre la réponse réussie et vérifier la redirection
    cy.wait('@loginRequest');
    cy.url().should('include', '/sessions');
  });

  it('should stay on login page on failed login', () => {
    // Saisie des informations de login incorrectes
    cy.get('input[formControlName=email]').type('wrong@studio.com');
    cy.get('input[formControlName=password]').type('wrongpassword');
    cy.get('button[type="submit"]').click();

    // Attendre la réponse échouée
    cy.wait('@loginRequest');

    // Vérifier que l'utilisateur reste sur la page de login
    cy.url().should('include', '/login');
  });
});

import { defineConfig } from "cypress";

export default defineConfig({
  e2e: {
    viewportWidth: 1280,
    viewportHeight: 720,
    watchForFileChanges: false,
    specPattern: 'cypress/e2e/**/*.feature',
    baseUrl: 'http://localhost:4200/',
    setupNodeEvents(on, config) {
      const cucumber = require('cypress-cucumber-preprocessor').default
      on('file:preprocessor', cucumber())
    },
  },
});

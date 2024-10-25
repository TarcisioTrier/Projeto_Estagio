import { defineConfig } from "cypress";
const cucumber = require('cypress-cucumber-preprocessor').default

export default defineConfig({
  e2e: {
    viewportWidth: 1280,
    viewportHeight: 720,
    watchForFileChanges: false,
    specPattern: 'cypress/e2e/features/*.feature',
    baseUrl: 'http://localhost:4200/',
    setupNodeEvents(on, config) {
      on('file:preprocessor', cucumber())
    },
  },
});

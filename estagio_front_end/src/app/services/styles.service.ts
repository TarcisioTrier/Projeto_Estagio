import { DOCUMENT } from '@angular/common';
import { inject, Injectable } from '@angular/core';
import { Theme } from '../models/app-enums';

@Injectable({
  providedIn: 'root',
})
export class StylesService {
  isDarkMode = true;
  #document = inject(DOCUMENT);

  constructor() {
    this.initializeTheme();
  }

  private initializeTheme() {
    const linkElement = this.#document.getElementById(
      'app-theme'
    ) as HTMLLinkElement;
    this.isDarkMode = linkElement.href.includes('dark');
  }

  toggleLightDark() {
    const linkElement = this.#document.getElementById(
      'app-theme'
    ) as HTMLLinkElement;

    if (this.isDarkMode) {
      linkElement.href = Theme.LIGHT;
      this.isDarkMode = false;
    } else {
      linkElement.href = Theme.DARK;
      this.isDarkMode = true;
    }
  }

  isDarkModeEnabled(): boolean {
    return this.isDarkMode;
  }
}

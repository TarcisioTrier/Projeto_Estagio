import { DOCUMENT } from '@angular/common';
import { inject, Injectable } from '@angular/core';
import { Theme } from '../models/app-enums';

@Injectable({
  providedIn: 'root',
})
export class StylesService {
  #document = inject(DOCUMENT);

  constructor() {
    this.initializeTheme();
  }

  private initializeTheme() {
    const linkElement = this.#document.getElementById(
      'app-theme'
    ) as HTMLLinkElement;
    if (this.isDarkModeEnabled()) {
      linkElement.href = Theme.DARK;
    } else {
      linkElement.href = Theme.LIGHT;
    }
  }

  toggleLightDark() {
    const linkElement = this.#document.getElementById(
      'app-theme'
    ) as HTMLLinkElement;
    if (this.isDarkModeEnabled()) {
      linkElement.href = Theme.LIGHT;
      sessionStorage.setItem('darkTheme', "false");
    } else {
      linkElement.href = Theme.DARK;
      sessionStorage.setItem('darkTheme', "true");
    }
    console.log(sessionStorage.getItem('darkTheme'));
  }

  isDarkModeEnabled(): boolean {
    const data = sessionStorage.getItem('darkTheme');
    if(data == "true")
    return true;
  return false;
  }

  systemIsDark(): string{
    return window.matchMedia('(prefers-color-scheme: dark)').matches.toString();
  }
}

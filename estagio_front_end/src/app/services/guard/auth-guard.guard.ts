import { inject } from '@angular/core';
import { CanActivateChildFn, Router } from '@angular/router';
import { MessageService } from 'primeng/api';

export const AuthGuard: CanActivateChildFn = (childRoute, state) => {
  const message = inject(MessageService);
  const router = inject(Router);
  const isAuthenticated: boolean = sessionStorage.getItem('filial') !== null;

  if (!isAuthenticated) {
    router.navigate(['']);
    message.add({
      severity: 'error',
      summary: 'Erro',
      detail: 'Filial não selecionada',
    });
    return false;
  }
  return true;
};

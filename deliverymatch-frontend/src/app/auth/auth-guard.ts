import { CanActivateChildFn, Router } from '@angular/router';

export const authGuard: CanActivateChildFn = (childRoute, state) => {
  const token = localStorage.getItem('jwt');
  if (token) {
    return true;
  } else {
    // Optionally, redirect to login
    window.location.href = '/auth/login';
    return false;
  }
};

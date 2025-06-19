import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: 'auth',
    loadChildren: () => import('./auth/auth.module').then(m => m.AuthModule)
  },
  {
    path: 'profile',
    loadChildren: () => import('./profile/profile-module').then(m => m.ProfileModule)
  },
  { path: 'login', redirectTo: 'auth/login', pathMatch: 'full' },
  { path: 'register', redirectTo: 'auth/register', pathMatch: 'full' },
  { path: '', redirectTo: 'profile', pathMatch: 'full' },
  {
    path: 'driver/dashboard',
    loadComponent: () => import('./driver/dashboard').then(m => m.Dashboard)
  },
  {
    path: 'shipper/dashboard',
    loadComponent: () => import('./shipper/dashboard').then(m => m.Dashboard)
  }
];

import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';
import { adminGuard } from './guards/admin.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', loadComponent: () => import('./components/login/login.component').then(m => m.LoginComponent) },
  { path: 'register', loadComponent: () => import('./components/register/register.component').then(m => m.RegisterComponent) },
  { path: 'chat', loadComponent: () => import('./components/chat/chat.component').then(m => m.ChatComponent), canActivate: [authGuard] },
  // Products and Clients management requires ADMIN role (mirrors backend ChatController permissions)
  { path: 'products', loadComponent: () => import('./components/products/products.component').then(m => m.ProductsComponent), canActivate: [adminGuard] },
  { path: 'clients', loadComponent: () => import('./components/clients/clients.component').then(m => m.ClientsComponent), canActivate: [adminGuard] },
  { path: '**', redirectTo: '/login' }
];

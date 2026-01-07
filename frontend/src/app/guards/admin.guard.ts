import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth.service';

/**
 * Guard that restricts access to ADMIN users only.
 * Mirrors backend logic from ChatController.java:
 * - Admin users have full access
 * - Non-admin users are redirected to chat
 */
export const adminGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // First check if authenticated
  if (!authService.isAuthenticated()) {
    router.navigate(['/login']);
    return false;
  }

  // Then check if admin
  if (authService.isAdmin()) {
    return true;
  }

  // Non-admin users are redirected to chat (they can only view products, not manage)
  router.navigate(['/chat']);
  return false;
};

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';

export interface LoginResponse {
  'access-token': string;
}

export interface User {
  username: string;
  password: string;
  email?: string;
}

export interface JwtPayload {
  sub: string;
  roles?: string[];
  exp?: number;
  iat?: number;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly apiUrl = 'http://localhost:8888/auth-service';
  private readonly tokenKey = 'jwt_token';
  private readonly isAuthenticatedSubject = new BehaviorSubject<boolean>(this.hasToken());

  isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  constructor(private readonly http: HttpClient) {}

  login(username: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, { username, password }).pipe(
      tap(response => {
        this.setToken(response['access-token']);
        this.isAuthenticatedSubject.next(true);
      })
    );
  }

  register(user: User): Observable<any> {
    return this.http.post(`${this.apiUrl}/users`, user);
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    this.isAuthenticatedSubject.next(false);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  private setToken(token: string): void {
    localStorage.setItem(this.tokenKey, token);
  }

  private hasToken(): boolean {
    return !!this.getToken();
  }

  isAuthenticated(): boolean {
    return this.hasToken();
  }

  /**
   * Decode JWT token payload (without verification - verification is done server-side)
   */
  private decodeToken(): JwtPayload | null {
    const token = this.getToken();
    if (!token) {
      return null;
    }

    try {
      const parts = token.split('.');
      if (parts.length !== 3) {
        return null;
      }
      const payload = parts[1];
      const decoded = atob(payload.replaceAll('-', '+').replaceAll('_', '/'));
      return JSON.parse(decoded) as JwtPayload;
    } catch {
      return null;
    }
  }

  /**
   * Get user roles from JWT token
   */
  getRoles(): string[] {
    const payload = this.decodeToken();
    return payload?.roles ?? [];
  }

  /**
   * Check if current user has ADMIN role
   * Mirrors backend logic: roles.contains("ADMIN")
   */
  isAdmin(): boolean {
    const roles = this.getRoles();
    return roles.includes('ADMIN');
  }

  /**
   * Get current user ID (subject) from JWT token
   */
  getUserId(): string {
    const payload = this.decodeToken();
    return payload?.sub ?? 'anonymous';
  }

  /**
   * Check if user has a specific role
   */
  hasRole(role: string): boolean {
    const roles = this.getRoles();
    return roles.includes(role);
  }
}

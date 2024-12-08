import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router) {}

  canActivate(): boolean {
    const token = localStorage.getItem('token');
    const lastActivity = localStorage.getItem('lastActivity');

    if (token && !this.isTokenExpired(token) && !this.isInactive(lastActivity)) {
      return true; // Token och aktivitet är giltiga, tillåt navigering
    }

    this.router.navigate(['/login']); // Omdirigera till inloggning vid ogiltig token eller inaktivitet
    return false;
  }

  private isTokenExpired(token: string): boolean {
    try {
      const payload = JSON.parse(atob(token.split('.')[1])); // Dekoda JWT payload
      const exp = payload.exp * 1000; // JWT-utgångstid i millisekunder
      return Date.now() > exp; // Kontrollera om nuvarande tid är efter utgångstid
    } catch (e) {
      return true; // Om något går fel, anta att token är ogiltig
    }
  }

  private isInactive(lastActivity: string | null): boolean {
    if (!lastActivity) return false; // Om ingen aktivitet är sparad, anta att användaren är aktiv

    const inactivityLimit = 30 * 60 * 1000; // 30 minuter
    const lastActivityTime = new Date(lastActivity).getTime();
    return (Date.now() - lastActivityTime) > inactivityLimit; // Kontrollera om användaren har varit inaktiv för länge
  }
}

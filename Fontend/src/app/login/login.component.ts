import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  username: string = ''; // Inmatat användarnamn
  password: string = ''; // Inmatat lösenord
  isLoading: boolean = false;
  public successMessage: string = '';
  public errorMessage: string = '';

  constructor(private http: HttpClient, private router: Router) {}

  login() {
    this.isLoading = true;
    this.http.post('/api/auth/login', { username: this.username, password: this.password })
      .subscribe({
        next: (response: any) => {
          // Spara JWT-token och användaruppgifter i localStorage
          localStorage.setItem('token', response.token); // Spara JWT-token
          localStorage.setItem('userName', response.userName); // Spara användarnamn
          localStorage.setItem('userId', response.userId); // Spara användarens ID

          this.successMessage = 'Inloggning OK';

          setTimeout(() => {
            this.router.navigate(['/dashboard']);
          }, 1000);

          this.isLoading = false;
        },
        error: (err) => {
          if (err.status === 404) {
            this.errorMessage = 'Användaren finns inte!';
          } else if (err.status === 401) {
            this.errorMessage = 'Felaktigt användarnamn eller lösenord!';
          } else {
            this.errorMessage = 'Ett oväntat fel uppstod!';
          }
          this.isLoading = false;
        }
      });
  }

  // Metod för att navigera till registreringssidan
  goToRegister() {
    this.router.navigate(['/register']);
  }
}

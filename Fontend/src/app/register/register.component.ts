import {HttpClient, HttpClientModule} from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import {Component} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  user = { username: '', email: '', password: '' };
  // Injektera både HttpClient och Router
  constructor(private http: HttpClient, private router: Router) {}

  onSubmit() {
    console.log('Submitting form...');
    console.log('User data:', this.user);

    this.http.post('http://localhost:8080/api/users', this.user)
      .subscribe({
        next: (response) => {
          console.log('Registration successful!', response);
          alert('Registration successful! You will be redirected to the login page.');
          this.router.navigate(['/login']);
        },
        error: (error) => {
          console.error('Registration failed!', error);
          alert('Registration failed. Please try again.');
        }
      });
  }
}

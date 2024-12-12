import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import {TopbarComponent} from '../topbar/topbar.component';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatInputModule,
    MatButtonModule,
    TopbarComponent
  ]
})
export class EditProfileComponent implements OnInit {
  user = { name: '', email: '', userId: 1 }; // Justera userId efter behov
  passwordData = { currentPassword: '', newPassword: '' };
  changePasswordActive = false;

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.loadUserProfile();
  }

  loadUserProfile(): void {
    const userId = this.user.userId;
    this.http.get(`/api/users/profile?userId=${userId}`).subscribe({
      next: (data: any) => {
        this.user = data;
      },
      error: (err) => {
        console.error('Error loading profile:', err);
      }
    });
  }

  updateProfile(): void {
    this.http.put('/api/users/profile/update', this.user).subscribe({
      next: (response: any) => {
        alert(response.message || 'Profilen har uppdaterats');
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        console.error('Error updating profile:', err);
      }
    });
  }

  toggleChangePassword(): void {
    this.changePasswordActive = !this.changePasswordActive;
  }

  changePassword(): void {
    this.http.put('/api/users/profile/change-password', this.passwordData).subscribe({
      next: (response: any) => {
        alert(response.message || 'Lösenordet har ändrats');
        this.toggleChangePassword();
      },
      error: (err) => {
        alert('Fel: ' + (err.error.message || 'Misslyckades med att ändra lösenordet'));
      }
    });
  }
}

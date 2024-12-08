import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatMenuModule } from '@angular/material/menu'; // Importera MatMenuModule
import { MatIconModule } from '@angular/material/icon'; // Importera MatIconModule
import { MatButtonModule } from '@angular/material/button'; // Importera MatButtonModule
import { MatFormFieldModule } from '@angular/material/form-field'; // Importera MatFormFieldModule
import { MatInputModule } from '@angular/material/input'; // Importera MatInputModule
import { SidebarComponent } from '../sidebar/sidebar.component'; // Importera SidebarComponent

@Component({
  selector: 'app-topbar',
  standalone: true,
  templateUrl: './topbar.component.html',
  styleUrls: ['./topbar.component.scss'],
  imports: [
    CommonModule,
    MatMenuModule,         // För mat-menu
    MatIconModule,         // För mat-icon
    MatButtonModule,       // För mat-button och mat-icon-button
    MatFormFieldModule,    // För mat-form-field
    MatInputModule,
    SidebarComponent,
  ],
})
export class TopbarComponent {
  dropdownOpen = false;
  userName: string | null = null;
  sidebarOpen = true; // Styr synlighet på sidomenyn

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.userName = localStorage.getItem('userName') || 'Användare';
  }

  toggleDropdown(): void {
    this.dropdownOpen = !this.dropdownOpen;
  }

  toggleSidebar(): void {
    this.sidebarOpen = !this.sidebarOpen; // Toggle sidebar synlighet
  }

  navigateToUpload(): void {
    this.router.navigate(['/upload']);
  }

  navigateToDashboard(): void {
    this.router.navigate(['/dashboard']);
  }

  navigateToEditProfile(): void {
    this.router.navigate(['/profile/edit']);
  }

  logout(): void {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}

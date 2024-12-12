import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { TopbarComponent } from '../topbar/topbar.component';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { Router, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    TopbarComponent,
    SidebarComponent,
    RouterOutlet, // För att visa children routes
  ],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
})
export class ProfileComponent {
  sidebarOpen = false;
  activeTab: string = 'profile';

  constructor(private router: Router) {}

  toggleSidebar(open: boolean): void {
    this.sidebarOpen = open;
  }

  navigateToSection(section: string): void {
    this.activeTab = section;
    this.router.navigate([`/profile/${section}`]); // Navigera till den valda sektionen
  }
}

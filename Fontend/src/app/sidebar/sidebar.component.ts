import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatSidenavModule } from '@angular/material/sidenav'; // Importera MatSidenavModule
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
  imports: [
    CommonModule,
    MatSidenavModule,     // För mat-sidenav
    MatButtonModule,
  ]
})
export class SidebarComponent {
  @Input() sidebarOpen: boolean = true; // Mottar värdet från topbar för att hantera sidomenyns synlighet
}

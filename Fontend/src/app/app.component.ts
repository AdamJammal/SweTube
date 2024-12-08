import {Component, OnDestroy, OnInit} from '@angular/core';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterModule,    // Lägg till RouterModule här
    MatButtonModule, // Lägg till Material-moduler för de komponenter du använder
    MatIconModule    // Lägg till MatIcon-modulen om du använder ikoner
  ],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, OnDestroy {
  title = 'angularclient';

  ngOnInit(): void {
    // Lägg till event listeners för att övervaka aktivitet
    document.body.addEventListener('mousemove', this.updateActivityTime);
    document.body.addEventListener('keydown', this.updateActivityTime);
    document.body.addEventListener('click', this.updateActivityTime);
  }

  updateActivityTime(): void {
    localStorage.setItem('lastActivity', new Date().toISOString());
  }

  ngOnDestroy(): void {
    // Ta bort event listeners för att förhindra minnesläckor
    document.body.removeEventListener('mousemove', this.updateActivityTime);
    document.body.removeEventListener('keydown', this.updateActivityTime);
    document.body.removeEventListener('click', this.updateActivityTime);
  }
}

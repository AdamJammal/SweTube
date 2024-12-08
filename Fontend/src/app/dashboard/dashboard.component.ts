import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router'; // Importera Router för navigation
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';  // Importera DomSanitizer
import { CommonModule } from '@angular/common';  // För ngFor, ngIf
import { TopbarComponent } from '../topbar/topbar.component';  // Importera TopbarComponent
import { MatCardModule } from '@angular/material/card';  // Importera MatCardModule
import { MatIconModule } from '@angular/material/icon';  // Importera MatIconModule

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  standalone: true,  // Sätt 'standalone' till true
  imports: [
    CommonModule,  // Viktigt för direktiv som *ngFor, *ngIf
    MatCardModule,  // För Material Cards
    MatIconModule,  // För Material Ikoner
    TopbarComponent  // Importera TopbarComponent
  ]
})
export class DashboardComponent implements OnInit {
  public videos: any[] = []; // Lista för att lagra videor från backend

  constructor(private http: HttpClient, private router: Router, private sanitizer: DomSanitizer) {}

  ngOnInit(): void {
    this.fetchVideos(); // Hämta videor när komponenten initieras
  }

  goToVideo(videoId: number, videoUrl: string): void {
    console.log('Navigating to video with ID:', videoId);

    // Skapa säker YouTube embed-URL med autoplay
    const youtubeEmbedUrl = this.createSafeEmbedUrl(videoUrl);

    // Navigera till WatchVideoComponent och skicka videoId och youtubeEmbedUrl som state
    this.router.navigate([`/video/${videoId}`], { state: { youtubeEmbedUrl } });
  }

  // Skapa säker YouTube embed-URL med autoplay
  createSafeEmbedUrl(videoUrl: string): SafeResourceUrl {
    const youtubeId = this.extractYouTubeId(videoUrl);
    if (youtubeId) {
      const embedUrl = `https://www.youtube.com/embed/${youtubeId}?autoplay=1`;
      return this.sanitizer.bypassSecurityTrustResourceUrl(embedUrl);  // Sanera URL här
    }
    return '';  // Återvänd en tom sträng om YouTube ID inte kan extraheras
  }

  fetchVideos(): void {
    this.http.get<any[]>('http://localhost:8080/api/videos').subscribe({
      next: (response) => {
        this.videos = response; // Lagra de hämtade videorna
        console.log('Fetched videos:', this.videos);
      },
      error: (err) => {
        console.error('Error fetching videos:', err);
      }
    });
  }

  // Extrahera YouTube video-ID från URL
  extractYouTubeId(url: string): string | null {
    const regex = /(?:https?:\/\/)?(?:www\.)?(?:youtube\.com\/(?:[^\/\n\s]+\/\S+\/|(?:v|e(?:mbed)?)\/|\S*?[?&]v=)|youtu\.be\/)([a-zA-Z0-9_-]{11})/;
    const match = url.match(regex);
    return match ? match[1] : null;
  }
}

import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';
import { TopbarComponent } from '../topbar/topbar.component';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatIconModule,
    TopbarComponent,
    SidebarComponent,
  ]
})
export class DashboardComponent implements OnInit {
  public videos: any[] = [];
  sidebarOpen = true; // Sidomenyn är initialt öppen

  constructor(private http: HttpClient, private router: Router, private sanitizer: DomSanitizer) {}

  ngOnInit(): void {
    this.fetchVideos();
  }

  goToVideo(videoId: number, videoUrl: string): void {
    const youtubeEmbedUrl = this.createSafeEmbedUrl(videoUrl);
    this.router.navigate([`/video/${videoId}`], { state: { youtubeEmbedUrl } });
  }

  createSafeEmbedUrl(videoUrl: string): SafeResourceUrl {
    const youtubeId = this.extractYouTubeId(videoUrl);
    if (youtubeId) {
      const embedUrl = `https://www.youtube.com/embed/${youtubeId}?autoplay=1`;
      return this.sanitizer.bypassSecurityTrustResourceUrl(embedUrl);
    }
    return '';
  }

  fetchVideos(): void {
    this.http.get<any[]>('http://localhost:8080/api/videos').subscribe({
      next: (response) => (this.videos = response),
      error: (err) => console.error('Error fetching videos:', err),
    });
  }

  extractYouTubeId(url: string): string | null {
    const regex = /(?:https?:\/\/)?(?:www\.)?(?:youtube\.com\/(?:[^\/\n\s]+\/\S+\/|(?:v|e(?:mbed)?)\/|\S*?[?&]v=)|youtu\.be\/)([a-zA-Z0-9_-]{11})/;
    const match = url.match(regex);
    return match ? match[1] : null;
  }

  toggleSidebar(status: boolean): void {
    this.sidebarOpen = status;
  }
}

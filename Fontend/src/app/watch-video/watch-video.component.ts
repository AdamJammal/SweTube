import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';  // Importera CommonModule
import { MatSidenavModule } from '@angular/material/sidenav';  // Importera MatSidenavModule
import { MatCardModule } from '@angular/material/card';  // Importera MatCardModule
import { MatIconModule } from '@angular/material/icon';  // Importera MatIconModule
import { FormsModule } from '@angular/forms';  // För ngModel och ngClass
import { TopbarComponent } from '../topbar/topbar.component';  // Importera TopbarComponent

@Component({
  selector: 'app-watch-video',
  standalone: true,
  templateUrl: './watch-video.component.html',
  styleUrls: ['./watch-video.component.scss'],
  imports: [
    CommonModule,
    MatSidenavModule,
    MatCardModule,
    MatIconModule,
    FormsModule,
    TopbarComponent  // Importera TopbarComponent här
  ]
})
export class WatchVideoComponent implements OnInit {
  videoId!: number;
  userId: number | null = null;
  videoUrl!: string;
  youtubeVideoId!: string;
  videoTitle!: string;
  userName: string = 'Laddar...';
  userAvatar: string = 'https://via.placeholder.com/48';
  videoDescription!: string;
  safeVideoUrl!: SafeResourceUrl;
  videoStarted = false;
  videoViews: number = 0;
  videoLikes: number = 0;
  videoDislikes: number = 0;
  videoDate!: string;
  comments: any[] = [];
  newCommentText: string = '';
  isCommentValid: boolean = false;
  relatedVideos: any[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private sanitizer: DomSanitizer,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    const storedUserId = localStorage.getItem('userId');
    if (storedUserId) {
      this.userId = +storedUserId;
    } else {
      console.error('User ID not found in localStorage');
      return;
    }

    const videoIdParam = this.route.snapshot.paramMap.get('id');
    if (videoIdParam) {
      this.videoId = +videoIdParam;
      this.initializePage();
    } else {
      console.error('Video ID not found in the URL');
      this.router.navigate(['/dashboard']);
    }
  }

  initializePage(): void {
    this.fetchVideoFromBackend();
    this.fetchLikesAndDislikes();
    this.fetchRecommendedVideos();
    this.fetchComments();
  }

  fetchVideoFromBackend(): void {
    this.http.get<any>(`http://localhost:8080/api/videos/${this.videoId}`).subscribe({
      next: (video) => {
        this.videoTitle = video.videoTitle || 'Ingen titel tillgänglig';
        this.videoDescription = video.videoDescription || 'Ingen beskrivning tillgänglig';
        this.videoDate = video.videoUploadDate || 'Okänd';
        this.videoViews = video.videoViews || 0;
        this.videoLikes = video.videoLikes || 0;
        this.videoDislikes = video.videoDislikes || 0;

        const extractedId = this.extractYouTubeId(video.videoUrl);
        if (extractedId) {
          this.youtubeVideoId = extractedId;
          const embedUrl = `https://www.youtube.com/embed/${this.youtubeVideoId}?autoplay=1`;
          this.safeVideoUrl = this.sanitizer.bypassSecurityTrustResourceUrl(embedUrl);
        }
      },
      error: (err) => {
        console.error('Error fetching video details from backend:', err.message);
        this.router.navigate(['/dashboard']);
      },
    });
  }

  extractYouTubeId(url: string): string | null {
    const regex = /(?:https?:\/\/)?(?:www\.)?(?:youtube\.com\/(?:[^\/\n\s]+\/\S+\/|(?:v|e(?:mbed)?)\/|\S*?[?&]v=)|youtu\.be\/)([a-zA-Z0-9_-]{11})/;
    const match = url.match(regex);
    return match ? match[1] : null;
  }

  startVideo(): void {
    if (!this.videoStarted) {
      this.videoStarted = true;
      const embedUrl = `https://www.youtube.com/embed/${this.youtubeVideoId}?autoplay=1`;
      this.safeVideoUrl = this.sanitizer.bypassSecurityTrustResourceUrl(embedUrl);
    }
  }

  fetchLikesAndDislikes(): void {
    this.http.get<number>(`http://localhost:8080/api/videos/${this.videoId}/like`).subscribe({
      next: (likeCount) => {
        this.videoLikes = likeCount;
      },
      error: (err) => {
        console.error('Error fetching likes:', err.message);
      },
    });

    this.http.get<number>(`http://localhost:8080/api/videos/${this.videoId}/dislike`).subscribe({
      next: (dislikeCount) => {
        this.videoDislikes = dislikeCount;
      },
      error: (err) => {
        console.error('Error fetching dislikes:', err.message);
      },
    });
  }

  fetchRecommendedVideos(): void {
    this.http.get<any[]>('http://localhost:8080/api/videos').subscribe({
      next: (response) => {
        this.relatedVideos = response.map((video) => ({
          id: video.videoId,
          title: video.videoTitle || 'Titel saknas',
          thumbnail: video.videoThumbnailUrl || 'https://via.placeholder.com/120x67',
          views: video.videoViews || 0,
          uploadDate: video.videoUploadDate || 'Datum saknas',
        }));
      },
      error: (err) => {
        console.error('Error fetching recommended videos:', err.message);
      },
    });
  }

  fetchComments(): void {
    this.http.get<any[]>(`http://localhost:8080/api/comments/video/${this.videoId}`).subscribe({
      next: (response) => {
        this.comments = response.map((comment) => ({
          userName: comment.userName || 'Okänd användare',
          avatarUrl: comment.avatarUrl || 'https://via.placeholder.com/48',
          text: comment.text,
          createdAt: comment.createdAt,
        }));
      },
      error: (err) => {
        console.error('Error fetching comments:', err.message);
      },
    });
  }

  addComment(): void {
    if (!this.isCommentValid || !this.userId) {
      console.error('Kommentar eller användar-ID saknas.');
      return;
    }

    const commentPayload = {
      videoId: this.videoId,
      userId: this.userId,
      text: this.newCommentText.trim(),
    };

    this.http.post('http://localhost:8080/api/comments', commentPayload).subscribe({
      next: () => {
        this.newCommentText = '';
        this.isCommentValid = false;
        this.fetchComments();
      },
      error: (err) => {
        console.error('Error adding comment:', err.message);
      },
    });
  }

  onInputChange(): void {
    this.isCommentValid = this.newCommentText.trim().length > 0;
  }
}

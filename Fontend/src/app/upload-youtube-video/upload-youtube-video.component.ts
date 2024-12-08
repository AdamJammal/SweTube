import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { TopbarComponent } from '../topbar/topbar.component';

@Component({
  selector: 'app-upload-youtube-video',
  standalone: true,
  templateUrl: './upload-youtube-video.component.html',
  imports: [
    FormsModule,
    SidebarComponent,
    TopbarComponent
  ],
  styleUrls: ['./upload-youtube-video.component.scss']
})
export class UploadYoutubeVideoComponent {
  youtubeLink: string | null = null;
  videoTitle: string = '';
  videoDescription: string = '';

  constructor(private route: ActivatedRoute, private http: HttpClient, private router: Router) {
  }

  ngOnInit(): void {
    // Hämta YouTube-länken från query params
    this.youtubeLink = this.route.snapshot.queryParamMap.get('link');
    console.log('YouTube Link från query params:', this.youtubeLink); // Debugging

    // Verifiera userId i localStorage
    const userId = localStorage.getItem('userId');
    console.log('Hämtat userId från localStorage i ngOnInit:', userId); // Debugging
  }

  saveDetails(): void {
    // Kontrollera att alla fält är ifyllda
    if (!this.videoTitle || !this.videoDescription || !this.youtubeLink) {
      alert('Please fill in all fields.');
      console.log('Fält som saknas:', {
        videoTitle: this.videoTitle,
        videoDescription: this.videoDescription,
        youtubeLink: this.youtubeLink
      });
      return;
    }

    // Hämta userId från localStorage och säkerställ att det är ett nummer
    const userId = localStorage.getItem('userId');
    const parsedUserId = userId ? Number(userId) : null;
    console.log('Parsed User ID:', parsedUserId);

    if (parsedUserId === null) {
      alert('User is not logged in or ID is missing.');
      this.router.navigate(['/login']);  // Skicka användaren till inloggningssidan om ingen användare är inloggad
      return;
    }

    // Skapa objekt med videodetaljer
    const videoData = {
      videoTitle: this.videoTitle,
      videoDescription: this.videoDescription,
      videoUrl: this.youtubeLink,
      videoUploadType: 'YouTube',
      videoUploadedBy: parsedUserId,  // Skickar med användar-ID
      videoUploadDate: new Date().toISOString().split('T')[0]
    };
    console.log('Video Data:', videoData);

    // Skicka POST-begäran till backend, inklusive userId som RequestParam
    this.http.post(`/api/videos?userId=${parsedUserId}`, videoData).subscribe({
      next: () => {
        alert('Video uploaded successfully!');
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        console.error('Error uploading video:', err);
        alert('Failed to upload video.');
      }
    });
  }
}

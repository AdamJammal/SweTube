import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-upload-video',
  standalone: true, // Gör detta till en standalone-komponent
  templateUrl: './upload-video.component.html',
  styleUrls: ['./upload-video.component.scss'],
})
export class UploadVideoComponent {
  videoId: string | null = null;
  videoTitle: string = '';
  videoDescription: string = '';

  constructor(private route: ActivatedRoute, private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.videoId = this.route.snapshot.paramMap.get('videoId');
  }

  saveDetails(): void {
    if (!this.videoId || !this.videoTitle || !this.videoDescription) {
      alert('Please fill in all fields.');
      return;
    }

    const updatedData = {
      videoTitle: this.videoTitle,
      videoDescription: this.videoDescription,
    };

    this.http.put(`http://localhost:8080/api/videos/${this.videoId}`, updatedData).subscribe({
      next: () => {
        alert('Details saved successfully!');
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        console.error('Error saving video details:', err);
        alert('Failed to save video details.');
      },
    });
  }
}

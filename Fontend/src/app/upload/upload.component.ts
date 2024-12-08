import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TopbarComponent } from '../topbar/topbar.component';
import { SidebarComponent } from '../sidebar/sidebar.component';
import {Router} from '@angular/router';

@Component({
  selector: 'app-upload',
  standalone: true,
  imports: [CommonModule, FormsModule, TopbarComponent, SidebarComponent],
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.scss']
})
export class UploadComponent {
  youtubeLink: string = '';
  selectedFile: File | null = null;
  message: string = '';

  constructor(private router: Router) {}

  // Modal hantering
  isLinkModalOpen = false;
  isFileModalOpen = false;

  showLinkModal(): void {
    this.isLinkModalOpen = true;
    this.isFileModalOpen = false;
  }

  showFileModal(): void {
    this.isFileModalOpen = true;
    this.isLinkModalOpen = false;
  }

  closeModals(): void {
    this.isLinkModalOpen = false;
    this.isFileModalOpen = false;
  }

  submitYoutubeLink(): void {
    if (!this.youtubeLink.trim().match(/^(https?:\/\/)?(www\.)?(youtube\.com|youtu\.?be)\/.+$/)) {
      this.message = 'Please enter a valid YouTube link.';
      return;
    }

    this.router.navigate(['/upload-youtube-video'], { queryParams: { link: this.youtubeLink } });
    this.closeModals();
  }


  onFileSelected(event: any): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
      this.message = `Selected file: ${this.selectedFile.name}`;
    }
  }

  uploadVideo(): void {
    if (!this.selectedFile) {
      this.message = 'Please select a video file first.';
      return;
    }

    const formData = new FormData();
    formData.append('file', this.selectedFile);

    console.log('Uploading file:', this.selectedFile.name);
    this.message = 'Video file uploaded!';
    this.closeModals();
  }
}

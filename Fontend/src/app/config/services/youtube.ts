import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable, throwError} from 'rxjs';

@Injectable({
  providedIn: 'root', // Gör tjänsten globalt tillgänglig
})
export class Youtube {
  private apiKey = 'AIzaSyA4bS-IiFvLfHP_7rRztRYkxNV38r5uIrg'; // Ersätt med din API-nyckel
  private apiUrl = 'https://www.googleapis.com/youtube/v3';

  constructor(private http: HttpClient) {}

  getVideoDetails(videoId: string): Observable<any> {
    if (!videoId) {
      console.error('Invalid YouTube video ID');
      return throwError(() => new Error('Invalid YouTube video ID'));
    }

    const url = `https://www.googleapis.com/youtube/v3/videos?part=snippet,contentDetails,statistics&id=${videoId}&key=${this.apiKey}`;
    return this.http.get<any>(url);
  }

}

import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-profile-section',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    FormsModule,
  ],
  templateUrl: './profile-section.component.html',
  styleUrls: ['./profile-section.component.scss'],
})
export class ProfileSectionComponent {
  userAvatar: string = 'https://via.placeholder.com/40'; // Exempelbild för användaravatar

  activities = [
    {
      description: 'Gillat låten...',
      title: 'Jonas Alberg',
      subtitle: 'Bäst i test',
      platform: 'på Spotify',
      timeAgo: '1 timme sedan',
    },
    {
      description: 'Gillat låten...',
      title: 'Anna Svensson',
      subtitle: 'Topplåtar',
      platform: 'på Spotify',
      timeAgo: '2 dagar sedan',
    },
    {
      description: 'Gillat låten...',
      title: 'Erik Karlsson',
      subtitle: 'Mina favoriter',
      platform: 'på Spotify',
      timeAgo: '5 minuter sedan',
    },
  ];

  newPostText: string = '';
  isPostValid: boolean = false;

  onInputChange(): void {
    this.isPostValid = this.newPostText.trim().length > 0;
  }

  addPost(): void {
    if (this.isPostValid) {
      console.log('New post:', this.newPostText);
      this.newPostText = ''; // Rensa textfältet
      this.isPostValid = false;
    }
  }

  friends = [
    {
      avatar: 'path/to/avatar1.jpg',
      name: 'Fredrik M.',
      username: 'FreddieBoy',
      friendsCount: '3.1k',
    },
    {
      avatar: 'path/to/avatar2.jpg',
      name: 'Lisa K.',
      username: 'LisaK',
      friendsCount: '2.5k',
    },
    {
      avatar: 'path/to/avatar3.jpg',
      name: 'John D.',
      username: 'JohnD',
      friendsCount: '1.2k',
    },
  ];

  followedArtists = [
    { name: 'Artist 1', imgUrl: 'https://via.placeholder.com/50' },
    { name: 'Artist 2', imgUrl: 'https://via.placeholder.com/50' },
    { name: 'Artist 3', imgUrl: 'https://via.placeholder.com/50' },
    { name: 'Artist 4', imgUrl: 'https://via.placeholder.com/50' },
    { name: 'Artist 5', imgUrl: 'https://via.placeholder.com/50' },
  ];

  playlists = [
    { name: 'Spellista 1', songCount: 12 },
    { name: 'Spellista 2', songCount: 9 },
    { name: 'Spellista 3', songCount: 15 },
  ];

  badges = [
    { id: 1, icon: 'star' },
    { id: 2, icon: 'favorite' },
    { id: 3, icon: 'check_circle' },
    { id: 4, icon: 'emoji_events' },
    { id: 5, icon: 'thumb_up' },
  ];

  activeIndex = 0;

  navigateCarousel(direction: 'next' | 'prev'): void {
    const totalActivities = this.activities.length;

    this.activeIndex =
      direction === 'next'
        ? (this.activeIndex + 1) % totalActivities
        : (this.activeIndex - 1 + totalActivities) % totalActivities;
  }
}

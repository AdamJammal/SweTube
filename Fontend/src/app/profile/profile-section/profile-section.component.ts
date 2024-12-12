import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button'; // Korrekt stavning
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-profile-section',
  standalone: true,
  imports: [CommonModule, MatButtonModule, MatCardModule], // Lägg till Material-moduler här
  templateUrl: './profile-section.component.html',
  styleUrls: ['./profile-section.component.scss']
})
export class ProfileSectionComponent {
  activities = [
    { description: 'Gillat låten...', title: 'Jonas Alberg', subtitle: 'Bäst i test', platform: 'på Spotify', timeAgo: '1 timme sedan' },
    { description: 'Gillat låten...', title: 'Anna Svensson', subtitle: 'Topplåtar', platform: 'på Spotify', timeAgo: '2 dagar sedan' },
    { description: 'Gillat låten...', title: 'Erik Karlsson', subtitle: 'Mina favoriter', platform: 'på Spotify', timeAgo: '5 minuter sedan' },
  ];

  followedArtists = [
    { name: 'Artist 1', imgUrl: 'https://via.placeholder.com/50' },
    { name: 'Artist 2', imgUrl: 'https://via.placeholder.com/50' },
    { name: 'Artist 3', imgUrl: 'https://via.placeholder.com/50' },
    { name: 'Artist 4', imgUrl: 'https://via.placeholder.com/50' },
    { name: 'Artist 5', imgUrl: 'https://via.placeholder.com/50' },
    { name: 'Artist 6', imgUrl: 'https://via.placeholder.com/50' },
    { name: 'Artist 7', imgUrl: 'https://via.placeholder.com/50' },
    { name: 'Artist 8', imgUrl: 'https://via.placeholder.com/50' },
  ];

  activeIndex = 0; // Håller reda på vilket kort som är aktivt

  // Hanterar navigering i carouselen
  navigateCarousel(direction: 'next' | 'prev'): void {
    const totalActivities = this.activities.length;

    // Hämta alla kort
    const cards = document.querySelectorAll('.activity-card');

    cards.forEach((card, index) => {
      card.classList.remove('prev', 'next', 'active');
      if (index === this.activeIndex) {
        card.classList.add(direction === 'next' ? 'prev' : 'next');
      }
    });

    // Uppdatera activeIndex
    if (direction === 'next') {
      this.activeIndex = (this.activeIndex + 1) % totalActivities;
    } else {
      this.activeIndex = (this.activeIndex - 1 + totalActivities) % totalActivities;
    }

    // Lägg till klassen "active" på det nya aktiva kortet
    const activeCard = cards[this.activeIndex];
    if (activeCard) {
      activeCard.classList.add('active');
    }
  }
}

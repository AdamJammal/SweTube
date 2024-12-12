import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProfileComponent } from './profile.component';

const routes: Routes = [
  {
    path: '',
    component: ProfileComponent,
    children: [
      {
        path: '',
        redirectTo: 'profile-section', // Laddar profile-section som standard
        pathMatch: 'full',
      },
      {
        path: 'profile-section',
        loadComponent: () =>
          import('./profile-section/profile-section.component').then(
            (m) => m.ProfileSectionComponent
          ),
      },
      {
        path: 'playlist-section',
        loadComponent: () =>
          import('./playlist-section/playlist-section.component').then(
            (m) => m.PlaylistSectionComponent
          ),
      },
      {
        path: 'activities-section',
        loadComponent: () =>
          import('./activities-section/activities-section.component').then(
            (m) => m.ActivitiesSectionComponent
          ),
      },
      {
        path: 'awards-section',
        loadComponent: () =>
          import('./awards-section/awards-section.component').then(
            (m) => m.AwardsSectionComponent
          ),
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ProfileRoutingModule {}

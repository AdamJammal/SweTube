import { Routes } from '@angular/router';
import { RegisterComponent } from './register/register.component';
import { AuthGuard } from './config/auth.guard';
import { WatchVideoComponent } from './watch-video/watch-video.component'; // Importera WatchVideoComponent

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', loadComponent: () => import('./login/login.component').then(m => m.LoginComponent) },
  { path: 'register', component: RegisterComponent },
  {
    path: 'video/:id',  // Din rutt för att titta på en video
    loadComponent: () => import('./watch-video/watch-video.component').then(m => m.WatchVideoComponent),
    canActivate: [AuthGuard]
  },
  {
    path: 'dashboard',
    loadComponent: () => import('./dashboard/dashboard.component').then(m => m.DashboardComponent), // Lazy load DashboardComponent
    canActivate: [AuthGuard] // AuthGuard används om du vill skydda denna rutt
  },
  {
    path: 'upload',
    loadComponent: () => import('./upload/upload.component').then(m => m.UploadComponent),
    canActivate: [AuthGuard]
  },
  {
    path: 'upload-video/:videoId',
    loadComponent: () => import('./upload-video/upload-video.component').then(m => m.UploadVideoComponent),
    canActivate: [AuthGuard]
  },
  {
    path: 'upload-youtube-video',
    loadComponent: () => import('./upload-youtube-video/upload-youtube-video.component').then(m => m.UploadYoutubeVideoComponent),
    canActivate: [AuthGuard],
  },
];

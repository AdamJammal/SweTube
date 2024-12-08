import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadYoutubeVideoComponent } from './upload-youtube-video.component';

describe('UploadYoutubeVideoComponent', () => {
  let component: UploadYoutubeVideoComponent;
  let fixture: ComponentFixture<UploadYoutubeVideoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UploadYoutubeVideoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UploadYoutubeVideoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

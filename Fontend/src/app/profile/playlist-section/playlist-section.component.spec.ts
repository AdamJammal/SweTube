import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlaylistSectionComponent } from './playlist-section.component';

describe('PlaylistSectionComponent', () => {
  let component: PlaylistSectionComponent;
  let fixture: ComponentFixture<PlaylistSectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlaylistSectionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PlaylistSectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

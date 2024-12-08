import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {LoginComponent} from './login.component';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoginComponent, HttpClientTestingModule, RouterTestingModule],
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should not call login API if form is invalid', () => {
    spyOn(component, 'login');
    component.username = '';
    component.password = '';
    fixture.detectChanges();

    const button = fixture.nativeElement.querySelector('button');
    button.click();

    expect(component.login).not.toHaveBeenCalled();
  });

  it('should call login API with correct credentials', () => {
    spyOn(component, 'login').and.callThrough();
    component.username = 'testuser';
    component.password = 'testpassword';
    fixture.detectChanges();

    const button = fixture.nativeElement.querySelector('button');
    button.click();

    expect(component.login).toHaveBeenCalled();
  });
});

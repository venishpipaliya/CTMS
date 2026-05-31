import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FinanceApprovedComponent } from './finance-approved.component';

describe('FinanceApprovedComponent', () => {
  let component: FinanceApprovedComponent;
  let fixture: ComponentFixture<FinanceApprovedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FinanceApprovedComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FinanceApprovedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

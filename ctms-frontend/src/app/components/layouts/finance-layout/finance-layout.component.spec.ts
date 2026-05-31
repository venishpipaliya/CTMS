import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FinanceLayoutComponent } from './finance-layout.component';

describe('FinanceLayoutComponent', () => {
  let component: FinanceLayoutComponent;
  let fixture: ComponentFixture<FinanceLayoutComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FinanceLayoutComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FinanceLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

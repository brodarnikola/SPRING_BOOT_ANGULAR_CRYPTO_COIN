import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExchangeRateAnalyticsComponent } from './exchange-rate-analytics.component';

describe('ExchangeRateAnalyticsComponent', () => {
  let component: ExchangeRateAnalyticsComponent;
  let fixture: ComponentFixture<ExchangeRateAnalyticsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExchangeRateAnalyticsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ExchangeRateAnalyticsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

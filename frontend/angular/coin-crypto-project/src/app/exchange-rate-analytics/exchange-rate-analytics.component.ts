import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
import { ExchangeRate } from '../model/exchangeRate';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-exchange-rate-analytics',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './exchange-rate-analytics.component.html',
  styleUrls: ['./exchange-rate-analytics.component.css']
})
export class ExchangeRateAnalyticsComponent implements OnInit {
  exchangeRates: ExchangeRate[] = [];
  eurMedian: number = 0;
  usdMedian: number = 0;
  gbpMedian: number = 0;
  dateFrom: string = '';
  dateTo: string = '';

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    const today = new Date();
    const twoYearsAgo = new Date(today.getFullYear() - 2, today.getMonth(), today.getDate());
    this.dateFrom = twoYearsAgo.toISOString().split('T')[0];
    this.dateTo = today.toISOString().split('T')[0];
    this.fetchExchangeRates();
  }

  fetchExchangeRates(): void {
    this.userService.getExchangeRates(this.dateFrom, this.dateTo).subscribe(
      (data: ExchangeRate[]) => {
        this.exchangeRates = data;
        this.calculateMedians();
      },
      (error) => console.error('Error fetching exchange rates', error)
    );
  }

  calculateMedians(): void {
    const eurRates = this.exchangeRates.map(rate => rate.excRateEur).sort((a, b) => a - b);
    const usdRates = this.exchangeRates.map(rate => rate.excRateUsd).sort((a, b) => a - b);
    const gbpRates = this.exchangeRates.map(rate => rate.excRateGbp).sort((a, b) => a - b);

    this.eurMedian = this.calculateMedian(eurRates);
    this.usdMedian = this.calculateMedian(usdRates);
    this.gbpMedian = this.calculateMedian(gbpRates);
  }

  calculateMedian(values: number[]): number {
    const mid = Math.floor(values.length / 2);
    return values.length % 2 !== 0 ? values[mid] : (values[mid - 1] + values[mid]) / 2;
  }

  getColor(value: number, median: number): string {
    if (value < median) {
      return 'red';
    } else if (value > median) {
      return '#24a349';
    } else {
      return 'black';
    }
  }

  onDateChange(): void {
    this.fetchExchangeRates();
  }
}
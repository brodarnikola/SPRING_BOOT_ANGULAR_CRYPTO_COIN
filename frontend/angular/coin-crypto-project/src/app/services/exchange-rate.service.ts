import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../enviroments';
import { ExchangeRate } from '../model/exchangeRate';

@Injectable({
  providedIn: 'root'
})
export class ExchangeRateService {
  private apiUrl = `${environment.apiUrl}/api/coin`;

  constructor(private http: HttpClient) { }

  getExchangeRates(dateFrom: string, dateTo: string): Observable<ExchangeRate[]> {
    return this.http.get<ExchangeRate[]>(`${this.apiUrl}/exchange-rates?dateFrom=${dateFrom}&dateTo=${dateTo}`);
  }
}
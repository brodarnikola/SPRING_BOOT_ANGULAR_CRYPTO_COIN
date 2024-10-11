import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../enviroments';
import { ExchangeRate } from '../model/exchangeRate';
import { Page } from '../model/page';

@Injectable({
  providedIn: 'root'
})
export class ExchangeRateService {
  private apiUrl = `${environment.apiUrl}/api/coin`;

  constructor(private http: HttpClient) { }

  getExchangeRates(dateFrom: string, dateTo: string): Observable<ExchangeRate[]> {
    return this.http.get<ExchangeRate[]>(`${this.apiUrl}/exchange-rates?dateFrom=${dateFrom}&dateTo=${dateTo}`);
  }

  

  getPaginatedExchangeRates(dateFrom: string, dateTo: string, page: number, size: number): Observable<Page<ExchangeRate>> {
    const params = new HttpParams()
      .set('dateFrom', dateFrom)
      .set('dateTo', dateTo)
      .set('page', page.toString())
      .set('size', size.toString());
      
    return this.http.get<Page<ExchangeRate>>(`${this.apiUrl}/exchange-rates-paginated`, { params });
    // return this.http.get<Page<ExchangeRate>>(`${this.apiUrl}/exchange-rates-paginated?dateFrom=${dateFrom}&dateTo=${dateTo}&page=${page.toString()}&size=${size.toString()}`);
  }

}
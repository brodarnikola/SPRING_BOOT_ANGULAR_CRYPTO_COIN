import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../enviroments';
import { Wallet } from '../model/wallet';
import { WalletDetailsResponse } from '../model/walletDetailsResponse';

@Injectable({
  providedIn: 'root'
})
export class WalletService {
  private apiUrl = `${environment.apiUrl}/api/coin`;

  constructor(private http: HttpClient) { }

  getWallets(userId: number): Observable<Wallet[]> {
    return this.http.get<Wallet[]>(`${this.apiUrl}/wallets?idUser=${userId}`);
  }

  getWalletDetails(userId: number): Observable<WalletDetailsResponse> {
    return this.http.get<WalletDetailsResponse>(`${this.apiUrl}/wallets/details?idUser=${userId}`);
  }
}
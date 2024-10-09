import { Wallet } from './wallet';

export interface WalletDetailsResponse {
  wallets: Wallet[];
  totalWorth: number;
  lastPurchase: Wallet;
}
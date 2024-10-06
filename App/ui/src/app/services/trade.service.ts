import { Injectable } from '@angular/core';
import { backendUrl } from '../constants';
import { HttpClient } from '@angular/common/http';
import { interval, Observable, startWith, switchMap } from 'rxjs';
import { Transaction } from '../models/transaction';
import { Rate } from '../models/rate';

@Injectable({
  providedIn: 'root',
})
export class TradeService {
  constructor(private http: HttpClient) {}

  getTransactions() {
    return this.http.get(
      backendUrl.fxTradeService.getTransactions
    ) as Observable<Transaction[]>;
  }

  getTransactionsPolling() {
    return interval(2000).pipe(
      startWith(0),
      switchMap(() => this.http.get(backendUrl.fxTradeService.getTransactions))
    ) as Observable<Transaction[]>;
  }

  saveTransaction(transaction: Transaction) {
    return this.http.post(
      backendUrl.fxTradeService.saveTransaction,
      transaction
    ) as Observable<any>;
  }

  getCurrencies() {
    return this.http.get(backendUrl.quoteService.getCurrencies) as Observable<
      string[]
    >;
  }

  getFxRate(primaryCcy: string, secondaryCcy: string) {
    return this.http.get(backendUrl.quoteService.getFxRate, {
      params: { primaryCcy, secondaryCcy },
    }) as Observable<Rate>;
  }

  getFxRatePolling(primaryCcy: string, secondaryCcy: string) {
    return interval(2000).pipe(
      startWith(0),
      switchMap(() =>
        this.http.get(backendUrl.quoteService.getFxRate, {
          params: { primaryCcy, secondaryCcy },
        })
      )
    ) as Observable<Rate>;
  }
}

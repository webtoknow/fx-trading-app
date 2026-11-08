import { Injectable } from '@angular/core';
import { backendUrl } from '../constants';
import { HttpClient } from '@angular/common/http';
import { catchError, EMPTY, interval, Observable, of, retry, startWith, switchMap } from 'rxjs';
import { Transaction } from '../models/transaction';
import { Rate } from '../models/rate';

@Injectable({
  providedIn: 'root',
})
export class TradeService {
  constructor(private http: HttpClient) {}

  getTransactionsPolling() {
    return interval(2000).pipe(
      startWith(0),
      switchMap(() => this.http.get<Transaction[]>(backendUrl.fxTradeService.getTransactions).pipe(
        catchError(() => EMPTY)
      ))
    );
  }

  saveTransaction(transaction: Transaction) {
    return this.http.post(
      backendUrl.fxTradeService.saveTransaction,
      transaction
    ) as Observable<any>;
  }

  getCurrencies() {
    return this.http.get<string[]>(backendUrl.quoteService.getCurrencies).pipe(
      retry({ delay: 2000 })
    );
  }

  getFxRatePolling(primaryCcy: string, secondaryCcy: string) {
    return interval(2000).pipe(
      startWith(0),
      switchMap(() =>
        this.http.get<Rate>(backendUrl.quoteService.getFxRate, {
          params: { primaryCcy, secondaryCcy },
        }).pipe(
          catchError(() => of(null))
        )
      )
    );
  }
}

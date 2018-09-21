import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Transaction } from 'src/app/models/transaction';
import { Observable } from 'rxjs/internal/Observable';
import { catchError, map, tap, startWith, switchMap } from 'rxjs/operators';
import { interval } from 'rxjs';
import { backendUrl } from '../constans';

@Injectable()
export class TradeService {

    constructor(
        private http: HttpClient
    ) { }

    getTransactions() {
        return this.http.get(backendUrl.fxTradeService.getTransactions) as Observable<Transaction[]>
    }

    saveTransaction(transaction: Transaction) {
        return this.http.post(backendUrl.fxTradeService.getTransactions, { params: transaction }) as Observable<Transaction[]>
    }

    getCurrencies() {
        return this.http.get(backendUrl.quoteService.getCurrencies) as Observable<string[]>
    }

    getFxRate(primaryCCY: string, secondaryCCY: string) {
        return this.http.get(backendUrl.quoteService.getCurrencies, { params: { primaryCCY, secondaryCCY } }) as Observable<string[]>
    }

}

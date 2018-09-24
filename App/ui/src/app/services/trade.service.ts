import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Transaction } from 'src/app/models/transaction';
import { Observable } from 'rxjs/internal/Observable';
import { interval } from "rxjs";
import {startWith, switchMap} from "rxjs/operators";
import { backendUrl } from '../constants';
import { Rate } from 'src/app/models/rate';

@Injectable()
export class TradeService {

    constructor(
        private http: HttpClient
    ) { }

    getTransactions() {
        return this.http.get(backendUrl.fxTradeService.getTransactions) as Observable<Transaction[]>
    }

    saveTransaction(transaction: Transaction) {
        return this.http.post(backendUrl.fxTradeService.saveTransaction, { params: transaction }) as Observable<Transaction[]>
    }

    getCurrencies() {
        return this.http.get(backendUrl.quoteService.getCurrencies) as Observable<string[]>
    }

    getFxRate(primaryCCY: string, secondaryCCY: string) {
        return this.http.get(backendUrl.quoteService.getFxRate, { params: { primaryCCY, secondaryCCY } }) as Observable<Rate>
    }

    getFxRatePolling(primaryCCY: string, secondaryCCY: string) {
        return interval(2000)
            .pipe(
                startWith(0),
                switchMap(() => this.http.get(backendUrl.quoteService.getFxRate, { params: { primaryCCY, secondaryCCY } })
            )
        ) as Observable<Rate>
    }


}

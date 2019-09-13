
export interface Transaction {
  id?: number;
  username: string;
  primaryCcy: string
  secondaryCcy: string;
  rate: number;
  action: string;
  notional: number;
  tenor: string;
  date: number;
  ccyPair?: string
}


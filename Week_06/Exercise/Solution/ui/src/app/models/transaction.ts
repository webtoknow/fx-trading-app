export interface Transaction {
  id?: number;
  username: string;
  primaryCcy: string
  secondaryCcy: string;
  rate: number;
  action: string;
  notional: number | null;
  tenor: string;
  date: number;
  ccyPair?: string
}

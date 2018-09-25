export interface Transaction {
  transactionId?: number;
  username: string;
  primaryCCY: string
  secondaryCCY: string;
  rate: number;
  action: string;
  notional: number;
  tenor: string;
  date: number;
  CCYPair?: string
}


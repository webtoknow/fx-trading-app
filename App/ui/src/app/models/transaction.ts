export interface Transaction {
  dealId: number;
  username: string;
  CCYPair: string;
  rate: string;
  action: string;
  notional: number;
  tenor: string;
  date: number;
}

export interface Transaction {
  dealId: number;
  username: string;
  CCY: string;
  rate: string;
  action: string;
  notional: number;
  tenor: string;
  date: number;
}

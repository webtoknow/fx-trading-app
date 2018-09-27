export class Widget {
  
  constructor(
    public primaryCcy: string,
    public secondaryCcy: string,
    public buyRate: number,
    public sellRate: number,
    public notional: number,
    public tenor: string,
    public pickCCYState: boolean,
  ) {  }

}

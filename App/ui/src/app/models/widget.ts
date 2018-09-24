export class Widget {
  
  constructor(
    public primaryCCY: string,
    public secondaryCCY: string,
    public buyRate: number,
    public sellRate: number,
    public notional: string,
    public tenor: string,
    public pickCCYState: boolean,
  ) {  }

}

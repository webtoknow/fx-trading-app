export class Widget {
  
  constructor(
    public primaryCCY: string,
    public secondaryCCY: string,
    public buyRate: number,
    public sellRate: number,
    public notional: number,
    public tenor: string,
    public pickCCYState: boolean,
  ) {  }

}

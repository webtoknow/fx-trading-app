export class Widget {
  
  constructor(
    public primaryCCY: string,
    public secondaryCCY: string,
    public primaryRate: number,
    public secondaryRate: number,
    public notional: string,
    public tenor: string,
    public pickCCYState: boolean,
  ) {  }

}

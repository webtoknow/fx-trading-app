export class Widget {
  
  constructor(
    public primaryCCY: string,
    public secondaryCCY: string,
    public primaryRate: number,
    public secondaryRate: number,
    public amount: string,
    public tenor: string,
    public selectCCYState: boolean,
  ) {  }

}

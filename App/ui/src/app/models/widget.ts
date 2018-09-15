export class Widget {
  
  constructor(
    public sellCCY: string,
    public buyCCY: string,
    public sellRate: number,
    public buyRate: number,
    public amount: string,
    public tenor: string,
    public selectCCYState: boolean,
  ) {  }

}

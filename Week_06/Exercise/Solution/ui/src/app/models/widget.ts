export class Widget {

  constructor(
    public primaryCcy: string = '',
    public secondaryCcy: string = '',
    public buyRate: number = 0,
    public sellRate: number = 0,
    public notional: number | null = null,
    public tenor: string = '',
    public pickCCYState: boolean = true,
  ) { }

}

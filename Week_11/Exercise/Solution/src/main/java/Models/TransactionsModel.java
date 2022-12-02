package Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionsModel {
    private Integer id;
    private String username;
    private String primaryCcy;
    private String secondaryCcy;
    private Double rate;
    private String action;
    private long notional;
    private String tenor;
    private long date;

    public TransactionsModel(String username, String primaryCcy, String secondaryCcy, double rate, String action, long notional, String tenor, long date) {
        this.username = username;
        this.primaryCcy = primaryCcy;
        this.secondaryCcy = secondaryCcy;
        this.rate = rate;
        this.action = action;
        this.notional = notional;
        this.tenor = tenor;
        this.date = date;
    }
}

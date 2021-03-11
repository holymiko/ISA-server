package home.holymiko.ScrapApp.Server.Entity;

import com.sun.istack.NotNull;
import home.holymiko.ScrapApp.Server.Entity.Enum.Dealer;
import home.holymiko.ScrapApp.Server.Entity.Enum.TickerState;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Ticker {
    @Id
    private String ticker;
    private TickerState tickerState;

    public Ticker() {
    }

    public Ticker(String ticker, TickerState tickerState) {
        this.ticker = ticker;
        this.tickerState = tickerState;
    }

    public void setTickerState(TickerState tickerState) {
        this.tickerState = tickerState;
    }

    public String getTicker() {
        return ticker;
    }

    public TickerState getTickerState() {
        return tickerState;
    }
}

package home.holymiko.ScrapApp.Server.Entity;

import home.holymiko.ScrapApp.Server.Enum.TickerState;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
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
}

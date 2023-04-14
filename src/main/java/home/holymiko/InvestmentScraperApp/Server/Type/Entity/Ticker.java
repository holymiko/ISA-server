package home.holymiko.InvestmentScraperApp.Server.Type.Entity;

import home.holymiko.InvestmentScraperApp.Server.Type.Enum.TickerState;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ticker")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Getter
@NoArgsConstructor
public class Ticker {
    @Id
    private String ticker;
    private TickerState tickerState;

    public Ticker(String ticker, TickerState tickerState) {
        this.ticker = ticker;
        this.tickerState = tickerState;
    }

    public void setTickerState(TickerState tickerState) {
        this.tickerState = tickerState;
    }
}

package home.holymiko.investment.scraper.app.server.type.entity;

import home.holymiko.investment.scraper.app.server.type.enums.TickerState;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

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

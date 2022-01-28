package home.holymiko.InvestmentScraperApp.Server.Core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.IM_USED, reason = "Client is in use")
public class ScrapRefusedException extends RuntimeException {
    public ScrapRefusedException() {
    }

    public ScrapRefusedException(String message) {
        super(message);
    }

    public ScrapRefusedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScrapRefusedException(Throwable cause) {
        super(cause);
    }

    public ScrapRefusedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

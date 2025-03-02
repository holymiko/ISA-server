package home.holymiko.investment.scraper.app.server.core.exception;

public class ScrapFailedException extends RuntimeException {
    public ScrapFailedException() {
    }

    public ScrapFailedException(String message) {
        super(message);
    }

    public ScrapFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScrapFailedException(Throwable cause) {
        super(cause);
    }

    public ScrapFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

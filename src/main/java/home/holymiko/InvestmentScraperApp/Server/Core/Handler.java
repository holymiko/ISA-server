package home.holymiko.InvestmentScraperApp.Server.Core;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Handler {
    public static void handleIllegalArgumentException(HttpStatus status, Exception ex, HttpServletResponse response) throws IOException {
        response.setStatus(status.value());
        response.getOutputStream().write(ExceptionUtils.getMessage(ex).getBytes());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }
}

package home.holymiko.InvestmentScraperApp.Server.API.TextPort;

import home.holymiko.InvestmentScraperApp.Server.Type.Entity.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class Export {

    // TODO Move to .env (senior)
    private static final Logger LOGGER = LoggerFactory.getLogger(Export.class);
    public static final String TICKER_PATH = "txt/export/tickers/";
    private static final String STOCK_PATH = "txt/export/stocks/";
    private static final  String PDF_PATH = "txt/export/pdf";
    private static final String JSON_PATH = "txt/export/json/";
    private static final String XML_PATH = "txt/export/xml/";
    private static final String DELIMITER = "#";
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss");

    public void exportToXML(Object object) throws JAXBException {
        new File(XML_PATH).mkdirs();
        File file = new File(XML_PATH + object.getClass().getSimpleName() + "_" + dtf.format(LocalDateTime.now()) +".xml");
        JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(object, file);
    }

    public void exportToJSON(Object object) throws IOException {
        new File(JSON_PATH).mkdirs();
        FileWriter myWriter = new FileWriter(JSON_PATH + object.getClass().getSimpleName() + "_" + dtf.format(LocalDateTime.now()) +".json");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        myWriter.write(gson.toJson(object));
        myWriter.close();
    }

    static class LocalDateSerializer implements JsonSerializer<LocalDate> {
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MMM-yyyy", Locale.forLanguageTag("US"));

        @Override
        public JsonElement serialize(LocalDate localDate, Type srcType, JsonSerializationContext context) {
            return new JsonPrimitive(formatter.format(localDate));
        }
    }

    static class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime> {
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d::MMM::uuuu HH::mm::ss", Locale.forLanguageTag("US"));

        @Override
        public JsonElement serialize(LocalDateTime localDateTime, Type srcType, JsonSerializationContext context) {
            return new JsonPrimitive(formatter.format(localDateTime));
        }
    }

    static class LocalDateDeserializer implements JsonDeserializer<LocalDate> {
        @Override
        public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return LocalDate.parse(json.getAsString(),
                    DateTimeFormatter.ofPattern("d-MMM-yyyy").withLocale(Locale.ENGLISH));
        }
    }

    static class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return LocalDateTime.parse(json.getAsString(),
                    DateTimeFormatter.ofPattern("d::MMM::uuuu HH::mm::ss").withLocale(Locale.ENGLISH));
        }
    }

    public static void exportTickers(final List<Ticker> tickers) {
        LOGGER.info("Export tickers");
        try {
            final File file = new File(TICKER_PATH + LocalDate.now());
            file.mkdirs();
            final String location = file.getPath()+"/";
            final FileWriter goodWriter = new FileWriter(location+"good.txt");
            final FileWriter badWriter = new FileWriter(location+"bad.txt");
            final FileWriter notFoundWriter = new FileWriter(location+"notfound.txt");
            final FileWriter unknownWriter = new FileWriter(location+"unknown.txt");

            for (Ticker ticker : tickers) {
                String x = ticker.getTicker()+"\n";
                switch (ticker.getTickerState()) {
                    case GOOD -> goodWriter.write(x);
                    case BAD -> badWriter.write(x);
                    case NOTFOUND -> notFoundWriter.write(x);
                    case NEW -> unknownWriter.write(x);
                }
            }
            goodWriter.close();
            badWriter.close();
            notFoundWriter.close();
            unknownWriter.close();
            LOGGER.info("Successfully wrote to the file.");
        } catch (IOException e) {
            LOGGER.error("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void exportStocks(final List<GrahamStock> grahamStocks) {
        LOGGER.info("Export grahamStocks");
        try {
            final File file = new File(STOCK_PATH + LocalDate.now());
            file.mkdirs();
            final FileWriter stockWriter = new FileWriter(file.getPath()+"/stock.txt");

            for (GrahamStock grahamStock : grahamStocks) {
                String x = grahamStock.getName()+DELIMITER+
                        grahamStock.getTicker().getTicker()+DELIMITER+
                        grahamStock.getGrahamGrade()+DELIMITER+
                        grahamStock.getCurrency()+DELIMITER+

                        grahamStock.getRatingScore()+DELIMITER+
                        grahamStock.getSizeInSales()+DELIMITER+
                        grahamStock.getAssetsLiabilities()+DELIMITER+
                        grahamStock.getNetAssetsLongTermDebt()+DELIMITER+
                        grahamStock.getEarningsStability()+DELIMITER+
                        grahamStock.getDividendRecord()+DELIMITER+
                        grahamStock.getNcav()+DELIMITER+
                        grahamStock.getEquityDebt()+DELIMITER+
                        grahamStock.getSizeInAssets()+DELIMITER+

                        grahamStock.getDefensivePrice()+DELIMITER+
                        grahamStock.getEnterprisingPrice()+DELIMITER+
                        grahamStock.getNcavPrice()+DELIMITER+
                        grahamStock.getIntrinsicPrice()+DELIMITER+
                        grahamStock.getPreviousClose()+DELIMITER+
                        grahamStock.getIntrinsicValue()+"\n";
                stockWriter.write(x);
            }
            stockWriter.close();
            LOGGER.info("Successfully wrote to the file.");
        } catch (IOException e) {
            LOGGER.error("An error occurred.");
            e.printStackTrace();
        }
    }

}

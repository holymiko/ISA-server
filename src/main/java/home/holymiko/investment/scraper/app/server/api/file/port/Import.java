package home.holymiko.investment.scraper.app.server.api.file.port;

import home.holymiko.investment.scraper.app.server.service.TickerService;
import home.holymiko.investment.scraper.app.server.type.entity.Ticker;
import home.holymiko.investment.scraper.app.server.type.enums.TickerState;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class Import {
    private static final Logger LOGGER = LoggerFactory.getLogger(Import.class);

    private final TickerService tickerService;

    public Import(TickerService tickerService) {
        this.tickerService = tickerService;
    }

    ////// IMPORT

    public void importExportedTickers() throws FileNotFoundException {
        Path latestExportPath = findLastExportDirectory().toPath();
        importExportedTickers2(
                new File(latestExportPath + "/good.txt"), TickerState.GOOD
        );
        importExportedTickers2(
                new File(latestExportPath + "/bad.txt"), TickerState.BAD
        );
        importExportedTickers2(
                new File(latestExportPath + "/notfound.txt"), TickerState.NOTFOUND
        );
        importExportedTickers2(
                new File(latestExportPath + "/unknown.txt"), TickerState.NEW
        );
    }

    public void importRawTickers() {
        String date = "_20210311.txt";
        String location = "txt/src/";
        readFile(location+"nasdaqtraded.txt");
        readFile(location+"nasdaqlisted.txt");
        readFile(location+"otherlisted.txt");
        readFile2(location+"AMEX"+date);
        readFile2(location+"FOREX"+date);
        readFile2(location+"INDEX"+date);
        readFile2(location+"NASDAQ"+date);
        readFile2(location+"NYSE"+date);
        readFile2(location+"OTCBB"+date);
        readFile2(location+"USE"+date);
        readFile3(location+"YahooStockTickers.txt");
    }

    private static File findLastExportDirectory() throws FileNotFoundException {
        File dir = new File(Export.TICKER_PATH);
        if (dir.exists() && dir.isDirectory()) {
            Optional<File> opFile = Arrays.stream(Objects.requireNonNull(dir.listFiles(File::isDirectory)))
                    .max(File::compareTo);

            if (opFile.isPresent()){
                return opFile.get();
            }
        }

        throw new FileNotFoundException();
    }

    private void importExportedTickers2(File file, TickerState tickerState) {
        int i = 0;
        Set<Ticker> tickerSet = new HashSet<>();
        try {
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                tickerSet.add(new Ticker(data, tickerState));
                i++;
            }
            myReader.close();
            tickerService.saveAll(tickerSet);
            LOGGER.info("Total: "+i);
        } catch (FileNotFoundException e) {
            LOGGER.error("An error occurred.");
            e.printStackTrace();
        }
    }

    private void readFile(String fileName) {
        int a;
        int b;
        switch (fileName) {
            case "nasdaqtraded.txt":
                a = 1;
                b = 5;
                break;
            case "nasdaqlisted.txt":
                a = 0;
                b = 6;
                break;
            case "otherlisted.txt":
                a = 0;
                b = 4;
                break;
            default:
                return;
        }
        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            int i = 0;
            myReader.nextLine();
            while (myReader.hasNextLine()) {
                String[] data = myReader.nextLine().split("\\|");
                if( data.length >= 6 && !data[b].equals("Y") ) {            // No ETFs
                    tickerService.save(data[a]);
                    i++;
                } else {
                    LOGGER.info("Ticker -> Kicked");
                }
            }
            LOGGER.info("Total: "+i);
            myReader.close();
        } catch (FileNotFoundException e) {
            LOGGER.error("An error occurred.");
            e.printStackTrace();
        }
    }

    private void readFile2(String fileName) {
        int i = 0;
        int j = 0;
        int k = 0;
        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                int a = 0;
                String[] data = myReader.nextLine().split(",");
                if( data.length <= 1 ) {
                    LOGGER.info("Kicked");
                    k++;
                    continue;
                }
                if( tickerService.save(data[a]) ) {
                    i++;
                } else {
                    j++;
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            LOGGER.error("An error occurred.");
            e.printStackTrace();
        }
        LOGGER.info("Saved: "+i);
        LOGGER.info("Known: "+j);
        LOGGER.info("Kicked: "+k);
    }

    private void readFile3(String fileName) {
        if(!fileName.equals("txt/YahooStockTickers.txt")){
            LOGGER.error("Wrong file name");
            return;
        }
        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            int i = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                data = data.replace(" ", "");
                data = data.replace(".", "");
                if(data.equals("\n")){
                    continue;
                }
                tickerService.save(data);
                i++;
            }
            LOGGER.info("Total: "+i);
            myReader.close();
        } catch (FileNotFoundException e) {
            LOGGER.error("An error occurred.");
            e.printStackTrace();
        }
    }
}

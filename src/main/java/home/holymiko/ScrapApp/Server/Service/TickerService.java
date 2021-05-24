package home.holymiko.ScrapApp.Server.Service;

import home.holymiko.ScrapApp.Server.Entity.Enum.TickerState;
import home.holymiko.ScrapApp.Server.Entity.Ticker;
import home.holymiko.ScrapApp.Server.Repository.TickerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Service
public class TickerService {
    private final TickerRepository tickerRepository;

    @Autowired
    public TickerService(TickerRepository tickerRepository) {
        this.tickerRepository = tickerRepository;
    }



    ////// FIND

    public List<Ticker> findAll() { return this.tickerRepository.findAll(); }

    public List<Ticker> findByTickerState(TickerState tickerState) { return tickerRepository.findByTickerState(tickerState); }

    public Ticker findById(String x) throws ResponseStatusException{
        Optional<Ticker> optionalTicker = this.tickerRepository.findById(x);
        if( optionalTicker.isEmpty() ) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return optionalTicker.get();
    }

    private Optional<Ticker> optionalFindById(String x) { return this.tickerRepository.findById(x); }



    ////// SAVE, UPDATE, DELETE

    @Transactional
    public boolean save(String name) {
        name = name.toUpperCase();
        Optional<Ticker> optionalTicker = this.tickerRepository.findById(name);

        if( optionalTicker.isEmpty() ) {
            this.tickerRepository.save( new Ticker(name, TickerState.UNKNOWN) );
            System.out.println("Ticker - New saved");
            return true;
        }
        System.out.println("Ticker - Already known");
        return false;
    }

    @Transactional
    public boolean save(Ticker ticker) {
        if( this.tickerRepository.findById(ticker.getTicker()).isPresent() ) {
            return false;
        }
        this.tickerRepository.save(ticker);
        return true;
    }

    @Transactional
    public void update(Ticker ticker, TickerState tickerState) {
        ticker.setTickerState(tickerState);
        this.tickerRepository.save(ticker);
    }

    @Transactional
    public void delete(Ticker ticker) {
        if( this.tickerRepository.findById(ticker.getTicker()).isEmpty() ) {
            System.out.println("Delete fail, 404");
            return;
        }
        if( ticker.getTickerState() != TickerState.UNKNOWN ) {
            System.out.println("Delete not allowed, wrong state");
            return;
        }
        this.tickerRepository.delete(ticker);
    }


    ////// EXPORT

    public void exportTickers() {
        try {
            FileWriter goodWriter = new FileWriter("txt/export/good.txt");
            FileWriter badWriter = new FileWriter("txt/export/bad.txt");
            FileWriter notFoundWriter = new FileWriter("txt/export/notfound.txt");
            FileWriter unknownWriter = new FileWriter("txt/export/unknown.txt");
            List<Ticker> tickers = this.tickerRepository.findAll();
            for (Ticker ticker : tickers) {
                String x = ticker.getTicker()+"\n";
                switch (ticker.getTickerState()) {
                    case GOOD -> goodWriter.write(x);
                    case BAD -> badWriter.write(x);
                    case NOTFOUND -> notFoundWriter.write(x);
                    case UNKNOWN -> unknownWriter.write(x);
                }
            }
            goodWriter.close();
            badWriter.close();
            notFoundWriter.close();
            unknownWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    ////// IMPORT

    public void loadTickers() {
        String date = "_20210311.txt";
        String location = "txt/";
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
                    if( this.optionalFindById(data[a]).isEmpty() ) {
                        this.save(new Ticker(data[a], TickerState.UNKNOWN));
                        System.out.println("Saved new - "+data[a]);
                    } else {
                        System.out.println("Already known - "+data[a]);
                    }
                    i++;
                } else {
                    System.out.println("Kicked");
                }
            }
            System.out.println("Total: "+i);
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
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
                    System.out.println("Kicked");
                    k++;
                    continue;
                }
                if( this.optionalFindById(data[a]).isEmpty() ) {
                    this.save(new Ticker(data[a], TickerState.UNKNOWN));
                    System.out.println("Saved new - "+data[a]);
                    i++;
                } else {
                    System.out.println("Already known - "+data[a]);
                    j++;
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        System.out.println("Saved: "+i);
        System.out.println("Known: "+j);
        System.out.println("Kicked: "+k);
    }

    private void readFile3(String fileName) {
        if(!fileName.equals("txt/YahooStockTickers.txt")){
            System.out.println("Wrong file name");
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
                if( this.optionalFindById(data).isEmpty() ) {
                    this.save(new Ticker(data, TickerState.UNKNOWN));
                    System.out.println("Saved new - "+data);
                } else {
                    System.out.println("Already known - "+data);
                }
                i++;
            }
            System.out.println("Total: "+i);
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
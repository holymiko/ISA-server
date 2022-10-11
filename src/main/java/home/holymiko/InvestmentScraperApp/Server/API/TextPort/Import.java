package home.holymiko.InvestmentScraperApp.Server.API.TextPort;

import home.holymiko.InvestmentScraperApp.Server.Service.TickerService;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Import {

    private final TickerService tickerService;

    public Import(TickerService tickerService) {
        this.tickerService = tickerService;
    }

    ////// IMPORT

    public void importTickers() {
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
                    System.out.println("Ticker -> Kicked");
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
                if( tickerService.save(data[a]) ) {
                    i++;
                } else {
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
                tickerService.save(data);
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

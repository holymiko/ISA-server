package home.holymiko.InvestmentScraperApp.Server.Scraper.extractor;

import home.holymiko.InvestmentScraperApp.Server.Type.DTO.create.ProductCreateDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.*;

import java.time.Year;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Extract {

    public static final double TROY_OUNCE = 31.1034768;
    private static final double OUNCE = 28.349523125;
    private static final double TOLAR = 28.07;

    private static Producer producerExtractBasic(String text) throws IllegalArgumentException {
        if (text.contains("perth") ) {
            return Producer.PERTH_MINT;
        }
        else if(text.contains("argor")) {
            return Producer.ARGOR_HERAEUS;
        }
        else if (text.contains("heraeus")) {
            return Producer.HERAEUS;
        }
        else if (text.contains("münze osterreich") || text.contains("wiener philharmoniker")) {
            return Producer.MUNZE_OSTERREICH;
        }
        else if (text.contains("pamp")) {
            return Producer.PAMP;
        }
        else if (text.contains("valcambi")) {
            return Producer.VALCAMBI;
        }
        else if (text.contains("royal canadian mint") || text.contains("maple leaf")){
            return Producer.ROYAL_CANADIAN_MINT;
        }
        else if (text.contains("the royal mint") || text.contains("britannia")) {
            return Producer.ROYAL_MINT_UK;
        }
        else if (text.contains("usa") || text.contains("american eagle")) {
            return Producer.UNITED_STATES_MINT;
        }
        else if (text.contains("mexico") || text.contains("mexiko")) {
            return Producer.MEXICO_MINT;
        }
        else if (text.contains("umicore")) {
            return Producer.UMICORE;
        }
        else if (text.contains("sunshine mint")) {
            return Producer.SUNSHINE_MINT;
        }
        else if (text.contains("silvertowne")) {
            return Producer.SILVER_TOWNE;
        }
        else if (text.contains("golden state mint")) {
            return Producer.GOLDEN_STATE_MINT;
        }
        else if (text.contains("johnson matthey")) {
            return Producer.JOHNSON_MATTHEY;
        }
        else if (text.contains("comex")) {
            return Producer.COMEX;
        }

        throw new IllegalArgumentException("Invalid argument for Producer enum");
    }

    /**
     * Extracts Producer from text
     * @param text including producer's name. LowerCase only
     * @return Enum class Producer
     */
    private static Producer producerExtract(String text) throws IllegalArgumentException {
        try {
            return producerExtractBasic(text);
        } catch (Exception ignored){}

        if (text.contains("rok") || text.contains("kangaroo")
        || text.contains("kookaburra") || text.contains("koala") || text.contains("austrálie")
        || text.contains("austral") || text.contains(" emu ") || text.contains("drak a ")
        || text.contains("next generation") || text.contains("black flag") || text.contains("orel klínoocasý") ) {
            return Producer.PERTH_MINT;
        }
        else if (text.contains("rand") || text.contains("krugerrand")) {
            return Producer.SOUTH_AFRICAN_MINT;
        }
        else if (text.contains("moose") || text.contains("golden eagle") || text.contains("kanada")
        || text.contains("roaring grizzly") || text.contains("growling cougar") || text.contains("howling wolf")
        || text.contains("zlatá horečka na klondiku") || text.contains(" elk ")){
            return Producer.ROYAL_CANADIAN_MINT;
        }
        else if (text.contains("sovereign elizabeth") || text.contains("the royal mint")
        || text.contains("the queen´s beasts") || text.contains("the queen's beasts")
        || text.contains("royal arms") || text.contains("dva draci")
        || text.contains("mýty a legendy") || text.contains("valiant")
        || text.contains("the royal tudor beasts")  ) {
            return Producer.ROYAL_MINT_UK;
        }
        else if (text.contains("gibraltar lady justice")) {
            return Producer.SCOTTSDALE_MINT;
        }
        // From now on precision is lowered
        else if (text.contains("libertad")) {
            return Producer.MEXICO_MINT;
        }
        else if (text.contains("slon") || text.contains("leopard somálsko")) {
            return Producer.BAVARIAN_STATE_MINT;
        }
        else if (text.contains("noble isle of man") ) {
            return Producer.POBJOY_MINT;
        }
        else if (text.contains("american")) {
            return Producer.UNITED_STATES_MINT;
        }
        else if (text.contains("panda")) {
            return Producer.CHINA_MINT;
        }
        else if (text.contains("obři doby ledové")) {
            return Producer.LEIPZIGER_EDELMETALLVERARBEITUNG_GMBH;
        }
        else if (text.contains("münze")) {
            return Producer.MUNZE_OSTERREICH;
        }


        throw new IllegalArgumentException("Invalid argument for Producer enum");
    }

    /**
     * Extracts Form from text
     * @param text including name of form. LowerCase only
     * @return Enum class Form
     */
    private static Form formExtract(String text) throws IllegalArgumentException {
        if(text.contains("münzbarren")) {
            return Form.MUNZBARREN;
        }
        if(text.contains("combibar") || text.contains("combi bar") || text.contains("multidisc") || text.contains("multigram")) {
            return Form.COMBIBAR;
        }
        if(text.contains("roundbar")) {
            return Form.ROUNDBAR;
        }
        if(text.contains("paket") || text.contains("odběr 100 ks a více")) {
            return Form.PACKET;
        }
        if(text.contains("mince") || text.contains("coin") || text.contains("münze") || text.contains("tolar")) {
            return Form.COIN;
        }
        if(text.contains("kinebar")) {
            return Form.KINEBAR;
        }
        if(text.contains("multicard")) {
            return Form.MULTICARD;
        }
        if(text.contains("bar") || text.contains("slitek") || text.contains("slitky") || text.contains("cihla")) {
            return Form.BAR;
        }

        throw new IllegalArgumentException("Invalid argument for Form enum");
    }

    /**
     * Extracts weight from various patterns
     * @param text including number with unit. LowerCase only
     * @return Grams
     */
    public static double weightExtract(String text) throws IllegalArgumentException {
//        text = text.replace("\u00a0", "");         // &nbsp;
        text = text.replace(" ", "");

        Pattern pattern = Pattern.compile("\\d+x\\d+g");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String s = matcher.group();
            s = s.replace("g", "");
            return Integer.parseInt(s.split("x")[0]) * Integer.parseInt(s.split("x")[1]);
        }

        pattern = Pattern.compile("\\d+\\.\\d+g");
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            String s = matcher.group();
            s = s.replace("g", "");
            return Double.parseDouble(s);
        }

        pattern = Pattern.compile("\\d+,\\d+g");
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            String s = matcher.group();
            s = s.replace("g", "");
            s = s.replace(",", ".");
            return Double.parseDouble(s);
        }

        pattern = Pattern.compile("\\d+g");
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            String s = matcher.group();
            s = s.replace("g", "");
            return Double.parseDouble(s);
        }

        pattern = Pattern.compile("\\d+\\/\\d+oz");
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            String s = matcher.group();
            s = s.replace("oz", "");
            return Double.parseDouble(s.split("/")[0]) / Double.parseDouble(s.split("/")[1]) * TROY_OUNCE;
        }

        pattern = Pattern.compile("\\d+oz");
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            String s = matcher.group();
            s = s.replace("oz", "");
            return Double.parseDouble(s) * TROY_OUNCE;
        }

        pattern = Pattern.compile("\\d+kg");
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            String s = matcher.group();
            s = s.replace("kg", "");
            return Double.parseDouble(s) * 1000;
        }

        if (text.contains("tolar") || text.contains("taler")) {
            return TOLAR;
        }

        throw new IllegalArgumentException("Invalid argument for weight extraction");
    }

    private static int yearExtract(String name) {
        Pattern pattern = Pattern.compile("20[012]\\d");
        Matcher matcher = pattern.matcher(name);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }
        pattern = Pattern.compile("19[98765]\\d");
        matcher = pattern.matcher(name);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }
        return Year.now().getValue();
    }

    /**.
     * Uses String.contains
     * Works for Czech, English and German language
     * @param text containing name of Metal. LowerCase only
     * @return metal of product
     */
    public static Metal metalExtract(String text) throws IllegalArgumentException {
        if (text.contains("zlat") || text.contains("gold")) {
            return Metal.GOLD;
        } else if (text.contains("stříbr") || text.contains("silver") || text.contains("silber") ) {
            return Metal.SILVER;
        }
        // There was NO other Metal seen in this form, so far
        else if (text.contains("münzbarren")) {
            return Metal.SILVER;
        }
        else if (text.contains("platin")) {
            return Metal.PLATINUM;
        } else if (text.contains("pallad")) {
            return Metal.PALLADIUM;
        }
        
        throw new IllegalArgumentException("Invalid argument for Metal enum");
    }

    /**
     * @param name LowerCase only
     */
    private static boolean isNameSpecial(String name) {
        return isNameSpecialCZ(name) || isNameSpecialEN(name) || isNameSpecialDE(name);
    }

    /**
     * @param name LowerCase only
     */
    private static boolean isNameSpecialCZ(String name) {
        return name.contains("lunární") || name.contains("výročí") || name.contains("horečka") ||
                name.contains("obři doby ledové") || name.contains("rok") || name.contains("draci") ||
                name.contains("drak") || name.contains("buffalo") || name.contains("legendy") ||
                name.contains("koala");
    }

    /**
     * @param name LowerCase only
     */
    private static boolean isNameSpecialEN(String name) {
        return name.contains("rush") || name.contains("the queen´s beasts");
    }

    /**
     * @param name LowerCase only
     */
    private static boolean isNameSpecialDE(String name) {
        return name.contains("lunar") || name.contains("serie");
    }
    /**
     *
     * @param name CaseInsensitive
     * @return Object includes all extracted params
     * @throws IllegalArgumentException for failure of any param extraction
     */
    public static ProductCreateDTO productAggregateExtract(String name) throws IllegalArgumentException {
        String nameLow = name.toLowerCase(Locale.ROOT);

        // Extraction of parameters for saving new Product/PricePair to DB
        return new ProductCreateDTO(
                    name,
                    producerExtract(nameLow),
                    formExtract(nameLow),
                    metalExtract(nameLow),
                    weightExtract(nameLow),
                    yearExtract(nameLow),
                    isNameSpecial(nameLow)
        );
    }
}

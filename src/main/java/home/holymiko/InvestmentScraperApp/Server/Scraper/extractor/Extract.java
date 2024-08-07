package home.holymiko.InvestmentScraperApp.Server.Scraper.extractor;

import home.holymiko.InvestmentScraperApp.Server.Type.DTO.create.ProductCreateDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.*;

import java.time.Year;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

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
        else if (Stream.of("münze osterreich", "wiener", "philharmoniker").anyMatch(text::contains)) {
            return Producer.MUNZE_OSTERREICH;
        }
        else if (text.contains("pamp")) {
            return Producer.PAMP;
        }
        else if (text.contains("valcambi")) {
            return Producer.VALCAMBI;
        }
        else if (Stream.of("royal canadian mint", "maple leaf").anyMatch(text::contains)) {
            return Producer.ROYAL_CANADIAN_MINT;
        }
        else if (Stream.of("the royal mint", "britannia").anyMatch(text::contains)) {
            return Producer.ROYAL_MINT_UK;
        }
        else if (Stream.of("usa", "american eagle").anyMatch(text::contains)) {
            return Producer.UNITED_STATES_MINT;
        }
        else if (Stream.of("mexico", "mexiko").anyMatch(text::contains)) {
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
        else if (Stream.of("hafner", "heimerle").anyMatch(text::contains)) {
            return Producer.C_HAFNER;
        }
        else if (text.contains("bessergold")) {
            return Producer.BESSERGOLD;
        }
        else if (text.contains("nautical")) {
            return Producer.BH_MAYER_MINT;
        }
        else if (text.contains("leipziger")) {
            return Producer.LEIPZIGER_EDELMETALLVERARBEITUNG_GMBH;
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

        if (Stream.of("rok", "kangaroo", "kookaburra", "koala", "austrálie", "austral", " emu ", "drak a ",
                "next generation", "black flag", "orel klínoocasý", "year of the", "marvel", "wombat", "quokka",
                "chinese myths and legends", "- lunár. série", "- lunární série").anyMatch(text::contains)) {
            return Producer.PERTH_MINT;
        }
        else if (Stream.of("rand", "krugerrand").anyMatch(text::contains)) {
            return Producer.SOUTH_AFRICAN_MINT;
        }
        else if (Stream.of("moose", "golden eagle", "kanada", "roaring grizzly", "growling cougar",
                "howling wolf", "zlatá horečka na klondiku", " elk ", "doba ledová -").anyMatch(text::contains)){
            return Producer.ROYAL_CANADIAN_MINT;
        }
        else if (Stream.of("sovereign elizabeth", "the royal mint", "the queen´s beasts", "the queen's beast",
                "royal arms", "dva draci", "mýty a legendy", "valiant", "tudor beast",  "queens beasts", "krále karla",
                "král karel", "alžbět", "svatý jiří a drak").anyMatch(text::contains)) {
            return Producer.ROYAL_MINT_UK;
        }
        else if (Stream.of("gibraltar lady justice", "kongo gorila", "kek", "ramesse").anyMatch(text::contains)) {
            return Producer.SCOTTSDALE_MINT;
        }
        // From now on precision is lowered
        else if (text.contains("libertad")) {
            return Producer.MEXICO_MINT;
        }
        else if (Stream.of("slon", "leopard somálsko", "somalia elephant").anyMatch(text::contains)) {
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
        else if (Stream.of("obři doby ledové", "giants of the ice age", "archa noemova", "noemova archa").anyMatch(text::contains)) {
            return Producer.LEIPZIGER_EDELMETALLVERARBEITUNG_GMBH;
        }
        else if (Stream.of("münze", "františka josefa").anyMatch(text::contains)) {
            return Producer.MUNZE_OSTERREICH;
        }
        else if (Stream.of("smetana", "štramberk", "tábor", "kroměříž", "moravská třebová",
                "československého rozhlasu", "olomouckým", "městská památková rezervace",
                "5000 kč hrad", "založení velké prahy", "vydání čtyř pražských artikulů",
                "kněžna ludmila").anyMatch(text::contains)) {
            return Producer.CNB;
        }
        else if (Stream.of("žampach", "českého svazu", "český lev", "česká republika 20", "orloj",
                "libuše", "národní divadlo", "unesco -", "mléčná dráha", "psí plemena", "kniha džunglí",
                "pravěký svět", "objevení ameriky", "obrněná technika", "aleš hrdlička").anyMatch(text::contains)) {
            return Producer.CESKA_MINCOVNA;
        }
        else if (Stream.of("pac-man", "piráti z karibiku", "sonic the hedgehog").anyMatch(text::contains)) {
            return Producer.NEW_ZEALAND_MINT;
        }
        else if (Stream.of("chronos", "equilibrium", "terra", "vivat humanitas", "pioneer plaque",
                "magnum opus").anyMatch(text::contains)) {
            return Producer.PRESSBURG_MINT;
        }
        else if (Stream.of("fauna a flóra").anyMatch(text::contains)) {
            return Producer.KREMNICA;
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
        if(Stream.of("combibar", "combi bar", "multidisc", "multigram").anyMatch(text::contains)) {
            return Form.COMBIBAR;
        }
        if(text.contains("roundbar")) {
            return Form.ROUNDBAR;
        }
        if(Stream.of("paket", "odběr 100 ks a více").anyMatch(text::contains)) {
            return Form.PACKET;
        }
        if(Stream.of("mince", "coin", "münze", "tolar", "sovereign").anyMatch(text::contains)) {
            return Form.COIN;
        }
        if(text.contains("kinebar")) {
            return Form.KINEBAR;
        }
        if(text.contains("multicard")) {
            return Form.MULTICARD;
        }
        if(Stream.of("bar", "slitek", "slitky", "cihla").anyMatch(text::contains)) {
            return Form.BAR;
        }

        if(Stream.of("kangaroo", "philharmoniker", "maple leaf").anyMatch(text::contains)) {
            return Form.COIN;
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
        if(text.contains("zlatá mince 5000 kč")) {
            return 15.55;
        } else if (text.contains("zlatá mince 10000 kč")) {
            return TROY_OUNCE;
        } else if (text.contains("stříbrná mince 200 kč")) {
            return 13.00;
        }

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
        if (Stream.of("zlat", "gold").anyMatch(text::contains)) {
            return Metal.GOLD;
        } else if (Stream.of("stříbr", "silver", "silber").anyMatch(text::contains)) {
            return Metal.SILVER;
        }
        // There was NO other Metal seen in this form, so far
        else if (text.contains("münzbarren")) {
            return Metal.SILVER;
        }
        else if (text.contains("platin")) {
            return Metal.PLATINUM;
        } else if (Stream.of("pallad", "paládiov").anyMatch(text::contains)) {
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
        return Stream.of(
                "lunární", "výročí", "horečka", "obři doby ledové", "rok", "draci", "drak", "buffalo", "legendy", "koala"
        ).anyMatch(name::contains);
    }

    /**
     * @param name LowerCase only
     */
    private static boolean isNameSpecialEN(String name) {
        return Stream.of("rush", "the queen´s beasts").anyMatch(name::contains);
    }

    /**
     * @param name LowerCase only
     */
    private static boolean isNameSpecialDE(String name) {
        return Stream.of("lunar", "serie").anyMatch(name::contains);
    }

    /**
     * @param name CaseInsensitive
     * @return Object includes all extracted params
     * @throws IllegalArgumentException for failure of any param extraction
     */
    public static ProductCreateDTO productAggregateExtract(String name) {
        String nameLow = name.toLowerCase(Locale.ROOT);
        boolean hidden = false;
        Producer producer = null;
        Form form = null;
        Metal metal = null;
        Double weight = null;

        try {
            producer = producerExtract(nameLow);
        } catch (IllegalArgumentException e) {
            hidden = true;
        }
        try {
            form = formExtract(nameLow);
        } catch (IllegalArgumentException e) {
            hidden = true;
        }
        try {
            metal = metalExtract(nameLow);
        } catch (IllegalArgumentException e) {
            hidden = true;
        }
        try {
            weight = weightExtract(nameLow);
        } catch (IllegalArgumentException e) {
            hidden = true;
        }

        // Extraction of parameters for saving new Product/PricePair to DB
        return new ProductCreateDTO(
                    name,
                    producer,
                    form,
                    metal,
                    weight,
                    yearExtract(nameLow),
                    isNameSpecial(nameLow),
                    hidden
        );
    }
}

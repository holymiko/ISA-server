package home.holymiko.InvestmentScraperApp.Server.Scraper.extractor;

import home.holymiko.InvestmentScraperApp.Server.Type.Enum.GrahamGrade;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ConvertTest {

    @Test
    void currencyClean0() {
        Assertions.assertEquals(Convert.currencyClean(""), "");
    }

    @Test
    void currencyClean1() {
        Assertions.assertEquals(Convert.currencyClean("   "), "");
    }

    @Test
    void currencyClean2() {
        Assertions.assertEquals(Convert.currencyClean("456  465   Kč"), "456465");
    }

    @Test
    void currencyClean3() {
        Assertions.assertEquals(Convert.currencyClean("45.85"), "45.85");
    }

    @Test
    void currencyClean4() {
        Assertions.assertEquals(Convert.currencyClean("7  8, 5 $  "), "78.5");
    }

    @Test
    void currencyClean5() {
        Assertions.assertEquals(Convert.currencyClean("kč56$7€"), "567");
    }

    @Test
    void currencyClean6() {
        Assertions.assertEquals(Convert.currencyClean("$"), "");
    }

    @Test
    void currencyClean7() {
        Assertions.assertEquals(Convert.currencyClean("€"), "");
    }

    @Test
    void currencyClean8() {
        Assertions.assertEquals(Convert.currencyClean("£"), "");
    }

    @Test
    void currencyClean9() {
        Assertions.assertEquals(Convert.currencyClean("¥"), "");
    }

    @Test
    void currencyClean10() {
        Assertions.assertThrows(NullPointerException.class, () -> Convert.currencyClean(null));
    }

    @Test
    void currencyToDouble0() {
        Assertions.assertEquals(45.85, Convert.currencyToDouble("45,85 ¥"));
    }

    @Test
    void currencyToDouble1() {
        // currencyToDouble is not a method for removing "bez dph etc."
        Assertions.assertThrows(NumberFormatException.class, () -> Convert.currencyToDouble("bez dph 465,45 kč"));
    }

    @Test
    void currencyToDouble2() {
        Assertions.assertThrows(NumberFormatException.class, () -> Convert.currencyToDouble(""));
    }

    @Test
    void currencyToDouble3() {
        Assertions.assertThrows(NumberFormatException.class, () -> Convert.currencyToDouble("dfgfd"));
    }

    @Test
    void currencyToDouble4() {
        Assertions.assertThrows(NullPointerException.class, () -> Convert.currencyToDouble(null));
    }

    @Test
    void currency0() {
        Assertions.assertThrows(NullPointerException.class, () -> Convert.currency(null, 24.0, "$"));
    }

    @Test
    void currency1() {
        Assertions.assertThrows(NullPointerException.class, () -> Convert.currency("28$", null, "$"));
    }

    @Test
    void currency2() {
        Assertions.assertThrows(NullPointerException.class, () -> Convert.currency("28$", 24.0, null));
    }

    @Test
    void currency3() {
        Assertions.assertEquals(28, Convert.currency("28$", 24.0, "Kč"));
    }

    @Test
    void currency4() {
        Assertions.assertEquals(28, Convert.currency("28$", 24.0, "x"));
    }

    @Test
    void currency5() {
        Assertions.assertEquals(672, Convert.currency("28$", 24.0, "$"));
    }

    @Test
    void currency6() {
        Assertions.assertEquals(56, Convert.currency("28$", 2.0, "$"));
    }

    @Test
    void currency7() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Convert.currency("28$", 24.0, ""));
    }

    @Test
    void currency8() {
        Assertions.assertThrows(NumberFormatException.class, () -> Convert.currency("", 24.0, "$"));
    }

    @Test
    void serenityToNumber0() {
        Assertions.assertThrows(NullPointerException.class, () -> Convert.serenityToNumber(null));
    }

    @Test
    void serenityToNumber1() {
        Assertions.assertThrows(NumberFormatException.class, () -> Convert.serenityToNumber("dfgfd"));
    }

    @Test
    void serenityToNumber2() {
        Assertions.assertEquals(2100000.5, Convert.serenityToNumber("2,100, 000.5 $"));
    }

    @Test
    void grahamGrade0() {
        Assertions.assertThrows(NullPointerException.class, () -> Convert.grahamGrade(null));
    }

    @Test
    void grahamGrade1() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Convert.grahamGrade("x"));
    }

    @Test
    void grahamGrade2() {
        Assertions.assertEquals(GrahamGrade.ENTERPRISING, Convert.grahamGrade("enTERprising"));
    }


    @Test
    void metal0() {
        Assertions.assertThrows(NullPointerException.class, () -> Convert.metal(null));
    }

    @Test
    void metal1() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Convert.metal("x"));
    }

    @Test
    void metal2() {
        Assertions.assertEquals(Metal.GOLD, Convert.metal("GoLD"));
    }
}
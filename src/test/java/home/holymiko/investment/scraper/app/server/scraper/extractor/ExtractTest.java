package home.holymiko.investment.scraper.app.server.scraper.extractor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExtractTest {

    @Test
    void weightExtractTest() {
        Assertions.assertEquals(100.0, Extract.productAggregateExtract("Goldbarren 100 x 1 g CombiBar VALCAMBI").getGrams());
    }

    @Test
    void weightExtractTest1() {
        Assertions.assertEquals(50.0, Extract.productAggregateExtract("Goldbarren 50 x 1 g CombiBar VALCAMBI").getGrams());
    }

    @Test
    void weightExtractTest2() {
        Assertions.assertEquals(20.0, Extract.productAggregateExtract("\nGoldbarren 20 x 1 g CombiBar VALCAMBI\n").getGrams());
    }
}
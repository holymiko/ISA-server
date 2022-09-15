# ToDo

## A priority
- [ ] <b>Write bachelors documentation</b>
- [ ] Write <b>JUnit tests</b> for all services (use Mockito)

## B priority
- [ ] <b>Scrap pricePairs from product list page and buyout page</b>
  for Bessergold & Silverum. Visiting all product detail pages won't be necessary.
  Instead, product name and price will be taken from product list page. Buyout
  price will be taken from buyout page. https://www.bessergold.cz/vykup.html
- [ ] Implement <b>multithreading</b> for scraping
- [ ] Implement <b>logging</b> and remove all System.out.print
- [ ] <b>Finalize SerenityScraper:</b> make tickers initializing method, endpoint and UI
- [ ] Add scraper for https://www.investicni-zlato-praha.cz/
- [ ] Add scraper for https://www.aurumbohemica.cz/

## C priority
- [ ] Implement <b>Swagger</b>
- [ ] Scrap <b>spot prices</b> of Gold and Silver to compute <b>Gold/Silver ratio</b>
- [ ] Finalize InvestmentStock in relation with Portfolio. Repair related mappers
- [ ] Make revision and finalize impl. of ScrapHistory. This has to be finalized
    before putting scraper endpoint on-line. (Alternative solution is to deprecate
    endpoint and make scraping automatic based on time.)
- [ ] Write Postman tests for Controllers
- [ ] Refactor (rename) Redemption to BuyOut
- [ ] Scrap spot prices of products to compute dealer's margins
- [ ] Implement Extract and Convert for special type of products, so they can be merged.  

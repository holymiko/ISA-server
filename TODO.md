# ToDo

## A priority
- [ ] <b>Latest price updating. Deleting doesn't work</b>
- [ ] <b>Write bachelors documentation</b>
- [ ] Add <b>Silverum.cz buyout</b> (redemption) scraping
- [ ] Add Silverum silver products
- [ ] <b>Scrap pricePairs from product list page and buyout page</b>
  for Bessergold & Silverum. Visiting all product detail pages won't be necessary. 
  Instead, product name and price will be taken from product list page. Buyout 
  price will be taken from buyout page. https://www.bessergold.cz/vykup.html
- [ ] Implement <b>multithreading</b> for scraping
- [ ] Implement <b>logging</b> and remove all System.out.print

## B priority
- [ ] Write documentation of methods
- [ ] Write Postman tests for Scraper package
- [ ] Create StockController
- [ ] Finalize InvestmentStock in relation with Portfolio
- [ ] Scrap <b>spot prices</b> of Gold and Silver to compute <b>Gold/Silver ratio</b>
- [ ] Make revision and finalize impl. of ScrapHistory. This has to be finalized 
  before putting scraper endpoint on-line. (Alternative solution is to deprecate 
  endpoint and make scraping automatic based on time.)

## C priority
- [ ] Convert methods to <b> Final and Static </b> if possible
- [ ] Refactor (rename) Redemption to BuyOut
- [ ] Scrap spot prices of products to compute dealer's margins


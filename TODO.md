# ToDo

## A priority
- [ ] <b>Write bachelors documentation</b>
- [ ] Create DTO with Best Price & Best Redemption to avoid frequent sorting on FE
- [ ] Add <b>Silverum.cz buyout</b> (redemption) scraping
- [ ] Add Silverum silver products
- [ ] <b>Scrap pricePairs from product list page and buyout page</b>
  for Bessergold & Silverum. Visiting all product detail pages won't be necessary. 
  Instead, product name and price will be taken from product list page. Buyout 
  price will be taken from buyout page. https://www.bessergold.cz/vykup.html
- [ ] Implement <b>multithreading</b> for scraping
- [ ] Implement <b>logging</b> and remove all System.out.print

## B priority
- [ ] Write Postman tests for Scraper package
- [ ] Implement <b>Swagger</b>
- [ ] Finalize InvestmentStock in relation with Portfolio. Repair related mappers
- [ ] Scrap <b>spot prices</b> of Gold and Silver to compute <b>Gold/Silver ratio</b>
- [ ] Make revision and finalize impl. of ScrapHistory. This has to be finalized 
  before putting scraper endpoint on-line. (Alternative solution is to deprecate 
  endpoint and make scraping automatic based on time.)
- [ ] Add scraper for https://www.investicni-zlato-praha.cz/
- [ ] Add scraper for https://www.aurumbohemica.cz/

## C priority
- [ ] Convert methods to <b> Final and Static </b> if possible
- [ ] Refactor (rename) Redemption to BuyOut
- [ ] Scrap spot prices of products to compute dealer's margins
- [ ] Implement Extract and Convert for special type of products, so they can be merged.  

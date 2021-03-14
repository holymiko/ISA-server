# Scrap App server

##Úvod
Smyslem algoritmu je získat, zpracovat, uložit a zprostředkovat data o investičních produktech.





##Získání dat
Získání dat zajišťují třídy `scrap`
Program se pomocí `htmlunit` klienta spojí s webovou stránkou a na základě `XPath` získá z dokumentu relevantní data. 
Ty jsou následné použity k vytvoření entit `stock` či `product`.

Informace pro `stock` jsou získávány spojením www.serenitystocks.com/stock/ +`ticker`
Databáze uchovává seznam tickerů (zkratek užívaných na burze) a jejich stav. 
Jejich zdrojem jsou soubory v adresáři `txt/`

Drahé kovy jsou reprezentovány entitou `product` s relací na `link` a `price` Na základě linkových adres je zjištěny ceny, váha, výrobce atd.



###Zdroje:

https://www.serenitystocks.com/

https://www.bessergold.cz/

https://zlataky.cz/

##Komunikační rozhraní

Data je možné získat na http://localhost:8080/api/v1/ +`entita` Jsou poskytovány ve standartním formátu entity či v optimalizovaném formátu `DTO` (Data Transfer Object)
<br />
<br />
<br />
###Relevance:

https://www.serenitystocks.com/article/how-build-complete-benjamin-graham-portfolio

https://www.silverum.cz/pruvodce-investora.html

https://en.wikipedia.org/wiki/Benjamin_Graham

https://en.wikipedia.org/wiki/The_Intelligent_Investor


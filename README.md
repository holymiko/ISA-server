# Scrap App server

##Úvod
Smyslem algoritmu je zprostředkovat informace o investičních produktech. 

Program vyhodnocuje nabídku drahých kovů na českém trhu a umožnuje tak sestavení defenzivní části portfolia za tu nejlepší cenu. 

Zároveň získává informace ze stránky, která vyhodnocuje akcie na základě strategie popsané v knize <b> Inteligentní Investor</b> od Benjamina Grahama





##Získání dat
Získání dat zajišťují třídy `scrap`
Program se pomocí `htmlunit` klienta spojí s webovou stránkou a na základě `XPath` získá z dokumentu relevantní data. 
Ty jsou následné použity k vytvoření entit `stock` či `product` a uloženy do databáze.

Informace pro `stock` jsou získány z www.serenitystocks.com/stock/ +`ticker`
Databáze uchovává seznam tickerů (zkratek užívaných na burze) a jejich stav. 
Jejich zdrojem jsou soubory v adresáři `txt/`

Drahé kovy jsou reprezentovány entitou `product` s relací na `link` a `price` 
Na základě link adresy je zjištěna cena, váha, výrobce atd.



###Zdroje dat:

https://www.serenitystocks.com/

https://www.bessergold.cz/

https://zlataky.cz/

##Aplikační rozhraní

Data je možné získat na http://localhost:8080/api/v1/ +`entita` Jsou poskytovány ve formátu JSON. Je možné získat celou entitu či optimalizovaný formát `DTO` (Data Transfer Object)
<br />
<br />
<br />

#V1
Commit dates

Základní verze umožňující těžbu dat z webů Bessergold (bessergold.cz) a Serenity (serenitystocks.com).

Obsahuje DTOs, které zmenšují objem dat posílaných na klienta.

Část databáze (tagy) je možné im/exportovat z/do textových souborů. Tato vlastnost však prozatím není zpřístupněna na kontroloru.

#V2
Commit dates

Metodám je postupně přidávána dokumentace.

Datové zdroje jsou rozšířeny o Zlaťáky (Zlataky.cz)

Ve třídě ScrapMetal je provedeno mnoho optimalizací a recyklací kódu. Tato třída nyní sjednocuje všechny metody pro scrapování drahých kovů.

Specifické informace pro jednotlivé dealery jsou uvedeny v potomcích třídy ScrapMetal. (ScrapBessergold,  ScrapZlataky)

Metody pro extrakci dat z třídy ScrapMetal jsou vyčleněny do nové třídy Extractor

Opravy ve třídě ScrapSerenity umožňují opakované scrapování při kterém je automaticky upraven i stav Tagu.

#ToDo
- [ ] Db optimalizace a uprava klienta.
- [ ] Spojeni stejných produktů
- [ ] Auto export do txt pro scrapSerenity
- [ ] Stock to Investments
- [ ] Gold to silver ratio

<br/>
<br/>
<br/>

###Relevantní články:

https://www.serenitystocks.com/article/how-build-complete-benjamin-graham-portfolio

https://en.wikipedia.org/wiki/Benjamin_Graham

https://en.wikipedia.org/wiki/The_Intelligent_Investor

https://www.silverum.cz/pruvodce-investora.html

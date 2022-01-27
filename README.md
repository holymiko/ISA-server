# Scrap App server

## Úvod
Smyslem algoritmu je zprostředkovat informace o investičních produktech. 

Program vyhodnocuje nabídku drahých kovů na českém trhu. 

Zároveň získává informace ze stránky, která vyhodnocuje akcie na základě strategie popsané v knize <b> Inteligentní Investor</b> od Benjamina Grahama


## Získání dat
Získání dat zajišťují třídy `scrap`
Program se pomocí `htmlunit` klienta spojí s webovou stránkou a na základě `XPath` získá z dokumentu relevantní data. 
Ty jsou následné použity k vytvoření entit `stock` či `product` a uloženy do databáze.

Informace pro `stock` jsou získány z www.serenitystocks.com/stock/ +`ticker`
Databáze uchovává seznam tickerů (zkratek užívaných na burze) a jejich stav. 
Jejich zdrojem jsou soubory v adresáři `txt/`

Drahé kovy jsou reprezentovány entitou `product` s relací na `link` a `price` 
Na základě link adresy je zjištěna cena, váha, výrobce atd.

### Zdroje dat:
https://www.serenitystocks.com/  
https://www.bessergold.cz/  
https://zlataky.cz/



## Aplikační rozhraní

Data je možné získat na http://localhost:8080/api/v2/ +`entita` Jsou poskytovány ve formátu JSON.
Entity jsou k dispozici v optimalizovaném formátu `DTO` (Data Transfer Object).

<br />

## V1
_Commits: 5. February - 15. March 2021_

Základní verze umožňující těžbu dat z webů Bessergold (www.bessergold.cz) a Serenity (www.serenitystocks.com).

Obsahuje DTOs, které zmenšují objem dat posílaných na klienta.

**Ticker** je možné im/exportovat z/do textových souborů. Tato vlastnost však prozatím není zpřístupněna na kontroloru.

<br />

## V2
_Commits: 19. May 2021 - now_

Metodám je postupně přidávána dokumentace.

Datové zdroje jsou rozšířeny o Zlaťáky (www.zlataky.cz)

Ve třídě **ScrapMetal** je provedeno mnoho optimalizací a recyklací kódu. 
Tato třída nyní sjednocuje všechny metody pro scrapování drahých kovů.  
Specifické informace pro jednotlivé dealery jsou uvedeny v potomcích třídy ScrapMetal (**ScrapBessergold**,  **ScrapZlataky**).  
Metody pro extrakci dat z HTML ve třídě ScrapMetal jsou vyčleněny do nové třídy **Extract**.

Opravy ve třídě **ScrapSerenity** umožňují opakované scrapování, při kterém je automaticky upraven **TickerState**.

Etické spoždění pro scrapování je nyní dynamické. 
Skutečná doba uspání algoritmu, mezi každým odeslaným dotazem, závisý na čase potřebném ke scrapování produktu (viz. metoda **dynamicSleepAndStatusPrint**).

Na převod datových typů se používá **@Mapper**, který automaticky generuje kód. Tento kód je často modifikovám pomocí **@Mapping(expression)**

Byl položen základ vlastních annotací a výjimek.

### Změny v databázi
Stejné produkty od různých dealerů jsou automaticky spojovány do jednoho.
**Produkt** tak nyní může obsahovat více **links** a **latestPrices**.

Z entity **Price** jsou odstraněny atributy **split** (spread) a **pricePerGram**. 
Je možné je vypočítat a tak se nyní vyskytují pouze v **PriceDTO**.  
Ze stejného důvodu byly odstraněny atributy **Portfolio.beginPrice**, **Portfolio.value**, **Portfolio.yield** a **InvestmentMetal.yield**.

Do entity **Price** je přidán atribut **dealer**, aby byly ceny přidružené k produktu rozlišitelné.

Entita **Produkt** obsahuje nový atribut **year** pro přesnější určení.


<br />


### Note: Sna69m se pou69vat DTO na příč komunikací aby se šetřili datový přenos a posílat minimum reguestu na databázi


## ToDo
- [ ] Final and Static
- [ ] Documentation
- [ ] Postman tests
- [ ] Class documentation
- [ ] Add Silverum.cz
- [ ] Lambda.orElseThrow discussion
- [ ] Scrap prices from product list for Bessergold & Silverum.
- [ ] Scrap redemption prices from https://www.bessergold.cz/vykup.html
- [ ] Stock investments
- [ ] Gold to silver ratio

<br/>
<br/>
<br/>

### Relevantní články:

https://www.serenitystocks.com/article/how-build-complete-benjamin-graham-portfolio  
https://en.wikipedia.org/wiki/Benjamin_Graham  
https://en.wikipedia.org/wiki/The_Intelligent_Investor  
https://www.silverum.cz/pruvodce-investora.html  

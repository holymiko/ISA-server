# Investment Scrap App server

## How to run?
>### Database
>* cd src/main/docker<br>
>* docker build .<br>
>* docker compose up
>### Back-end application
>Make sure your device has access to internet.<br>
>Run <b>main method in InvestmentScraperApp.java</b><br>
>Initial actions, like data scraping,
> can be handled by <b>EventListeners in Run.java</b>
>### Front-end application
>Enter client directory
>* yarn install<br>
>* yarn start<br>


## Tech stack 
> ### BE
> Java<br>
> Springboot<br>
> Hibernate<br>
> Maven<br>
> Docker<br>
> PostgreSQL<br>

> ### FE
> ReactJS<br>
> JavaScript/TypeScript<br>
> Material-UI<br>

## Úvod
> Smyslem algoritmu je zprostředkovat informace o investičních produktech. <br>
Program vyhodnocuje nabídku drahých kovů na českém trhu. <br>
Zároveň získává informace ze stránky, která vyhodnocuje akcie na základě 
strategie popsané v knize <b> Inteligentní Investor</b> od Benjamina Grahama


## Získání dat
> Získání dat zajišťují třídy `scrap`
Program se pomocí `htmlunit` klienta spojí s webovou stránkou a na základě `XPath` získá z dokumentu relevantní data. 
Ty jsou následné použity k vytvoření entit `stock` či `product` a uloženy do databáze.

> Informace pro `stock` jsou získány z www.serenitystocks.com/stock/ +`ticker`
Databáze uchovává seznam tickerů (zkratek užívaných na burze) a jejich stav. 
Jejich zdrojem jsou soubory v adresáři `txt/`

>Drahé kovy jsou reprezentovány entitou `product` s relací na `link` a `pricePair` 
Na základě link adresy je zjištěna cena, váha, výrobce atd.

### Zdroje dat
https://www.serenitystocks.com<br>
https://www.bessergold.cz<br>
https://zlataky.cz<br>
https://silverum.cz<br>



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
**Produkt** tak nyní může obsahovat více **links** a **latestPricePairs**.

Z entity **Price** jsou odstraněny atributy **split** (spread) a **pricePerGram**. 
Je možné je vypočítat a tak se nyní vyskytují pouze v **PriceDTO**.  
Ze stejného důvodu byly odstraněny atributy **Portfolio.beginPrice**, **Portfolio.value**, **Portfolio.yield** a **InvestmentMetal.yield**.

Do entity **Price** je přidán atribut **dealer**, aby byly ceny přidružené k produktu rozlišitelné.

Entita **Produkt** obsahuje nový atribut **year** pro přesnější určení.


<br />

### DTO
V aplikaci jsou používány Data Transfer Objects, aby se šetřil datový přenos mezi aplikacemi a minimalizovalo množství dat, se kterými se manipuluje.


<br/>
<br/>
<br/>

### Relevantní články
https://www.serenitystocks.com/article/how-build-complete-benjamin-graham-portfolio  
https://en.wikipedia.org/wiki/Benjamin_Graham  
https://en.wikipedia.org/wiki/The_Intelligent_Investor  
https://www.silverum.cz/pruvodce-investora.html  

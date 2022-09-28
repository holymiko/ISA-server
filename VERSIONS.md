# Investment Scrap App server

## V1
_Commits: 5. February - 15. March 2021_

Základní verze umožňující těžbu dat z webů Bessergold (www.bessergold.cz) a Serenity (www.serenitystocks.com).

Obsahuje DTOs, které zmenšují objem dat posílaných na klienta.

**Ticker** je možné im/exportovat z/do textových souborů. Tato vlastnost však prozatím není zpřístupněna na kontroloru.


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


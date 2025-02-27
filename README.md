# Investment Scrap App

## How to run on local machine?
#### Database
>`cd src/main/docker`<br>
>`docker build .`<br>
>`docker compose up`
#### Backend application
>Make sure your device has access to internet.<br>
>Run <b>main method in InvestmentScraperApp.java</b><br>
>Initial actions, like data scraping,
> can be handled by <b>EventListeners in Run.java</b>
#### Frontend application
>Enter client directory <br>
>`yarn install`<br>
>`yarn start`<br>
<br>

## Server deployment

#### Commands
><b>Login</b> to server<br>
>`sudo ssh -v -p 22 root@67.223.117.163`<p>
> <b>Backup DB</b> on server<br>
> `nano app/postgres_data_dump/README.txt`<p>
> <b>Copy DB</b> dump from server to local machine<br>
>`scp root@67.223.117.163:/root/app/postgres_data_dump/14-04-24-after-clean-up.sql .`<p>
> <b>Restore DB</b> from local machine<br>
>`psql -U postgres -p 5432 -h 67.223.117.163 -d goldSilver < 25_04_24_product_synchronization.dump`<p>
> [useful SQL commands](request.sql) <br>

#### Tips
> Lack of memory in Docker<br>
> [docker system prune](https://docs.docker.com/reference/cli/docker/system/prune/)
> <b>BACKUP Postgres</b> BEFORE this command !!!!<p>
> After FE deployment, open app in new tab. Otherwise, changes won't take place.
<br>


## Technology stack 
#### Backend stack
> Java<br>Springboot<br>Hibernate<br>Maven<br>Docker<br>PostgreSQL<br>
>
#### Frontend stack
> ReactJS<br>JavaScript/TypeScript<br>Material-UI<br>
<br>

## Introduction
> Smyslem algoritmu je zprostředkovat informace o investičních produktech. <br>
Program vyhodnocuje nabídku drahých kovů na českém trhu. <br>
Zároveň získává informace ze stránky, která vyhodnocuje akcie na základě 
strategie popsané v knize <b> Inteligentní Investor</b> od Benjamina Grahama

### Relevant articles
> https://www.silverum.cz/pruvodce-investora.html <br>
https://www.grahamvalue.com/article/how-build-complete-benjamin-graham-portfolio <br>
https://en.wikipedia.org/wiki/Benjamin_Graham  
https://en.wikipedia.org/wiki/The_Intelligent_Investor  
<br>

## Data gathering
> Získání dat zajišťují třídy `scrap`
Program se pomocí `htmlunit` klienta spojí s webovou stránkou a na základě `XPath` získá z dokumentu relevantní data. 
Ty jsou následné použity k vytvoření entit `stockGraham` či `product` a uloženy do databáze.

> Informace pro `stockGraham` jsou získány z www.serenitystocks.com/stockGraham/ +`ticker`
Databáze uchovává seznam tickerů (zkratek užívaných na burze) a jejich stav. 
Jejich zdrojem jsou soubory v adresáři `txt/`

>Drahé kovy jsou reprezentovány entitou `product` s relací na `link` a `pricePair` 
Na základě link adresy je zjištěna cena, váha, výrobce atd.

<img src="doc/img/isa_draw/scraper_api_5_legend.png" title="app diagram" alt="app diagram"/>

### Data sources
> https://www.serenitystocks.com<br>
https://www.bessergold.cz<br>
https://www.bessergold.de<br>
https://zlataky.cz<br>
https://silverum.cz<br>

<br>

## Application interface

> Data je možné získat na http://localhost:8080/api/v2/ +`entita` Jsou poskytovány ve formátu JSON.
Entity jsou k dispozici v optimalizovaném formátu `DTO` (Data Transfer Object).

### Swagger
> http://localhost:8080/swagger-ui/

### DTO
> V aplikaci jsou používány Data Transfer Objects, aby se šetřil datový přenos mezi aplikacemi a minimalizovalo množství dat, se kterými se manipuluje.

<br>

## User interface

### Product table
![Gold products filter](https://github.com/user-attachments/assets/deb75c99-2ea2-46d5-96f5-ee83f52ec55e)
![Screenshot from 2024-08-15 16-46-18](https://github.com/user-attachments/assets/60e2c696-bab8-4428-aa9f-dfac9b1bfd96)
![Screenshot from 2024-08-15 16-46-29](https://github.com/user-attachments/assets/c1afb7a8-98dd-4596-b2e2-985eff75561b)
### Product detail
![Screenshot from 2024-08-28 10-43-48](https://github.com/user-attachments/assets/086d4d52-238c-4b6c-8f6a-5d29632172aa)
![Screenshot from 2024-08-28 10-43-55](https://github.com/user-attachments/assets/c531d089-12e5-4a38-baf5-ee97bf47fd00)
![Screenshot from 2024-08-28 10-44-00](https://github.com/user-attachments/assets/168f72ff-5ed2-473f-8cb5-441ffa9fa0a6)
![Screenshot from 2024-08-28 10-44-57](https://github.com/user-attachments/assets/f036b7e2-9244-4d3c-9606-7e166d4743f8)
### Portfolios
![Portfolios](https://github.com/user-attachments/assets/8412783d-639d-450a-92cf-041eed546764)
### Analytics
![Analytics filter](https://github.com/user-attachments/assets/70b6276e-6dbe-47e5-a855-b2f9c2f1dfbc)
![Dealer stats](https://github.com/user-attachments/assets/4dd88f77-2459-4e56-93d3-aec67383f902)
![Gold distribution fce](https://github.com/user-attachments/assets/9e2153ff-0a29-4a70-b77f-6e36c4877777)
![Gold distribution fce 2](https://github.com/user-attachments/assets/fe25d200-a26f-40d5-89f7-9a8cd9e0a1a3)
![Availability](https://github.com/user-attachments/assets/20acb5f4-c619-462c-b231-8f1c0c528982)
![Availability 2](https://github.com/user-attachments/assets/456f1722-752d-4e2d-b1f6-f0a030711519)
![Screenshot from 2024-08-28 00-23-59](https://github.com/user-attachments/assets/08474891-78f2-4e85-93df-4400e4a08725)
![Screenshot from 2024-08-28 00-24-16](https://github.com/user-attachments/assets/769c40e8-f143-4ace-bce0-61794b96a073)


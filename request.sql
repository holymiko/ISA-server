-- For fixing production DB and future liquibase

-- Delete wrong Bessergold prices, caused by pop up special offer
DELETE FROM price_pair where id in
     (select distinct pp.id from link l
             join price_pair pp on l.id = pp.link_id
             join price p on pp.sell_price_id = p.id
      where
              l.dealer = 0 and
              p.date_time <= '2023-11-23 23:59:08.172019')


-- Synchronize ID generating after DB copy/restore
SELECT nextval('hibernate_sequence');
SELECT MAX(id) FROM price;
SELECT setval('hibernate_sequence', (SELECT MAX(id) FROM price)+1);
-- If you have problem to import into empty DB, try set sequence to 0
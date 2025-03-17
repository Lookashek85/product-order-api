USE tutorial;
ALTER TABLE tutorial.product ADD CONSTRAINT uniq_product_name UNIQUE (name);
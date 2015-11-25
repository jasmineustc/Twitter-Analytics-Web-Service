#!/bin/bash
for f in q3data/*.csv
do
sudo mysql --local-infile -uroot -p123456 mydb -e "use mydb" -e "
      LOAD DATA LOCAL INFILE '$f'
      INTO TABLE test3 
      FIELDS TERMINATED BY '\t' 
      LINES TERMINATED BY '\n';"
        echo "$f finish" 
done
echo finish
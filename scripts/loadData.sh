#!/bin/bash
#######################################
# load q2 data
#######################################
cd /home/ubuntu/q2data
for f in *
do
sudo mysql --local-infile -uroot -p123456 mydb -e "use mydb" -e "
      LOAD DATA LOCAL INFILE '$f'
      INTO TABLE test2 
      FIELDS TERMINATED BY '\t' 
      LINES TERMINATED BY '\n';"
        echo "$f finish" 
done
echo finish
#######################################
# load q3 data
#######################################
cd /home/ubuntu/q3data
for f in *
do
sudo mysql --local-infile -uroot -p123456 mydb -e "use mydb" -e "
      LOAD DATA LOCAL INFILE '$f'
      INTO TABLE test3 
      FIELDS TERMINATED BY '\t' 
      LINES TERMINATED BY '\n';"
        echo "$f finish" 
done
echo finish
#######################################
# load q3 data
#######################################
cd /home/ubuntu/q4data
for f in *
do
sudo mysql --local-infile -uroot -p123456 mydb -e "use mydb" -e "
      LOAD DATA LOCAL INFILE '$f'
      INTO TABLE test4
      FIELDS TERMINATED BY '\t' 
      LINES TERMINATED BY '\n';"
        echo "$f finish" 
done
echo finish
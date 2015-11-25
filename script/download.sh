counter=2
while [ $counter -le  4 ]
do
     wget https://s3.amazonaws.com/dongdongwu/q3newkeyvalue/part-r-0000$counter
     ((counter++))
 done

echo ALL done

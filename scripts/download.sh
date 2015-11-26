#############################
# download q2 data
#############################
cd /home/ubuntu/q2data
counter=0
while [ $counter -le  9 ]
do
    wget https://s3.amazonaws.com/dongdongwu/q2/part-r-0000$counter.csv
    ((counter++))
done
counter=10
while [ $counter -le  22 ]
do
    wget https://s3.amazonaws.com/dongdongwu/q2/part-r-000$counter.csv
    ((counter++))
done

echo ALL done
#############################
# download q3 data
#############################
cd /home/ubuntu/q3data
counter=0
while [ $counter -le  4 ]
do
    wget https://s3.amazonaws.com/dongdongwu/q3newkeyvalue/part-r-0000$counter
    ((counter++))
done

echo ALL done
#############################
# download q4 data
#############################
cd /home/ubuntu/q4data

wget https://s3.amazonaws.com/15619yuchengzhangdatas/q4sortnew.csv

echo ALL done

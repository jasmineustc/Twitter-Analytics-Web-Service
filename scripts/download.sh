#############################
# download q2 data
#############################
cd /home/ubuntu/q2data
counter=0
while [ $counter -le  9 ]
do
    wget https://s3.amazonaws.com/dongdongwu/q2/part-0000$counter.csv
    ((counter++))
done
counter=10
while [ $counter -le  22 ]
do
    wget https://s3.amazonaws.com/dongdongwu/q2/part-000$counter.csv
    ((counter++))
done

echo Q2 ALL done
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

echo Q3 ALL done
#############################
# download q4 data
#############################
cd /home/ubuntu/q4data

wget https://s3.amazonaws.com/15619yuchengzhangdatas/q4sortnew.csv

echo Q4 ALL done
#############################
# download q5 data
#############################
cd /home/ubuntu/q5data
wget https://s3.amazonaws.com/mizhangteamproject/q5merge.csv
echo Q5 ALL done

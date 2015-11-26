#############################
# download q2 data
#############################
cd /home/ubuntu/q2data
counter=2
while [ $counter -le  4 ]
do
    wget https://s3.amazonaws.com/dongdongwu/q3newkeyvalue/part-r-0000$counter
    ((counter++))
done

echo ALL done
#############################
# download q3 data
#############################
cd /home/ubuntu/q3data
counter=2
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
counter=2
while [ $counter -le  4 ]
do
    wget https://s3.amazonaws.com/dongdongwu/q3newkeyvalue/part-r-0000$counter
    ((counter++))
done

echo ALL done

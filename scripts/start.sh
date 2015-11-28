export CLASSPATH=/home/ubuntu/*:$CLASSPATH
sudo iptables -t nat -A PREROUTING -p tcp --dport 80 -j REDIRECT --to-ports 8080
#export JAVA_OPTS='-Xms2048m -Xmx2048m'
sudo apt-get install openjdk-8-jdk
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update
sudo apt-get install oracle-java8-installer


sudo apt-get update
sudo apt-get upgrade
sudo apt-get install mysql-server

wget https://s3.amazonaws.com/teamprojectmysqlremote/mysql-connector-java-5.1.37-bin.jar
wget https://s3.amazonaws.com/teamprojectmysqlremote/vert.x-3.1.0-full.tar.gz
tar -vxzf vert.x-3.1.0-full.tar.gz

wget "https://s3.amazonaws.com/aws-cli/awscli-bundle.zip" -o "awscli-bundle.zip"
sudo apt-get install unzip
unzip awscli-bundle.zip
sudo apt-get install awscli

mkdir q2data
mkdir q3data
mkdir q4data
mkdir q5data
mkdir q6data
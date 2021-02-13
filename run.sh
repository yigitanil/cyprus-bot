ps axf | grep cyprus-bot | grep -v grep | awk '{print "kill -9 " $1}' | sh
cd cyprus-bot/
git pull
PATH="/home/ec2-user/jdk-14.0.2/bin:$PATH"
export PATH
../maven/bin/mvn clean install
cd ..
nohup java -jar cyprus-bot/target/cyprus-bot-0.0.1-SNAPSHOT.jar -> out.log &
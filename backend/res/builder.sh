#!/bin/bash

set -x
exec > >(tee /home/ubuntu/logs/backend-api-builder.log |logger -t ubuntu) 2>&1

date
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

#Max memory = [-Xmx] + [-XX:MaxPermSize] + number_of_threads * [-Xss]
pkill -f activator
#$DIR/../activator stage -J-Xms128M -J-Xmx512m


pkill -f cta
#sudo kill `cat $DIR/../target/universal/stage/RUNNING_PID`
sudo rm $DIR/../target/universal/stage/RUNNING_PID

mkdir -p /home/ubuntu/myapp/api/target/universal/stage
sudo rm -R /home/ubuntu/myapp/api/target/universal/cta-1.0-SNAPSHOT/
unzip /home/ubuntu/myapp/api/target/universal/cta-1.0-SNAPSHOT.zip  -d  /home/ubuntu/myapp/api/target/universal/
cp -rf /home/ubuntu/myapp/api/target/universal/cta-1.0-SNAPSHOT/* /home/ubuntu/myapp/api/target/universal/stage/
sudo service monit restart

date
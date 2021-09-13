FROM bitbucketpipelines/scala-sbt:latest

COPY . ./

RUN ./activator dist

RUN cd target/universal && unzip cta-1.0-SNAPSHOT.zip && rm cta-1.0-SNAPSHOT.zip

CMD ./target/universal/cta-1.0-SNAPSHOT/bin/cta -J-Xms300M -J-Xmx800m -J-server -Dplay.crypto.secret=7000cymit227 -Dhttp.port=7000 -Dconfig.resource=application.conf -Dlogger.resource=logback-prod.xml 2>&1
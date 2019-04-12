mvn clean package -pl ui --am
cp ui/target/ui-0.0.1-SNAPSHOT.jar ~/l4jtmp/
~/utils/launch4j/launch4j ~/l4jtmp/l4jcfg.xml

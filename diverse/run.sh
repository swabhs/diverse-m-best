export CLASSPATH=bin/:/root/.m2/repository/com/google/protobuf/protobuf-java/2.4.1/protobuf-java-2.4.1.jar:$CLASSPATH ; cd ~/Projects/diverse-m-best/diverse/; javac -d bin/ basicViterbi/*.java hypergraph/*.java; java  basicViterbi.ViterbiMain ../data/test.hyper
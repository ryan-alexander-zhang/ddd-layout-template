# ddd-layout-template

```shell
mvn archetype:create-from-project

mkdir -p /tmp/ddd-demo-gen && cd /tmp/ddd-demo-gen

mvn archetype:generate \
  -DarchetypeGroupId=com.ryan.ddd \
  -DarchetypeArtifactId=ddd-layout-template-archetype \
  -DarchetypeVersion=0.0.1-SNAPSHOT \
  -DgroupId=ai.saharalabs.hive \
  -DartifactId=hive-server \
  -Dversion=0.0.1-SNAPSHOT \
  -Dpackage=ai.saharalabs.hive \
  -DinteractiveMode=false

```
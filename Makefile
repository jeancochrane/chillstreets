data/raw/chicago-filtered.osm.pbf: data/raw/chicago.osm
	osmium cat $< -o $@ -f osm.pbf,add_metadata=false

data/raw/chicago.osm:
	wget --no-use-server-timestamps -O $@ https://overpass-api.de/api/map?bbox=-87.8558,41.6229,-87.5085,42.0488

build:
	MAVEN_OPTS=-Xss10M mvn clean compile assembly:single -U -e
	touch $@

serve: build
	java -jar target/chillstreets-0.0.1-SNAPSHOT-jar-with-dependencies.jar server config.yml

clean:
	rm -f build
	rm -Rf target/*
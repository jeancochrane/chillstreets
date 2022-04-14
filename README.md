# chillstreets

Mellow Bike Map v2

## Installation

* Docker
* make
* wget
* Osmium Tools (`brew install osmium-tools`)

## Make the data

```commandline
make data/raw/chicago-filtered.osm.pbf
```

## Run the server

```commandline
docker run --rm -p 8989:8989 -v `pwd`/data/raw:/data/raw -v `pwd`/config.yml:/graphhopper/config.yml -e FILE=/data/raw/chicago-filtered.osm.pbf israelhikingmap/graphhopper:5.0 -c config.yml --host 0.0.0.0
```

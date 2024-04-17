#! /bin/sh

echo "getting all data...."
curl http://localhost:21000/person

echo
echo "killing off person #2"
curl -X PATCH -H "Content-Type: application/json-patch+json" http://localhost:21000/person/2 -d '[ { "op": "replace", "path": "/deathDate", "value": "1600-02-15" } ]'

echo
echo "getting all people again"
curl http://localhost:21000/person

echo



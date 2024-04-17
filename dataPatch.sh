#! /bin/sh

urlRoot=http://localhost:21000

echo "getting all data...."
curl $urlRoot/person

echo
echo "add new person...."
curl -X POST -H "Content-Type: application/json" $urlRoot/person -d '{ "name": "Charles", "birthDate": "1950-04-15" }'

echo
echo "killing off person #2"
curl -X PATCH -H "Content-Type: application/json-patch+json" $urlRoot/person/2 -d '[ { "op": "replace", "path": "/deathDate", "value": "1600-02-15" } ]'

echo
echo "getting all people again"
curl $urlRoot/person

echo



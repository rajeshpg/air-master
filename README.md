
data: http://ourairports.com/data/
install [mongodb](https://docs.mongodb.com/manual/installation/)

start mongodb 

> `mongod`

import data into mongodb

> `mongoimport --db airmaster --collection runways --drop --file ./db/runways.csv --type csv --headerline`

> `mongoimport --db airmaster --collection countries --drop --file ./db/countires.csv --type csv --headerline`

> `mongoimport --db airmaster --collection airports --drop --file ./db/airports.csv --type csv --headerline`


start the web application 

> `./bin/activator run`

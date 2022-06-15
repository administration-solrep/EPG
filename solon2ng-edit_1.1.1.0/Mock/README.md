Requetes de test :
================
                                     
  * Docx download  : http://localhost:9001/download?docId=425513353&userId=1

  * ODT download : http://localhost:9001/download?docId=421676&userId=1


  exemples :
  -----

  * Voir https://github.com/namshi/mockserver/tree/master/test/mocks

```
curl -i http://localhost:9001/

curl -i -H "Authorization: 12345" http://localhost:9001/h

curl -i -H "Authorization: 123455" http://localhost:9001/h


curl -i -H "Response-Delay: 5000" -H "Authorization: 123455" http://localhost:9001/h

curl -i "http://localhost:9001/param?docId=12&userId=F35"

curl -i -H "Authorization: 1234" "http://localhost:9001/param?docId=12&userId=F35"


curl -i "http://localhost:9001/users/2"

curl -i "http://localhost:9001/users/2/detail"


curl -i "http://localhost:9001/dyn"

curl -i "http://localhost:9001/dyn?dyn=true"

curl -i "http://localhost:9001/dyn?eval=true"

curl -i -X POST -d 'bla bla bla' http://localhost:9001/dyn

curl -i -X POST -d "bla bla foo bla" "http://localhost:9001/dyn"

curl -X POST -d "bla bla foo bla" "http://localhost:9001/dyn?code=true"

```
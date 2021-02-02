curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"id":"11006","gender":"M","birthDate":"23.06.1953","isoCountry":"gb","testDate":"1.03.2021","testResult":"P","intervention":"quarantine"}' \
  -w "@curl-format.txt" \
  -o /dev/null \
  -s http://localhost:8080/people


curl --header "Content-Type: application/json" \
  -w "@curl-format.txt" \
  -o /dev/null \
  -s http://localhost:8080/people/11006


curl --header "Content-Type: application/json" \
  --request PATCH \
  --data '{"id":"11006","gender":"F","birthDate":"23.06.1953","isoCountry":"gb","testDate":"1.03.2021","testResult":"P","intervention":"quarantine"}' \
  -w "@curl-format.txt" \
  -o /dev/null \
  -s http://localhost:8080/people/11006


curl --header "Content-Type: application/json" \
  -w "@curl-format.txt" \
  -o /dev/null \
  -s http://localhost:8080/analytics/positiveByDates


curl --header "Content-Type: application/json" \
  -w "@curl-format.txt" \
  -o /dev/null \
  -s http://localhost:8080/analytics/positiveByGenderAndState

curl --header "Content-Type: application/json" \
  -w "@curl-format.txt" \
  -o /dev/null \
  -s http://localhost:8080/analytics/quarantine


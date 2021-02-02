#!/usr/bin/env ruby

require 'csv'
require 'net/http'
require 'uri'
require 'json'

KEY_MAPPING = {
  'Id' => 'id',
  'Gender' => 'gender',
  'BirthDate' => 'birthDate',
  'ISOCountry' => 'isoCountry',
  'TestDate' => 'testDate',
  'TestResult' => 'testResult',
  'Intervention' => 'intervention'
}.freeze

URI_X = URI("http://127.0.0.1:8080/people")
HTTP_X = Net::HTTP.new(URI_X.host, URI_X.port)

def post(content)
  request = Net::HTTP::Post.new(URI_X.request_uri, {'Content-Type': 'application/json'})
  request.body = JSON.dump(content)

  response = HTTP_X.request(request)
  [response.code, response.body]
end

i = 0
CSV.parse(File.read($ARGV[0]), headers: true, col_sep: ';').each do |pom|
  person = Hash[pom.headers.map { |key| [KEY_MAPPING.fetch(key), pom[key]] }]

  puts post(person).inspect

  #if i > 10
    # exit
  #end

  i = i + 1
end

#!/usr/bin/env python
# -*- coding: utf-8 -*-

import json

# open sample weather data stored in WeatherData.json
fr = open('WeatherData.json')

# read data
jsonData = fr.read()

# parse
parsed_json = json.loads(jsonData)

# print useful data
print ("current temperature: {0}".format(parsed_json['data']['current_condition'][0]['temp_C']))
print ("current weather status: {0}".format(parsed_json['data']['current_condition'][0]['weatherDesc'][0]['value']))
print ("current weather status[lang_zh]: {0}".format(parsed_json['data']['current_condition'][0]['lang_zh'][0]['value'].encode("utf-8")))



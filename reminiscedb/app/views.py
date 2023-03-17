from django.shortcuts import render
from django.http import JsonResponse, HttpResponse
from django.db import connection
from django.views.decorators.csrf import csrf_exempt
from django.conf import settings
from django.core.files.storage import FileSystemStorage
# from google.oauth2 import id_token
# from google.auth.transport import requests
import json, os, time, hashlib

# TEMPORARY FRAMEWORK W/O EXPIRATION
@csrf_exempt
def adduser(request):
    if request.method != 'POST':
        return HttpResponse(status=404)

    json_data = json.loads(request.body)
    user_id = json_data['id']   # the front end app's OAuth 2.0 Client ID
    username = json_data['username']

    now = time.time()                  # secs since epoch (1/1/70, 00:00:00 UTC)

    cursor = connection.cursor()
    cursor.execute('DELETE FROM users WHERE %s > expiration;', (now, ))

    cursor.execute('INSERT INTO users (id, username, expiration) VALUES '
                   '(%s, %s, %s);', (user_id, username, now))

    return JsonResponse({'id': user_id, 'lifetime': 0})

@csrf_exempt
def posttrip(request):
    if request.method != 'POST':
        return HttpResponse(status=404)

    json_data = json.loads(request.body)
    # trip_id = json_data['trip_id'] auto increment
    user_id = json_data['user_id']
    trip_name = json_data['trip_name']
    trip_start = json_data['trip_start']
    trip_end = json_data['trip_end']
    trip_spotify = json_data['trip_spotify']
    trip_people = json_data['trip_people']
    trip_description = json_data['trip_description']

    cursor = connection.cursor()
    cursor.execute('INSERT INTO chatts (user_id, trip_name, trip_start, trip_end, trip_spotify, trip_people, trip_description) VALUES '
                   '(%s, %s, %s, %s, %s, %s, %s);', (user_id, trip_name, trip_start, trip_end, trip_spotify, trip_people, trip_description))

    return JsonResponse({})

def getalltrips(request):
    if request.method != 'GET':
        return HttpResponse(status=404)
    
    json_data = json.loads(request.body)
    user_id_request = json_data['user_id']

    cursor = connection.cursor()
    cursor.execute('SELECT trip_id, trip_name, trip_start, trip_description FROM trips WHERE user_id = {};'.format(user_id_request))
    data = cursor.fetchall()

    response = {}
    response['trips'] = data
    return JsonResponse(response)

def gettripdata(request):
    if request.method != 'GET':
        return HttpResponse(status=404)

    json_data = json.loads(request.body)
    trip_id_request = json_data['trip_id']

    cursor = connection.cursor()
    cursor.execute('SELECT * FROM trips WHERE trip_id = {};'.format(trip_id_request))
    data = cursor.fetchall()

    response = {}
    response['trip_data'] = data
    return JsonResponse(response)
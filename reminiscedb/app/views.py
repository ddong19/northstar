from django.shortcuts import render
from django.http import JsonResponse, HttpResponse
from django.db import connection
from django.views.decorators.csrf import csrf_exempt
from django.conf import settings
from django.core.files.storage import FileSystemStorage
# from google.oauth2 import id_token
# from google.auth.transport import requests
import urllib.request, json, os, time, hashlib
from urllib import parse
from urllib.parse import parse_qs, urlparse

# TEMPORARY FRAMEWORK W/O EXPIRATION
@csrf_exempt
def adduser(request):
    if request.method != 'POST':
        return HttpResponse(status=404)

    json_data = json.loads(request.body)
    # user_id = json_data['id']   # the front end app's OAuth 2.0 Client ID
    username = json_data['username']

    now = time.time()                  # secs since epoch (1/1/70, 00:00:00 UTC)

    cursor = connection.cursor()
    # cursor.execute('DELETE FROM users WHERE %s > expiration;', (now, ))
    
    insert_stmt = (
    "INSERT INTO users (username, expiration) "
    "VALUES (%s, %s)"
    )
    data = (username, now)
    cursor.execute(insert_stmt, data)
    
    return JsonResponse({'lifetime': 0})

def getuser(request):
    if request.method != 'GET':
        return HttpResponse(status=404)
    
    json_data = json.loads(request.body)
    user_id_request = json_data['user_id']

    cursor = connection.cursor()
    cursor.execute('SELECT username FROM users WHERE id = {};'.format(user_id_request))
    data = cursor.fetchall()

    response = {}
    response['username'] = data
    return JsonResponse(response)

@csrf_exempt
def posttrip(request):
    if request.method != 'POST':
        return HttpResponse(status=404)

    json_data = json.loads(request.body)
    # trip_id = json_data['trip_id'] auto increment
    user_id = json_data['user_id']
    trip_destination = json_data['trip_destination']
    trip_start = json_data['trip_start']
    trip_end = json_data['trip_end']
    trip_spotify = json_data['trip_spotify']
    # trip_people = json_data['trip_people']
    trip_description = json_data['trip_description']

    cursor = connection.cursor()
    
    insert_stmt = (
    "INSERT INTO trips (user_id, trip_destination, trip_start, trip_end, trip_spotify, trip_description) "
    "VALUES (%s, %s, %s, %s, %s, %s)"
    )
    data = (user_id, trip_destination, trip_start, trip_end, trip_spotify, trip_description)
    cursor.execute(insert_stmt, data)

    return JsonResponse({})

def getalltrips(request):
    if request.method != 'GET':
        return HttpResponse(status=404)
    
    json_data = json.loads(request.body)
    user_id_request = json_data['user_id']
    
    

    cursor = connection.cursor()
    cursor.execute('SELECT * FROM trips WHERE user_id = {} ORDER BY trip_id DESC;'.format(user_id_request))
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

@csrf_exempt
def postimage(request):
    if request.method != 'POST':
        return HttpResponse(status=404)

    json_data = json.loads(request.body)
    trip_id = json_data['trip_id']
    image_locations = json_data['image_location']
    image_uri = json_data['image_uri']

    cursor = connection.cursor()
    insert_stmt = (
    "INSERT INTO images (trip_id, image_location, image_uri) "
    "VALUES (%s, %s, %s)"
    )
    data = (trip_id, image_locations, image_uri)
    cursor.execute(insert_stmt, data)

    data = cursor.lastrowid
    response = {}
    response['image_id'] = data

    return JsonResponse(response)

def getimage(request):
    if request.method != 'GET':
        return HttpResponse(status=404)
    
    json_data = json.loads(request.body)
    image_id_request = json_data['image_id']

    cursor = connection.cursor()
    cursor.execute('SELECT * FROM images WHERE image_id = {};'.format(image_id_request))
    data = cursor.fetchall()

    response = {}
    response['image_data'] = data
    return JsonResponse(data, safe=False)
from django.shortcuts import render
from django.http import JsonResponse, HttpResponse
from django.db import connection
from django.views.decorators.csrf import csrf_exempt
from django.conf import settings
from django.core.files.storage import FileSystemStorage
from urllib import parse
from urllib.parse import parse_qs, urlparse
from requests import post, get
import urllib.request, json, os, time, hashlib, base64

# from google.oauth2 import id_token
# from google.auth.transport import requests

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

def getuser(request, user_id):
    if request.method != 'GET':
        return HttpResponse(status=404)
    
    # json_data = json.loads(request.body)
    # user_id_request = json_data['user_id']

    cursor = connection.cursor()
    cursor.execute('SELECT username FROM users WHERE id = {};'.format(user_id))
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
    trip_people = json_data['trip_people']
    trip_description = json_data['trip_description']

    cursor = connection.cursor()

    if trip_spotify == '':
        trip_spotify = None
    
    insert_stmt = (
    "INSERT INTO trips (user_id, trip_destination, trip_start, trip_end, trip_spotify, trip_people, trip_description) "
    "VALUES (%s, %s, %s, %s, %s, %s, %s)"
    )
    data = (user_id, trip_destination, trip_start, trip_end, trip_spotify, trip_people, trip_description)
    cursor.execute(insert_stmt, data)

    return JsonResponse({})

def getalltrips(request, user_id):
    if request.method != 'GET':
        return HttpResponse(status=404)

    cursor = connection.cursor()
    cursor.execute('SELECT * FROM trips WHERE user_id = {} ORDER BY trip_id DESC;'.format(user_id))
    data = cursor.fetchall()

    response = {}
    response['trips'] = data
    return JsonResponse(response)

def gettripdata(request, trip_id):
    if request.method != 'GET':
        return HttpResponse(status=404)

    # json_data = json.loads(request.body)
    # trip_id_request = json_data['trip_id']

    cursor = connection.cursor()
    cursor.execute('SELECT * FROM trips WHERE trip_id = {};'.format(trip_id))
    data = cursor.fetchall()

    response = {}
    response['trip_data'] = data
    return JsonResponse(response)

# # POST THUMBNAIL
@csrf_exempt
def postthumbnail(request):
    if request.method != 'POST':
        return HttpResponse(status=404)

    json_data = json.loads(request.body)
    trip_id = json_data['trip_id']
    thumbnail_uri = json_data['thumbnail_uri']

    cursor = connection.cursor()
    insert_stmt = (
        "UPDATE trips "
        "SET thumbnail = %s " 
        "WHERE trip_id = %s "
        "RETURNING *;"
    )
    data = (thumbnail_uri, trip_id)
    cursor.execute(insert_stmt, data)

    data = cursor.fetchall()

    response = {}
    response['completed_request'] = data
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

def getimage(request, image_id):
    if request.method != 'GET':
        return HttpResponse(status=404)
    
    # json_data = json.loads(request.body)
    # image_id_request = json_data['image_id']

    cursor = connection.cursor()
    cursor.execute('SELECT * FROM images WHERE image_id = {};'.format(image_id))
    data = cursor.fetchall()

    response = {}
    response['image_data'] = data
    return JsonResponse(response)

def gettripimages(request, trip_id):
    if request.method != 'GET':
        return HttpResponse(status=404)

    cursor = connection.cursor()
    cursor.execute('SELECT * FROM images WHERE trip_id = {};'.format(trip_id))
    data = cursor.fetchall()

    response = {}
    response['images'] = data
    return JsonResponse(response)

CLIENT_ID = '7cd5299abbef4b70b440ed43951faa81'
CLIENT_SECRET = '96406df5ce204e29a64b3544c2492dbd'
def get_token():
    auth_string = CLIENT_ID + ":" + CLIENT_SECRET
    auth_bytes = auth_string.encode("utf-8")
    auth_base64 = str(base64.b64encode(auth_bytes), "utf-8")

    url = "https://accounts.spotify.com/api/token"
    headers = {
        "Authorization": "Basic " + auth_base64,
        "Content-Type": "application/x-www-form-urlencoded"
    }
    data = {"grant_type": "client_credentials"}
    result = post(url, headers=headers, data=data)
    json_result = json.loads(result.content)
    token = json_result["access_token"]
    return token

# https://open.spotify.com/playlist/37i9dQZF1DX8IzjtXj8ThV?si=62a694ac201146f6
# https://open.spotify.com/playlist/0qber30AnP7EpENFD93KIm?si=9ca0b77ad27c46e5
# example call for playlist above: https://ubuntu@34.75.243.151/getspotifyplaylist/0qber30AnP7EpENFD93KIm
def getspotifyplaylist(request, playlistID):
    if request.method != 'GET':
        return HttpResponse(status=404)

    # https://developer.spotify.com/documentation/web-api/reference/get-playlist
    # field = "fields=owner.id,tracks.items.track.name,name"
    url = "https://api.spotify.com/v1/playlists/{}".format(playlistID)
    headers = {"Authorization": "Bearer " + get_token()}
    result = get(url, headers=headers, params={'fields': 'owner.id,tracks.items.track(name, artists.name),name'})
    json_result = json.loads(result.content)

    response = {}
    response['data'] = json_result
    return JsonResponse(response)

@csrf_exempt
def deleteimage(request):
    if request.method != 'POST':
        return HttpResponse(status=404)

    json_data = json.loads(request.body)
    image_uri = json_data['image_uri']

    cursor = connection.cursor()
    insert_stmt = (
    "DELETE FROM images WHERE image_uri = %s RETURNING *;"
    )

    cursor.execute(insert_stmt, (image_uri,))
    data = cursor.fetchall()

    response = {}
    response['deleted_data'] = data
    return JsonResponse(response)


def getallusers(request):
    if request.method != 'GET':
        return HttpResponse(status=404)

    # json_data = json.loads(request.body)
    # user_id_request = json_data['user_id']

    cursor = connection.cursor()
    cursor.execute('SELECT id,username FROM users;')
    data = cursor.fetchall()

    response = {}
    response['users'] = data
    return JsonResponse(response)




# # GET THUMBNAIL
# def getthumbnail(request, trip_id):
#     if request.method != 'GET':
#         return HttpResponse(status=404)

#     cursor = connection.cursor()
#     cursor.execute('SELECT * FROM thumbnail WHERE trip_id = {};'.format(trip_id))
#     data = cursor.fetchall()

#     response = {}
#     response['thumbnail_data'] = data
#     return JsonResponse(response)

# # EDIT THUMBNAIL
# # insert_stmt = ("UPDATE courses SET published_date = '2020-08-01' WHERE course_id = 3;")

# # DELETE THUMBNAIL
# @csrf_exempt
# def deletethumbnail(request):
#     if request.method != 'POST':
#         return HttpResponse(status=404)

#     json_data = json.loads(request.body)
#     thumbnail_uri = json_data['thumbnail_uri']

#     cursor = connection.cursor()
#     insert_stmt = (
#     "DELETE FROM thumbnail WHERE thumbnail_uri = %s RETURNING *;"
#     )

#     cursor.execute(insert_stmt, (thumbnail_uri,))
#     data = cursor.fetchall()

#     response = {}
#     response['deleted_data'] = data
#     return JsonResponse(response)


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
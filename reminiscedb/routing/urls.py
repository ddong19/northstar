"""routing URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/4.1/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path
from app import views

urlpatterns = [
   path('admin/', admin.site.urls),
   path('adduser/', views.adduser, name='adduser'),
   path('getuser/<int:user_id>', views.getuser, name='getuser'),
   path('posttrip/', views.posttrip, name='posttrip'),
   path('getalltrips/<int:user_id>', views.getalltrips, name='getalltrips'),
   path('gettripdata/<int:trip_id>', views.gettripdata, name='gettripdata'),
   path('postthumbnail/', views.postthumbnail, name='postthumbnail'),
   path('postimage/', views.postimage, name='postimage'),
   path('getimage/<int:image_id>', views.getimage, name='getimage'),
   path('gettripimages/<int:trip_id>', views.gettripimages, name='gettripimages'),
   path('getspotifyplaylist/<str:playlistID>', views.getspotifyplaylist, name='getspotifyplaylist'),
   path('deleteimage/', views.deleteimage, name='deleteimage'),
   path('getallusers', views.getallusers, name='getallusers'),
]

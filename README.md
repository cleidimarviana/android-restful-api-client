# Android RESTful Api Client

Consuming a RESTful Web Service Client for Android
## Overview
I will not go into details of how to create an Android project since we know how to do this, and if we do not, what is missing is not the forum tutorial explaining about it.

We will make the call to the API in an AsyncTask.

##The Setup

Json example

```
[
  {
    "id": "1",
    "title": "Praca da Liberdade",
    "image_path": "http://.../imagem.jpg",
    "city_name": "Belo Horizonte - MG"
  },
  {
    "id": "2",
    "title": "Rua das Flores",
    "image_path": "http://.../imagem.jpg",jpg",
    "city_name": "Curitiba - PR"
  },
  {
    "id": "3",
    "title": "Parque Edmundo Zanone",
    "image_path": "http://.../imagem.jpg",
    "city_name": "Atibaia - SP"
  }
]
```

Let's take care of the depency:

```
dependencies {
    compile 'com.android.support:appcompat-v7:23.1.0'
    compile 'com.android.support:design:23.1.0'
    compile 'com.android.support:cardview-v7:23.1.+'
    compile 'com.android.support:recyclerview-v7:23.1.+'
    compile 'com.squareup.picasso:picasso:2.5.+'
}
```
Don't forget Android App Permissions in AndroidManifest:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

Extras
----
Loading Images with Picasso

If you want to load a remote image url into a particular ImageView, you can use Picasso to do that with:

```java 
Picasso.with(this).load(imageUrl).
  noFade().fit().into(imageView);
```
This will load an image into the specified ImageView and resize the image to fit.

Support
----
 - [Creating Lists and Cards][1]
 - [Android Material Design Snackbar][2]

Screenshots
----
![alt tag](https://github.com/cleidimarviana/android-restful-api-client/blob/master/screenshots/image3004.png "Layouts")

Feedback
----
Questions, comments, and feedback are welcome at cleidimarviana@gmail.com

License
----

The MIT License (MIT)

Copyright (c) 2015 Cleidimar Viana 

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

**Free Software, Hell Yeah!**

  [1]: http://developer.android.com/training/material/lists-cards.html
  [2]: http://www.androidhive.info/2015/09/android-material-design-snackbar-example/

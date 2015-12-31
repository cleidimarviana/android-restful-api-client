# Android RESTful Api Client

Consuming a RESTful Web Service Client for Android
## Overview
I will not go into details of how to create an Android project since we know how to do this, and if we do not, what is missing is not the forum tutorial explaining about it.

We will make the call to the API in an AsyncTask.


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

License
----

MIT

**Free Software, Hell Yeah!**

  [1]: http://developer.android.com/training/material/lists-cards.html
  [2]: http://www.androidhive.info/2015/09/android-material-design-snackbar-example/


## *COLLECTIFY*

![banner](docs/media/banner.png)

Collectify is a simple Android application intended to display a collection list. A user can then click on a specific collection to view a list of products as well its inventory.

An exercise in the use of recyclerviews, as well as REST to fetch data (although I do not recommend using loopj in production builds and my use of nested get requests may not be ideal).
Feel free to also use it as a basic bottom navigation template.

## USAGE

If you plan on using the recycler adapters, you must use Square's Picasso library for loading images
Alternatively, you could use Glide, just change or delete the line of code loading the image.

```
implementation 'com.squareup.picasso:picasso:2.71828'
```

## ACKNOWLDGEMENTS  

The icons used in the splash screen and toolbar are provided by Freepik from www.flaticon.com 

All other images are fetched through JSON and are property of Shopify.

## LICENSE 
 
MIT license, but TTLDR; do whatever you want and use this code as you please (no attrition or anything needed)





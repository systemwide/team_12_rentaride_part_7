function myMap() {
    var mapCanvas = document.getElementById("map");
    var mapOptions = {
        center: new google.maps.LatLng(33.950769, -83.377735),
        zoom: 10
    };
    var map = new google.maps.Map(mapCanvas, mapOptions);
}

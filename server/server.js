
var express = require("express");
var app = express();
var port = process.env.PORT || 8088;
var https =require("https");
var url = require("url");
var geohash = require('ngeohash');
var spotify = require('spotify-web-api-node');


// var cors = require("cors");
// app.use(cors());

app.use(express.static('public'));


// app.get('/cur_loc', function(req, res){
//
//     res.setHeader("Content-Type","text/plain");
//     res.setHeader("Access-Control-Allow-Origin","*");
//
//     https.get("https://ip-api.com/json", function(req2,res2){
//         var cur_res = "";
//         req2.on('data',function(data){
//             cur_res += data;
//         });
//         req2.on('end',function(){
//             console.log("server cur loc :" + cur_res);
//             return res.send(cur_res);
//         });
//     });
// });


app.get('/auto_complete', function(req, res){

    res.setHeader("Content-Type","text/plain");
    res.setHeader("Access-Control-Allow-Origin","*");
    var para = url.parse(req.url, true).query;

    var autoUrl = "https://app.ticketmaster.com/discovery/v2/suggest?apikey=x1yqJ2mxEfexyGTEKjQwOSJKzH3sPPAB" + "&keyword=" + para.autoComplete;

    https.get(autoUrl, function(req2,res2){
        var auto_res = "";
        req2.on('data',function(data){
            auto_res += data;
        });
        req2.on('end',function(){
            return res.send(auto_res);
        });
    });
});




app.get('/input_loc', function(req, res){

    res.setHeader("Content-Type","text/plain");
    res.setHeader("Access-Control-Allow-Origin","*");
    var para = url.parse(req.url, true).query;

    console.log("input loc: " + para.inputLoc);/////////////////////

    var inputUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" + para.inputLoc + "&key=AIzaSyD3vy_5SYNS_4mmdb1xTm8XaLmi2JS09vo";

    https.get(inputUrl, function(req2,res2){
        var input_res = "";
        req2.on('data',function(data){
            input_res += data;
        });
        req2.on('end',function(){
            var inputLocRst = JSON.parse(input_res);
            var curLat = inputLocRst["results"][0]["geometry"]["location"]["lat"];
            var curLng = inputLocRst["results"][0]["geometry"]["location"]["lng"];

            var loc = {
                lat : curLat,
                lng: curLng
            };
            var rst = JSON.stringify(loc);
            console.log("after encode");
            console.log(rst);
            console.log(typeof rst);

            return res.send(rst);


        });
    });
});


app.get('/search_rst', function (req, res) {

    res.setHeader("Content-Type","text/plain");
    res.setHeader("Access-Control-Allow-Origin","*");
    var params = url.parse(req.url, true).query;

    var category = params.category;

    var segmentId = "";
    if(category === "Music"){
        segmentId = "KZFzniwnSyZfZ7v7nJ";
    }else if(category === "Sports"){
        segmentId = "KZFzniwnSyZfZ7v7nE";
    }else if(category === "Arts & Theatre"){
        segmentId = "KZFzniwnSyZfZ7v7na";
    }else if(category === "Film"){
        segmentId = "KZFzniwnSyZfZ7v7nn";
    }else if(category === "Miscellaneous"){
        segmentId = "KZFzniwnSyZfZ7v7n1";
    }


    var geoPoint = geohash.encode(parseFloat(params.locLat), parseFloat(params.locLng));

    console.log("keyword is:" + params.keyword);//////////////////////////////////////////
    console.log("segmentId is:" + segmentId);//////////////////////////////////////////
    console.log("radius is:" + params.distance);//////////////////////////////////////////
    console.log("unit is:" + params.unit);//////////////////////////////////////////
    console.log("lat is:" + params.locLat);//////////////////////////////////////////
    console.log("lng is:" + params.locLng);//////////////////////////////////////////
    console.log("geopoint is:" + geoPoint);//////////////////////////////////////////

    var ticketUrl = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=x1yqJ2mxEfexyGTEKjQwOSJKzH3sPPAB" + "&keyword=" + params.keyword + "&segmentId=" + segmentId + "&radius=" + params.distance + "&unit=" + params.unit +"&geoPoint=" + geoPoint;

    https.get(ticketUrl, function(req2,res2){
        var search_res = "";
        req2.on('data',function(data){
            search_res += data;
        });
        req2.on('end',function(){

            var searRst = JSON.parse(search_res);

            // var searRst = search_res;

            var array = [];

            if(searRst["_embedded"] == undefined){

            }else{

                var compons = searRst["_embedded"]["events"];
                for(var i = 0; i < compons.length; i++){

                    var name = compons[i]["name"];
                    var date = compons[i]["dates"]["start"]["localDate"];
                    var time = compons[i]["dates"]["start"]["localTime"];
                    var rst_time = date + " " + time;
                    var venue = compons[i]["_embedded"]["venues"][0]["name"];
                    var venue_lat = compons[i]["_embedded"]["venues"][0]["location"]["latitude"];
                    var venue_lng =  compons[i]["_embedded"]["venues"][0]["location"]["longitude"];

                    var id = compons[i]["id"];
                    var category = compons[i]["classifications"][0]["segment"]["name"];


                    //test artist array
                    var artArray = [];
                    for(var j = 0 ; j < compons[i]["_embedded"]["attractions"].length; j++){
                        var cur = compons[i]["_embedded"]["attractions"][j]["name"];
                        var artItem = {
                            name: cur
                        };
                        artArray.push(artItem);
                    }

                    var item = {
                        rst_name: name,
                        rst_time: rst_time,
                        rst_venue: venue,
                        rst_id: id,
                        rst_category: category,
                        rst_artist: artArray,
                        rst_lat:venue_lat,
                        rst_lng:venue_lng
                    };
                    array.push(item);
                }
            }

            res.contentType('application/json');
            return res.send(JSON.stringify(array));

        });
    });
});


// invoke event detail API
app.get('/event_detail', function(req, res){

    res.setHeader("Content-Type","text/plain");
    res.setHeader("Access-Control-Allow-Origin","*");
    var para = url.parse(req.url, true).query;

    console.log("event id : " + para.eventId);////////////////////////

    var detailUrl = "https://app.ticketmaster.com/discovery/v2/events/" +para.eventId + "?apikey=x1yqJ2mxEfexyGTEKjQwOSJKzH3sPPAB&";

    https.get(detailUrl, function(req2,res2){
        var detail_res = "";
        req2.on('data',function(data){
            detail_res += data;
        });
        req2.on('end',function(){

            var detailRst = JSON.parse(detail_res);

            var detail_name = "";
            var detail_team = "";
            var detail_venue = "";
            var detail_time = "";
            var detail_category = "";
            var detail_price = "";
            var detail_status = "";
            var detail_buyUrl = "";
            var detail_seatUrl = "";
            var detail_type = "";
            var detail_lat = "";
            var detail_lng = "";
            var detail_artist = [];
            var detail_id = "";

            if(detailRst["id"] !== undefined){
                detail_id = detailRst["id"];
            }

            if(detailRst["name"] !== undefined){
                detail_name = detailRst["name"];
            }

            if(detailRst["_embedded"] !== undefined){
                    if(detailRst["_embedded"]["venues"] !== undefined){
                        detail_lat = detailRst["_embedded"]["venues"][0]["location"]["latitude"];
                        detail_lng = detailRst["_embedded"]["venues"][0]["location"]["longitude"];

                    }

                if(detailRst["_embedded"]["attractions"] !== undefined){

                    var array = [];
                    for(var i = 0 ; i < detailRst["_embedded"]["attractions"].length ; i++){
                        var cur = detailRst["_embedded"]["attractions"][i]["name"];
                        array.push(cur);
                        var art = {
                            name: cur
                        };
                        detail_artist.push(art);
                    }
                    detail_team = array.join(" | ");
                }

                if(detailRst["_embedded"]["venues"] !== undefined){
                    detail_venue = detailRst["_embedded"]["venues"][0]["name"];
                }

            }


            if(detailRst["dates"] !== undefined){
                var date = detailRst["dates"]["start"]["localDate"];
                var time = detailRst["dates"]["start"]["localTime"];
                detail_time =  date + " " + time ;
            }

            if(detailRst["classifications"] !== undefined){
                var left = detailRst["classifications"][0]["segment"]["name"];
                detail_type = detailRst["classifications"][0]["segment"]["name"];
                var right = detailRst["classifications"][0]["genre"]["name"];
                detail_category = left + " | " + right;
            }

            if(detailRst["priceRanges"] !== undefined){
                var min = detailRst["priceRanges"][0]["min"];
                var max = detailRst["priceRanges"][0]["max"];
                detail_price = "$" + min + " ~ " + "$" + max;
            }

            if(detailRst["dates"] !== undefined){
                detail_status = detailRst["dates"]["status"]["code"];
            }


            if(detailRst["url"] !== undefined){
                detail_buyUrl = detailRst["url"];
            }

            if(detailRst["seatmap"] !== undefined){
                detail_seatUrl = detailRst["seatmap"]["staticUrl"];
            }


            var item = {
                detail_id:detail_id,
                detail_name: detail_name,
                detail_type: detail_type,
                detail_lat:detail_lat,
                detail_lng:detail_lng,
                detail_artist:detail_artist,
                detail_team: detail_team,
                detail_venue: detail_venue,
                detail_time: detail_time,
                detail_category: detail_category,
                detail_price: detail_price,
                detail_status: detail_status,
                detail_buyUrl: detail_buyUrl,
                detail_seatUrl: detail_seatUrl
            };

            // success json array test !!!!
            // var compones = inputLocRst["results"][0]["address_components"];
            // var comArray = [];
            //
            // for(var j = 0; j < compones.length ; j++){
            //     var curL = compones[j]["long_name"];
            //     var curS = compones[j]["short_name"];
            //     var item = {
            //         longN: curL,
            //         shortN: curS
            //     };
            //     comArray.push(item);
            // }
            // console.log("array is: ");//////////////////////////////////////////
            // console.log(comArray.toString());////////////////////////////////////
            //
            // res.contentType('application/json');



            // console.log("send detail json is: ");/////////////////////
            // console.log(JSON.stringify(item));////////////////////////////

            res.contentType('application/json');
            return res.send(item);

        });
    });
});

//google get image api
app.get('/img_rst', function(req, res){

    res.setHeader("Content-Type","text/plain");
    res.setHeader("Access-Control-Allow-Origin","*");
    var para = url.parse(req.url, true).query;

    console.log("search content is : " + para.search);/////////////////////////////////////

    var imgUrl = "https://www.googleapis.com/customsearch/v1?q="+ para.search +"&cx=005172689902481577915:rf3fcc2ixcm&imgSize=huge&imgType=news&num=8&searchType=image&key=AIzaSyAm92MOn5DSlepEbGHXgryJVg6BoYzsmws";

    https.get(imgUrl, function(req2,res2){

        var img_res = "";
        req2.on('data',function(data){
            img_res += data;
        });
        req2.on('end',function(){

            var imgRst = JSON.parse(img_res);

            var img1 = "";
            var img2 = "";
            var img3 = "";
            var img4 = "";
            var img5 = "";
            var img6 = "";
            var img7 = "";
            var img8 = "";

            if(imgRst["items"] !== undefined){
                if(imgRst["items"].length >= 1){
                    img1 = imgRst["items"][0]["link"];
                }

                if(imgRst["items"].length >= 2){
                    img2 = imgRst["items"][1]["link"];
                }

                if(imgRst["items"].length >= 3){
                    img3 = imgRst["items"][2]["link"];
                }

                if(imgRst["items"].length >= 4){
                    img4 = imgRst["items"][3]["link"];
                }

                if(imgRst["items"].length >= 5){
                    img5 = imgRst["items"][4]["link"];
                }

                if(imgRst["items"].length >= 6){
                    img6 = imgRst["items"][5]["link"];
                }

                if(imgRst["items"].length >= 7){
                    img7 = imgRst["items"][6]["link"];
                }

                if(imgRst["items"].length >= 8){
                    img8 = imgRst["items"][7]["link"];
                }
            }

            var item = {
                img1:img1,
                img2:img2,
                img3:img3,
                img4:img4,
                img5:img5,
                img6:img6,
                img7:img7,
                img8:img8
            };

            res.contentType('application/json');
            return res.send(item);

            // return res.send(img_res);
        });
    });
});


// get upcoming id api
app.get('/upcoming_id', function(req, res){

    res.setHeader("Content-Type","text/plain");
    res.setHeader("Access-Control-Allow-Origin","*");
    var para = url.parse(req.url, true).query;

    console.log("upcoming venue name is : " + para.venueName);/////////////////////////////////////

    var idUrl = "https://api.songkick.com/api/3.0/search/venues.json?query="+ para.venueName +"&apikey=lyqb4LQZWDoK45WH";

    https.get(idUrl, function(req2,res2){

        var id_res = "";
        req2.on('data',function(data){
            id_res += data;
        });
        req2.on('end',function(){

            var upcomingIdRst = JSON.parse(id_res);

            var venue_id = upcomingIdRst["resultsPage"]["results"]["venue"][0]["id"];

            console.log("venue id is: " + venue_id);/////////////////////////////////////////////////////

            var item = {
                venue_id: venue_id
            };

            res.contentType('application/json');
            return res.send(item);
        });
    });
});


//get upcoming events api
app.get('/upcoming_event', function(req, res){

    res.setHeader("Content-Type","text/plain");
    res.setHeader("Access-Control-Allow-Origin","*");
    var para = url.parse(req.url, true).query;

    console.log("upcoming id is : " + para.upcomingId);/////////////////////////////////////

    var upcomingUrl = "https://api.songkick.com/api/3.0/venues/"+ para.upcomingId +"/calendar.json?apikey=lyqb4LQZWDoK45WH";

    https.get(upcomingUrl, function(req2,res2){

        var upcoming_res = "";
        req2.on('data',function(data){
            upcoming_res += data;
        });
        req2.on('end',function(){

            var upcomingRst = JSON.parse(upcoming_res);

            if(upcomingRst["resultsPage"]["results"]["event"] !== undefined){
                var list = upcomingRst["resultsPage"]["results"]["event"];

                var rstArray = [];

                var upc_display = "";
                var upc_artist = "";
                var upc_time = "";
                var ucp_type = "";
                var upc_uri = "";

                for(var i = 0; i < list.length; i++){

                    if(list[i]["displayName"] !== undefined){
                        upc_display = list[i]["displayName"];
                    }
                    if(list[i]["performance"] !== undefined){
                        upc_artist = list[i]["performance"][0]["displayName"];
                    }
                    if(list[i]["start"] !== undefined){
                        var date = "";
                        var time = "";
                        if(list[i]["start"]["date"] !== undefined){
                            date = list[i]["start"]["date"];
                        }
                        if(list[i]["start"]["time"] !== undefined){
                            time = list[i]["start"]["time"];
                        }
                        upc_time = date + " " + time;
                    }
                    if(list[i]["type"] !== undefined){
                        ucp_type = list[i]["type"];
                    }

                    if(list[i]["uri"] !== undefined){
                        upc_uri = list[i]["uri"];
                    }

                    var item = {
                        upc_display: upc_display,
                        upc_artist: upc_artist,
                        upc_time: upc_time,
                        upc_type: ucp_type,
                        upc_uri: upc_uri
                    };

                    rstArray.push(item);
                }
            }


            res.contentType('application/json');
            return res.send(JSON.stringify(rstArray));
        });
    });
});

// spotify api
app.get('/artist_rst', function(req, res){

    res.setHeader("Content-Type","text/plain");
    res.setHeader("Access-Control-Allow-Origin","*");
    var para = url.parse(req.url, true).query;

    var artist = para.artist;

    console.log("artist is : " + artist);///////////////////////////////////////////////


    //spotify api
    var clientId = '442bc4722cd3409b85a47363fa8c8b40',
        clientSecret = '1ae83fd89576443cb8889f8f60d5816c';

    var spotifyApi = new spotify({
        clientId: clientId,
        clientSecret: clientSecret
    });

    spotifyApi.clientCredentialsGrant().then(
        function(data) {

            spotifyApi.setAccessToken(data.body['access_token']);
            spotifyApi.searchArtists(artist)
                .then(function(data) {

                    console.log("artist: " + artist + "rst is: ");/////////////////////////////////
                    console.log(data.body);///////////////////////////////////////////////////////////////
                    console.log("rst items is: ");//////////////////////////////////////
                    console.log(data.body.artists.items);////////////////////////

                    var art_name = "";
                    var art_follower = "";
                    var art_pop = "";
                    var art_spotify = "";

                    if(data.body.artists.items !== undefined){
                        // var artistRst = JSON.parse(data.body.artists.items);
                        var cur = data.body.artists.items[0];
                            if(cur.name !== undefined){
                                art_name = cur.name;
                            }

                            if(cur.followers !== undefined){
                                art_follower = cur.followers.total;

                                console.log("follower type");/////////////////////////
                                console.log(typeof art_follower);////////////////////////
                            }

                            if(cur.popularity !== undefined){
                                art_pop = cur.popularity;
                            }

                            if(cur.external_urls !== undefined){
                                art_spotify = cur.external_urls.spotify;
                            }
                    }

                    var item = {
                        art_name: art_name,
                        art_follower:art_follower,
                        art_pop:art_pop,
                        art_spotify:art_spotify
                    };

                    res.contentType('application/json');
                    res.send(item);
                    // res.send(data.body);

                }, function(err) {
                    console.error(err);
                });
        },
        function(err) {
            console.log('Wrong!', err);
        }
    );

});


//get venue info api
app.get('/venue_rst', function(req, res){

    res.setHeader("Content-Type","text/plain");
    res.setHeader("Access-Control-Allow-Origin","*");
    var para = url.parse(req.url, true).query;

    console.log("venue is : " + para.venue);/////////////////////////////////////

    var venueUrl = "https://app.ticketmaster.com/discovery/v2/venues?apikey=x1yqJ2mxEfexyGTEKjQwOSJKzH3sPPAB&keyword=" + para.venue;

    https.get(venueUrl, function(req2,res2){

        var venue_res = "";
        req2.on('data',function(data){
            venue_res += data;
        });
        req2.on('end',function(){
            var venueRst = JSON.parse(venue_res);

            var venue_address = "";
            var venue_city = "";
            var venue_phone = "";
            var venue_hour = "";
            var venue_general = "";
            var venue_child = "";
            var venue_lat = "";
            var venue_lng = "";

            var item = {};

            if(venueRst["_embedded"]["venues"][0] !== undefined){
                venue_address = venueRst["_embedded"]["venues"][0]["address"]["line1"];
                if(venueRst["_embedded"]["venues"][0]["state"] !== undefined){
                    venue_city = venueRst["_embedded"]["venues"][0]["state"]["name"];
                }
                if(venueRst["_embedded"]["venues"][0]["boxOfficeInfo"] !== undefined){
                    if(venueRst["_embedded"]["venues"][0]["boxOfficeInfo"]["phoneNumberDetail"] !== undefined){
                        venue_phone = venueRst["_embedded"]["venues"][0]["boxOfficeInfo"]["phoneNumberDetail"];
                    }

                    if(venueRst["_embedded"]["venues"][0]["boxOfficeInfo"]["openHoursDetail"] !== undefined){
                        venue_hour = venueRst["_embedded"]["venues"][0]["boxOfficeInfo"]["openHoursDetail"];
                    }
                }

                if(venueRst["_embedded"]["venues"][0]["generalInfo"] !== undefined){
                    venue_general = venueRst["_embedded"]["venues"][0]["generalInfo"]["generalRule"];
                    venue_child = venueRst["_embedded"]["venues"][0]["generalInfo"]["childRule"];
                }

                venue_lat = venueRst["_embedded"]["venues"][0]["location"]["latitude"];
                venue_lng = venueRst["_embedded"]["venues"][0]["location"]["longitude"];

                item = {
                    venue_address: venue_address,
                    venue_city: venue_city,
                    venue_phone: venue_phone,
                    venue_hour: venue_hour,
                    venue_general: venue_general,
                    venue_child: venue_child,
                    venue_lat: venue_lat,
                    venue_lng: venue_lng
                };

            }else{
                item = {
                    no_result: "yes"
                };
            }

            // console.log("send venue tab");////////////////////////////////////
            // console.log(JSON.stringify(item));/////////////////////////////////////////

            console.log("lng type!!");
            console.log(typeof venue_lng);

            res.contentType('application/json');
            return res.send(item);

        });
    });
});


var server = app.listen(port, function () {

    console.log("express started !");

    var host = server.address().address;////////////////////////////
    var port = server.address().port;/////////////////////////////////

    console.log("host is : "+ host + " port is :" + port)

});
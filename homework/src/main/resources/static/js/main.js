
(function ($) {
    "use strict";

    /*==================================================================
    [ World Map ]*/

    var coords = [38.7223, -9.1393]; // default = Lisbon
    var worldmap = L.map('mapid').setView(coords, 8);
    var marker = L.marker(coords);

    function mapping () {
        L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}', {
            attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
            maxZoom: 18,
            id: 'mapbox.streets',
            accessToken: 'pk.eyJ1IjoiZmlsaXBlcGlyZXM5OCIsImEiOiJjanYzbmUzODUxNDVlNDNwOTB2M290eXo4In0.VgJ4YV1nGaxXglw-c8I5FA'
        }).addTo(worldmap);
        marker.addTo(worldmap);
        //marker.bindPopup("<b>Hello world!</b><br>I am a popup."); //.openPopup();
    }
    mapping();

    var popup = L.popup()
        .setLatLng(coords)
        .setContent("I am a standalone popup.");
        //.openOn(worldmap);

    function onMapClick(e) {
        marker.remove(worldmap);
        coords = [e.latlng.lat, e.latlng.lng];
        marker = L.marker(coords).addTo(worldmap);
        //console.log("You clicked the map at " + e.latlng.toString());
    }
    worldmap.on('click', onMapClick);

    function show_main_div() { document.getElementById('main-div').style.display = "block"; };
    function show_form() { document.getElementById('form').style.display = "block"; };
    function hide_form() { document.getElementById('form').style.display = "none"; };
    function show_table() { document.getElementById('table').style.display = "block"; };
    function hide_table() { document.getElementById('table').style.display = "none"; };

    /*==================================================================
    [ REST ]*/

    var input = $('.validate-input .input100');

    $('.weather-forecast').on('click',function(){
        console.log("weather-forecast clicked");
        // check chosen location
        if(input[3].value != "" && input[4].value != "") {
            coords = [parseFloat(input[3].value), parseFloat(input[4].value)];
        }
        // check chosen option
        var chosen_url = "http://localhost:8080/weather/now/" + coords[0] + "," + coords[1];
        if(input[0].value != "") {
            chosen_url = "http://localhost:8080/weather/recent/" + coords[0] + "," + coords[1] + "/" + input[0].value;
        } else if(input[1].value != "" && input[2].value != "") {
            chosen_url = "http://localhost:8080/weather/period/" + coords[0] + "," + coords[1] + "/" + input[1].value + "," + input[2].value;
        }
        // updating map
        marker.remove(worldmap);
        marker = L.marker(coords).addTo(worldmap);
        worldmap.setView(coords,8);
        // transform html
        hide_form();
        show_table();
        document.getElementById('main-div').style.width = "1000px";
        show_main_div();
        // send request to internal api                                     !!! error here !!!
        var response;
        $.ajax({
            type: 'GET',
            async: false,
            url: chosen_url,//"http://localhost:8080/weather/now/40.6405,-8.6538",
            error: function(textStatus, errorThrown) {
                console.log(textStatus);
                console.log(errorThrown);
            },
            success: function (data) {
                response = $.parseJSON(data);
                console.log(response);
                append_json(response);
            }
        }); 
    });

    function append_json(data){
        var table = document.getElementById('table-body');
        var count = 1;
        data.forEach(function(object) {
            var tr = document.createElement('tr');
            tr.setAttribute("id","tr-"+count);
            tr.innerHTML = 
                '<td>' + count + '</td>' +
                '<td>' + formatDate((new Date(object.time * 1000))) + '</td>' +
                '<td>' + object.summary + '</td>' +
                '<td>' + object.temperatureMax + '</td>' + 
                '<td>' + object.temperatureMin + '</td>' + 
                '<td>' + object.humidity + '</td>' + 
                '<td>' + object.precipProbability + '</td>';
            table.appendChild(tr);
            count++;
            console.log(tr);
        });
    };

    function formatDate(date) {
        var d = new Date(date),
            month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear();

        if (month.length < 2) month = '0' + month;
        if (day.length < 2) day = '0' + day;

        return [year, month, day].join('-');
    };

    $('.past-searches').on('click',function(){
        console.log("past-searches clicked");
        // transform html
        hide_form();
        show_table();
        document.getElementById('main-div').style.width = "800px";
        show_main_div();
        // send request to internal api                                           !!! error here !!!
        var response = $.ajax({
            url: "localhost:8080/weather/cached",
            method: 'GET',
            success: function(response) {  
                alert(response);
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                alert(textStatus + " - " + errorThrown);
            }
        });
    });

    /*==================================================================
    [ Form Validation ]*/
    
    /*$('.validate-form').on('submit',function(){
        // validate form
        var check = true;
        //for(var i=0; i<input.length; i++) {
        //    if(validate(input[i]) == false){
        //        showValidate(input[i]);
        //        check=false;
        //    }
        //} 
        // check chosen location
        if(input[3].value != "" && input[4].value != "") {
            coords = [parseFloat(input[3].value), parseFloat(input[4].value)];
        }
        // check chosen option
        var chosen_url = "localhost:8080/weather/now/" + coords[0] + "," + coords[1];
        if(input[0].value != "") {
            chosen_url = "localhost:8080/weather/recent/" + coords[0] + "," + coords[1] + "/" + input[0].value;
        } else if(input[1].value != "" && input[2].value != "") {
            chosen_url = "localhost:8080/weather/period/" + coords[0] + "," + coords[1] + "/" + input[1].value + "," + input[2].value;
        }
        // updating map
        marker.remove(worldmap);
        marker = L.marker(coords).addTo(worldmap);
        worldmap.setView(coords,8);
        // process submission
        if(check) {
            // transform html
            hide_form();
            show_table();
            document.getElementById('main-div').style.width = "800px";
            show_main_div();
            // send request to internal api                                           !!! error here !!!
            var response = $.ajax({
                url: chosen_url,//"localhost:8080/weather/now/40.6405,-8.6538",
                method: 'GET',
                success: function(response) {  
                    alert(response);
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    alert(textStatus + " - " + errorThrown);
                }
            });
        }
        return check; // https://stackoverflow.com/questions/51275730/populate-html-table-with-json-data
    });*/

    $('.validate-form .input100').each(function(){
        $(this).focus(function(){
           hideValidate(this);
        });
    });

    function validate (input) {
        if($(input).attr('type') == 'email' || $(input).attr('name') == 'email') {
            if($(input).val().trim().match(/^([a-zA-Z0-9_\-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+\.)+))([a-zA-Z]{1,5}|[0-9]{1,3})(\]?)$/) == null) {
                return false;
            }
        }
        else {
            if($(input).val().trim() == ''){
                return false;
            }
        }
    }

    function showValidate(input) {
        var thisAlert = $(input).parent();
        $(thisAlert).addClass('alert-validate');
    }

    function hideValidate(input) {
        var thisAlert = $(input).parent();
        $(thisAlert).removeClass('alert-validate');
    }

    

})(jQuery);
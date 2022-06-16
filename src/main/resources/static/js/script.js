$(document).ready(function () {

    var airportsTable = $('#airportsTable').DataTable({
        ajax: '../getAirports',
          columns: [
              { data: 'icao' },
              { data: 'lat' },
              { data: 'lng' },
              { data: 'alt' },
              { data: 'iata' }
          ],
    });

    $('#airportsTable tbody').on('click', 'tr', function () {

            var data = airportsTable.row(this).data();
            $("#sidStarModalTitle").html("Aiport ICAO: " + data["icao"])
            $("#sidStarFormSTinput").val(data["icao"])
            const sidStarModal = new bootstrap.Modal(document.getElementById('sidStarModal'), {})
            $("#sidStarFormSelect").val("default")
            $("#sidStarModalInfo").html("")
            sidStarModal.show()
     });

    $( "#sidStarFormSelect" ).change(function() {
        stdIntrutmentType = $( "#sidStarFormSelect option:selected").val()
        if(stdIntrutmentType == "sids" || stdIntrutmentType == "stars"){
            $.getJSON({url: "../getTopTwoAssoWaypoints?icao="+$("#sidStarFormSTinput").val() +"&stdIntrutmentType=" + stdIntrutmentType, success: function(data){

                var content = '<div class="container"><table class="table">'
                content += "<thead class='thead-dark'><th>Waypoint name</th><th>Count</th></thead><tbody>"
                for(i=0; i<data["topWaypoints"].length; i++){
                    toAdd = '<tr><td>' + data["topWaypoints"][i]["name"] + "</td><td>" + data["topWaypoints"][i]["count"] + '</td></tr>';
                    content += toAdd
                }
                content += "</tbody></table>"

                jsonRepresentation = "<h5>JSON DATA</h5><p>" + JSON.stringify(data,null,2) + "'</p>"
                content += jsonRepresentation
                content += "</div>"
                $("#sidStarModalInfo").html(content)
            }});
        }else{
            $("#sidStarModalInfo").html("")
        }
    });


});
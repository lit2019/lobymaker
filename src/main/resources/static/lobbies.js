function getLobbySearchUrl(){
    return "/lobby/get/"
}

function getAllLobbies(){
    console.log("getting lobbies")
	var url = getLobbySearchUrl()+"false";
	$.ajax({
	   url: url,
	   type: 'GET',
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(data) {
	   		console.log("Lobby data fetched");
	   		console.log(data);
	   		displayLobbyList(data);     //...
	   },
	   error: function(error){
	   	    message = error.responseJSON.message;
	   		makeToast(false, message, downloadErrors);

	   }
	});
}
function displayLobbyList(data) {
    console.log('Printing lobby data');
    var $tbody = $('#lobby-table').find('tbody');
    $tbody.empty();
    for (var i in data) {
        var e = data[i];
        var buttonHtml = '<td><button class="btn" onclick="displayLobbyMembers(' + e.id + ')"><i class="fa fa-chevron-circle-down"></i>&nbsp;&nbsp;&nbsp;Show Members</button></td>';
        var row = '<tr>' +
            '<td>' + e.createdAt + '</td>' +
            '<td>' + e.title + '</td>' +
            '<td>' + e.adminUsername + '</td>' +
            buttonHtml +
            '</tr>';
        $tbody.append(row);
    }
}
function displayLobbyMembers(lobbyId) {
    var url = "/lobby/members/" + lobbyId;
    console.log(url)
    $.ajax({
        url: url,
        type: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (data) {
            console.log("Lobby members data fetched");
            console.log(data);
            var $tableBody = $('#lobby-members-table-body');
            $tableBody.empty();
            for (var i in data) {
                var member = data[i];
                var row = '<tr>' +
                    '<td>' + member.username + '</td>' +
                    '</tr>';
                $tableBody.append(row);
            }
            $('#lobby-members-modal').modal('show');
        },
        error: function (error) {
            message = error.responseJSON.message;
            makeToast(false, message, downloadErrors);
        }
    });
}

// ...
function createNewLobby() {
    var title = $('#lobby-title-input').val();

    // Perform validation if needed

    var lobbyForm = {
        title: title
    };

    $.ajax({
        url: '/lobby/create',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(lobbyForm),
        success: function () {
            // Handle success, such as refreshing the lobby list
            getAllLobbies();
            $('#create-lobby-modal').modal('hide');
        },
        error: function (error) {
            message = error.responseJSON.message;
            makeToast(false, message, downloadErrors);
        }
    });
}

// ...


function init(){
    getAllLobbies();
}

$(document).ready(init);

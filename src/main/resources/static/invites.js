


function getReceivedInvites() {
console.log("fetching invites")
  $.ajax({
    url: '/lobby/invitation/received',
    method: 'GET',
    success: function (invitations) {
        console.log(invitations);
      displayInvites(invitations);
    },
    error: function (error) {
            message = error.responseJSON.message;
            makeToast(false, message, null);
    }
  });
}

function displayInvites(invitations){
    var tableBody = $('#invites-table tbody');
          tableBody.empty(); // Clear previous table data

      invitations.forEach(function (invitation) {
        var buttonHtml = '<td><button class="btn" onclick="accept(' + invitation.id + ')">Accept</button><button class="btn" onclick="deny(' + invitation.id + ')">Decline</button></td>';

        var row = $('<tr>');
        row.append($('<td>').text(convertDate(invitation.createdAt)));
        row.append($('<td>').text(invitation.senderUsername));
        row.append($('<td>').text(invitation.lobbyTitle));
        row.append(buttonHtml);
        tableBody.append(row);
      });
}

function accept(inviteId){

  $.ajax({
    url: '/lobby/invitation/update/'+inviteId+'/ACCEPTED',
    method: 'PUT',
    success: function (invitations) {
        console.log(invitations);
        getReceivedInvites();
        makeToast(true, "joined lobby", null);
    },
    error: function (error) {
            message = error.responseJSON.message;
            makeToast(false, message, null);
    }
  });
}
function deny(inviteId){

  $.ajax({
    url: '/lobby/invitation/update/'+inviteId+'/DECLINED',
    method: 'PUT',
    success: function (invitations) {
        console.log(invitations);
        getReceivedInvites();
    },
    error: function (error) {
            message = error.responseJSON.message;
            makeToast(false, message, null);

    }
  });
}

function init(){
    getReceivedInvites();
}

$(document).ready(init);

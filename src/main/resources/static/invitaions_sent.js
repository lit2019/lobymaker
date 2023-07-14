
var inviteStatus = null;

function refreshTable() {
  var status = document.getElementById("status").value;
  inviteStatus = status;
  getSentInvites();
}

function getSentInvites() {
url = '/lobby/invitation/sent';
if (inviteStatus!='null' && inviteStatus!=null){
    url = url+'?status='+inviteStatus;
}
console.log(url);
console.log("fetching invites")
  $.ajax({
    url: url,
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
      buttonHtml = '<td></td>';
      if  (invitation.inviteStatus=='SENT'){
         buttonHtml = '<td><button class="btn" onclick="revokeInvite(' + invitation.id + ')"><i class="fa far fa-times-circle"></i>&nbsp;&nbsp;&nbsp;Revoke</button></td>';
      }

        var row = $('<tr>');
        row.append($('<td>').text(convertDate(invitation.createdAt)));
        row.append($('<td>').text(invitation.receiverUsername));
        row.append($('<td>').text(invitation.inviteStatus));
        row.append($('<td>').text(invitation.lobbyTitle));
        row.append(buttonHtml);
        tableBody.append(row);
      });
}


function revokeInvite(inviteId){
    console.log("reached here")
//    console.log(url)
  $.ajax({
    url: 'http://localhost:8080/lobby/invitation/revoke/'+inviteId,
    method: 'PUT',
    success: function (invitations) {
        console.log(invitations);
        getSentInvites();
    },
    error: function (error) {
            message = error.responseJSON.message;
            makeToast(false, message, null);

    }
  });
}

function init(){
    getSentInvites();
}

$(document).ready(init);

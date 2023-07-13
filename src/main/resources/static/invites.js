


function getAllInvites() {
console.log("fetching invites")
  $.ajax({
    url: '/lobby/invitation/get',
    method: 'GET',
    success: function (invitations) {
      var tableBody = $('#invites-table tbody');
      tableBody.empty(); // Clear previous table data

      invitations.forEach(function (invitation) {
        var row = $('<tr>');
        row.append($('<td>').text(invitation.createdAt));
        row.append($('<td>').text(invitation.senderUsername));
        row.append($('<td>').text(invitation.lobbyTitle));
        row.append($('<td>').text(invitation.inviteStatus));
        tableBody.append(row);
      });
    },
    error: function (error) {
      console.error('Failed to fetch invitations:', error);
    }
  });
}


function init(){
    getAllInvites();
}

$(document).ready(init);

var app = angular.module("EventsManagement", []);
  
// generate UUID
function create_UUID(){
    var dt = new Date().getTime();
    var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = (dt + Math.random()*16)%16 | 0;
        dt = Math.floor(dt/16);
        return (c=='x' ? r :(r&0x3|0x8)).toString(16);
    });
    return uuid;
}

//Controller Part
app.controller("EventsManagementController", function ($scope, $http) {

  //Initialize page with default data
  $scope.form = {
    id: create_UUID(),
    message: "",
    severity: ""
  };
  $scope.vform = {
    id: create_UUID(),
    message: "",
    severity: ""
  }

  // HTTP POST Handler: Publish a new event
  $scope.publish = function (routeType) {
    var method = "";
    var url = "";
    var data = {};

    // POST operation
    method = "POST";
    if (routeType == 'producer') {
      url = "/producer/post";
      data.id = $scope.form.id;
      data.message = $scope.form.message;
      data.severity = $scope.form.severity;
    } else {
      url = "/vertx/post";
      data.id = $scope.vform.id;
      data.message = $scope.vform.message;
      data.severity = $scope.vform.severity;
    } 
    data.event_timestamp = new Date().getTime().toString();

    $http({
      method: method,
      url: url.toString(),
      data: angular.toJson(data),
      headers: {
        'Content-Type': 'application/json'
      }
    }).then(_success, _error);
  };

  function _success(response) {
    _clearForm()
  }

  function _error(response) {
    alert(response.data.message || response.statusText);
  }

  // Clear the form
  function _clearForm() {
    $scope.form.message = "";
    $scope.form.severity = "";
    $scope.form.id = create_UUID();
    $scope.vform.message = "";
    $scope.vform.severity = "";
    $scope.vform.id = create_UUID();
  }
});
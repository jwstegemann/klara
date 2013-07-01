'use strict';

/* Controllers */

function SchuelerListCtrl($scope, $http, $filter, message) {
  var url = "/schueler"

  $scope.schuelerList = [];
  $scope.columns = [
    {title: "Name", attribute: "name"},
    {title: "Vorname", attribute: "vorname"},
    {title: "Geschlecht", attribute: "geschlecht"}
  ];

  $scope.reload = function() {
		$http.get(url).success(function(response) {
			$scope.schuelerList = response;
      console.log("XXXXXXXXXXXXXXXXXXXXXXX: reloaded!");
		}).error(function(data, status, headers, config) {
      //TODO: msg
			alert("Fehler: " + data	);
		});
  }

  $scope.init = function() {
    $scope.reload();
  }

  $scope.deleteSchueler = function(schuelerId) {
    $http.delete(url + "/" + schuelerId).success(function(response) {
        message.success('Löschen erfolgreich.','Die Entität mit der id ' + schuelerId + ' wurde entfernt.');
      })     
  }
}


/*
 *
 */

 function SchuelerDetailCtrl($scope, $routeParams, $http, $location, dictionary, message) {

  var createMode = false;
  if($routeParams.schuelerId == undefined) createMode = true;

  var request = {};

  if (createMode) {
    request.url = "/schueler";
    request.method = "POST";
  }
  else {
    request.url = "/schueler/" + $routeParams.schuelerId;
    request.method = "PUT";
  }

  $scope.schueler = { _id: null, version: null }
  $scope.validation = { errors: [] }
  
  $scope.reload = function() {
    $http.get(request.url).success(function(response) {
      $scope.schueler = response;
    });
  }

  $scope.save = function() {
    request.data = $scope.schueler;
    $http(request).success(function(response) {
      message.success('Herzlichen Glückwunch!','Ihre Änderungen wurden erfolgreich gespeichert.');
    }).error(function(data, status, headers, config) {
      if(status == 412) {
        //clear header messages
        $scope.validation.errors = [];

        //clear all tips
        $(".validationResult").html("");

        //add tips to fields and header message
        angular.forEach(data, function(msg, key) {
          var controlGroup = $("#" + msg.field);
          controlGroup.addClass("error");
          controlGroup.find(".validationResult").html(msg.text);
          $scope.validation.errors.push(msg);
        });
      }
    });
  }

  $scope.goToList = function() {
    $location.path("/schueler");
  }

  $scope.init = function() {
    if ($routeParams.schuelerId != undefined) {
      $scope.reload();
    }

    /*
     * init dictionaries
     */
    $scope.geschlecht = dictionary.get("Geschlecht");
  }

}


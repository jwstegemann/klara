'use strict';

/* Controllers */

function SchuelerListCtrl($scope, $http) {
  var url = "/schueler"

  $scope.schuelerList = [];
  $scope.schuelerTable = { filter: undefined, predicate: "name", reverse: false };

  $scope.schuelerSelection = {};

  $scope.reload = function() {
		$http.get(url).success(function(response) {
			$scope.schuelerList = response;
		}).error(function(data, status, headers, config) {
			alert("Fehler: " + data	);
		});
  }

  $scope.init = function() {
    $scope.reload();
  }

  $scope.sort = function(attribute) {
    if (attribute == $scope.schuelerTable.predicate) {
      $scope.schuelerTable.reverse = !$scope.schuelerTable.reverse;
    }
    else {
      $scope.schuelerTable.predicate = attribute;
      $scope.schuelerTable.reverse = false;
    }
  }

  $scope.select = function() {
    console.log(angular.toJson($scope.schuelerSelection, true));
  }

}


/*
 *
 */

 function SchuelerDetailCtrl($scope, $routeParams, $http, $location) {

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
    }).error(function(data, status, headers, config) {
      alert("Fehler: " + data );
    });
  }

  $scope.save = function() {
    request.data = $scope.schueler;
    $http(request).success(function(response) {
      alert("Erfolgreich gespeichert!")
    }).error(function(data, status, headers, config) {
      if(status == 412) {
        //clear all tips
        $(".validationResult").html("");

        angular.forEach(data, function(msg, key) {
          var controlGroup = $("#" + msg.field);
          controlGroup.addClass("error");
          controlGroup.find(".validationResult").html(msg.text);
          $scope.validation.errors.push(msg);
        });
      }
      else alert("Fehler: " + angular.toJson(data,true) );
    });
  }

  $scope.goToList = function() {
    $location.path("/schueler");
  }

  $scope.init = function() {
    if ($routeParams.schuelerId != undefined) {
      $scope.reload();
    }
  }

}


'use strict';

/* Controllers */

function SchuelerListCtrl($scope, $http, $filter) {
  var url = "/schueler"

  $scope.schuelerList = [];
  $scope.schuelerTable = { filter: undefined, predicate: "name", reverse: false };

  $scope.schuelerSelection = {};

  $scope.reload = function() {
		$http.get(url).success(function(response) {
			$scope.schuelerList = response;
		}).error(function(data, status, headers, config) {
      //TODO: msg
			alert("Fehler: " + data	);
		});
  }

  $scope.init = function() {
    $scope.reload();

    /*
     * handling col-resizing
     */

    var pressed = false;
    var start = undefined;
    var startX, startWidth;

    // prevent sorting when resizing cols
    $("table thead tr th .sizehandler").click(function(e) {
      e.stopImmediatePropagation()
    });
    
    $("table thead tr th .sizehandler").mousedown(function(e) {
        start = $(this).parent();
        pressed = true;
        startX = e.pageX;
        startWidth = start.width();

        e.preventDefault();

        $(start.parent().mousemove(function(e) {
          if(pressed) {
              $(start).width(startWidth+(e.pageX-startX));
              e.preventDefault();
          }
        }));
    });

    $(document).mouseup(function(e) {
        e.preventDefault();

        if(pressed) {
            pressed = false;
            start.parent().off('mousemove');
        }
    });
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

  $scope.selectAll = function() {
    angular.forEach($scope.schuelerList, function(item, index) {
      item.selected = $scope.schuelerTable.selectAll;
    });
  }

  $scope.selectedItems = function() {
    var visibleItems = $filter('filter')($scope.schuelerList, $scope.schuelerTable.filter);
    return $filter('filter')(visibleItems, {selected: true});
  }

  $scope.deleteSchueler = function(schuelerId) {
    $http.delete(url + "/" + schuelerId).success(function(response) {
        //TODO: msg
        console.log("deleted " + schuelerId);
      }).error(function(data, status, headers, config) {
        //TODO: msg
        alert("Fehler: " + data );
    });      
  }

  $scope.deleteSelected = function() {
    angular.forEach($scope.selectedItems(), function(item, index) {
      $scope.deleteSchueler(item._id);
    });
    $scope.reload();
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


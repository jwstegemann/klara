'use strict';

/* Controllers */

function SchuelerListCtrl($scope) {
  $scope.phones = [{ name: "Karl" },{ name: "Otto" }];
}

//SchuelerListCtrl.$inject = ['$scope'];

/*
 *
 */

 function SchuelerDetailCtrl($scope, $routeParams, $http) {
  $scope.user = {};
 
  $scope.schueler = { name: "Karl", id: $routeParams.schuelerId };
  
    $scope.restrictedAction = function() {
      $http.get('/user/info').success(function(response) {
        // this piece of code will not be executed until user is authenticated
        $scope.user = response;
      });
    }
}

//PhoneDetailCtrl.$inject = ['$scope', '$routeParams', '$http'];
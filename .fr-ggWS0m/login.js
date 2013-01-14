'use strict';

/* Controllers */

function LoginCtrl($scope, $http, authService) {
	$scope.username="";
	$scope.password="";

    $scope.submit = function() {

		console.log("username: " + $scope.username);
		console.log("passoword: " + $scope.password);
    
      var credentials = $.base64.encode($scope.username + ":" + $scope.password);
      
      console.log("credentials: " + credentials);
    
      $http.get('user/login',{headers: {'Authorization': 'Basic ' + credentials}}).success(function() {
        authService.loginConfirmed();
      });
    }
  }

//SchuelerListCtrl.$inject = ['$scope','$http','authService'];

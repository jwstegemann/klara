'use strict';

/* Controllers */

function LoginCtrl($scope, $http, authService) {
	$scope.username="";
	$scope.password="";

    $scope.submit = function() {

		console.log("username: " + $scope.username);
		console.log("passoword: " + $scope.password);
    
		var loginRequest = {username: $scope.username, password: $scope.password}
	
      $http.post('/user/login',loginRequest)
		.success(function() {
			authService.loginConfirmed();
		})
		.error(function(response) {
			console.log(response);
			alert("Sie konnten nicht angemeldet werden!");
		});
    }
  }

//SchuelerListCtrl.$inject = ['$scope','$http','authService'];
